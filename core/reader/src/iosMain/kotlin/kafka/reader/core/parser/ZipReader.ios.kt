@file:OptIn(ExperimentalForeignApi::class)

package kafka.reader.core.parser

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import okio.Source
import okio.buffer
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.NSMakeRange
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.closeFile
import platform.Foundation.create
import platform.Foundation.fileHandleForReadingAtPath
import platform.Foundation.getBytes
import platform.Foundation.readDataToEndOfFile
import platform.Foundation.writeToFile
import platform.posix.remove
import kotlin.random.Random

actual fun createZipReader(source: Source): ZipReader = IOSZipReader(source)

private class IOSZipReader(private val source: Source) : ZipReader {
    private val buffer = source.buffer()
    private var closed = false
    private var tempFile: String? = null

    override fun entries(): Sequence<ZipReader.Entry> = sequence {
        if (closed) return@sequence

        try {
            val bytes = buffer.readByteArray()
            tempFile = createTempFile(bytes)

            val fileHandle = NSFileHandle.fileHandleForReadingAtPath(tempFile!!)
                ?: throw IllegalStateException("Failed to open temporary file")

            try {
                val data = fileHandle.readDataToEndOfFile()
                val size = data.length.toInt()

                var position = 0
                while (position < size - 4) {
                    if (isLocalFileHeader(data, position)) {
                        val entry = parseZipEntry(data, position)
                        if (entry != null) {
                            yield(entry)
                            position += entry.data.size + entry.name.length + 30 // header size
                        } else {
                            position += 4
                        }
                    } else {
                        position++
                    }
                }
            } finally {
                fileHandle.closeFile()
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to read ZIP file: ${e.message}")
        }
    }

    private fun isLocalFileHeader(data: NSData, offset: Int): Boolean {
        val signature = ByteArray(4)
        signature.usePinned { pinnedSignature ->
            data.getBytes(pinnedSignature.addressOf(0), NSMakeRange(offset.toULong(), 4.toULong()))
        }
        return signature[0].toInt() == 0x50 &&
               signature[1].toInt() == 0x4B &&
               signature[2].toInt() == 0x03 &&
               signature[3].toInt() == 0x04
    }

    private fun parseZipEntry(data: NSData, offset: Int): ZipReader.Entry? {
        val headerBuffer = ByteArray(30)
        headerBuffer.usePinned { pinnedHeader ->
            data.getBytes(pinnedHeader.addressOf(0), NSMakeRange(offset.toULong(), 30.toULong()))
        }

        val nameLength = ((headerBuffer[27].toInt() and 0xFF) shl 8) or (headerBuffer[26].toInt() and 0xFF)
        val extraLength = ((headerBuffer[29].toInt() and 0xFF) shl 8) or (headerBuffer[28].toInt() and 0xFF)
        val compressedSize = ((headerBuffer[21].toInt() and 0xFF) shl 24) or
                           ((headerBuffer[20].toInt() and 0xFF) shl 16) or
                           ((headerBuffer[19].toInt() and 0xFF) shl 8) or
                           (headerBuffer[18].toInt() and 0xFF)

        if (offset + 30 + nameLength + extraLength + compressedSize > data.length.toInt()) {
            return null
        }

        val nameBytes = ByteArray(nameLength)
        nameBytes.usePinned { pinnedName ->
            data.getBytes(pinnedName.addressOf(0), NSMakeRange((offset + 30).toULong(), nameLength.toULong()))
        }
        val name = nameBytes.decodeToString()

        val fileData = ByteArray(compressedSize)
        fileData.usePinned { pinnedData ->
            data.getBytes(pinnedData.addressOf(0),
                NSMakeRange((offset + 30 + nameLength + extraLength).toULong(), compressedSize.toULong()))
        }

        return ZipReader.Entry(
            name = name,
            isDirectory = name.endsWith("/"),
            data = fileData
        )
    }

    private fun createTempFile(bytes: ByteArray): String {
        val tempDir = NSTemporaryDirectory()
        val fileName = "temp_zip_${Random.nextInt()}.zip"
        val tempFile = tempDir + fileName
        
        val data = bytes.toNSData()
        if (!data.writeToFile(tempFile, true)) {
            throw IllegalStateException("Failed to write temporary file")
        }
        
        return tempFile
    }

    override fun close() {
        if (!closed) {
            closed = true
            source.close()
            tempFile?.let { remove(it) }
            tempFile = null
        }
    }
}

private fun ByteArray.toNSData(): NSData {
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}
