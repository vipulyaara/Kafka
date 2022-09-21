package com.kafka.user

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import org.kafka.common.extensions.asString
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

data class GrantResult(
    val permission: String,
    val granted: Boolean
)

/**
 * Helps us manage, check, and dispatch permission requests without much boiler plate in our Activities
 * or views.
 */
interface PermissionsManager {

    fun onGrantResult(): Flow<GrantResult>

    fun attach(activity: Activity)

    fun hasStoragePermission(): Boolean

    fun requestStoragePermission(waitForGranted: Boolean = false): Flow<GrantResult>

    fun processResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

    fun detach(activity: Activity)
}

@Singleton
class RealPermissionsManager @Inject constructor(
    private val context: Application
) : PermissionsManager {

    companion object {
        const val REQUEST_CODE_STORAGE = 69
    }

    var activity: Activity? = null
    private val relay = ConflatedBroadcastChannel<GrantResult>()

    override fun onGrantResult(): Flow<GrantResult> = relay.asFlow()

    override fun attach(activity: Activity) {
        Timber.d("attach(): $activity")
        this.activity = activity
    }

    override fun hasStoragePermission() = hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun requestStoragePermission(waitForGranted: Boolean) =
        requestPermission(
            REQUEST_CODE_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            waitForGranted
        )

    override fun processResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Timber.d(
            "processResult(): requestCode= %d, permissions: %s, grantResults: %s",
            requestCode, permissions.asString(), grantResults.toString()
        )
        for ((index, permission) in permissions.withIndex()) {
            val granted = grantResults[index] == PERMISSION_GRANTED
            val result = GrantResult(permission, granted)
            Timber.d("Permission grant result: %s", result)
            relay.trySendBlocking(result)
        }
    }

    override fun detach(activity: Activity) {
        // === is referential equality - returns true if they are the same instance
        if (this.activity === activity) {
            Timber.d("detach(): $activity")
            this.activity = null
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    }

    private fun requestPermission(
        code: Int,
        permission: String,
        waitForGranted: Boolean
    ): Flow<GrantResult> {
        if (hasPermission(permission)) {
            Timber.d("Already have this permission!")
            return flow {
                emit(GrantResult(permission, true).also {
                    relay.send(it)
                })
            }
        }

        val attachedTo = activity ?: throw IllegalStateException("Not attached")
        ActivityCompat.requestPermissions(attachedTo, arrayOf(permission), code)
        return onGrantResult()
            .filter { it.permission == permission }
            .filter {
                if (waitForGranted) {
                    // If we are waiting for granted, only allow emission if granted is true
                    it.granted
                } else {
                    // Else continue
                    true
                }
            }
            .take(1)
    }
}
