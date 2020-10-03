package com.kafka.logger

import com.kafka.logger.firebase.CrashlyticsCrashLogger
import com.kafka.logger.firebase.FirebaseEventLogger
import com.kafka.logger.loggers.CrashLogger
import com.kafka.logger.loggers.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggerModuleBinds {

    @Binds
    abstract fun logger(firebaseEventLogger: FirebaseEventLogger): Logger

    @Binds
    abstract fun crashLogger(crashlyticsCrashLogger: CrashlyticsCrashLogger): CrashLogger
}
