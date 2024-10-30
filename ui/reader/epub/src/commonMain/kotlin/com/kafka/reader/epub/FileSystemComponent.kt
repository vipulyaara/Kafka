package com.kafka.reader.epub

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides
import okio.FileSystem
import okio.SYSTEM

interface FileSystemComponent {

    @Provides
    @ApplicationScope
    fun provideFileSystem(): FileSystem = FileSystem.SYSTEM

}
