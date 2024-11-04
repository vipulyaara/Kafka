package kafka.reader.core.parser

import okio.Source
import okio.buffer
import java.util.zip.ZipInputStream

actual fun createZipReader(source: Source): ZipReader = AndroidZipReader(source)

class AndroidZipReader(private val source: Source) : ZipReader {
    private val zipStream = ZipInputStream(source.buffer().inputStream())

    override fun entries(): Sequence<ZipReader.Entry> = sequence {
        var entry = zipStream.nextEntry
        while (entry != null) {
            val data = if (!entry.isDirectory) {
                zipStream.readBytes()
            } else {
                ByteArray(0)
            }
            yield(ZipReader.Entry(entry.name, entry.isDirectory, data))
            zipStream.closeEntry()
            entry = zipStream.nextEntry
        }
    }

    override fun close() {
        zipStream.close()
        source.close()
    }
}
