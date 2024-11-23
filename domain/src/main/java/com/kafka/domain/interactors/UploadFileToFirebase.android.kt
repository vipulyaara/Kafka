package com.kafka.domain.interactors

import dev.gitlive.firebase.storage.Data

actual fun storageData(byteArray: ByteArray) = Data(byteArray)