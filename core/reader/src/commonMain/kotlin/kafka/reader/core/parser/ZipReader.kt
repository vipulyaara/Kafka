package kafka.reader.core.parser

import okio.Source

interface ZipReader {
    data class Entry(
        val name: String,
        val isDirectory: Boolean,
        val data: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Entry

            if (name != other.name) return false
            if (isDirectory != other.isDirectory) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + isDirectory.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    fun entries(): Sequence<Entry>
    fun close()

    companion object {
        operator fun invoke(source: Source): ZipReader = createZipReader(source)
    }
}

// Expect function for platform-specific implementation
expect fun createZipReader(source: Source): ZipReader