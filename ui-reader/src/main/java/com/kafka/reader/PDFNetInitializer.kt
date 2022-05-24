package com.kafka.reader

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build.VERSION
import com.kafka.reader.TrialKeyProvider.generateTrialKey
import com.pdftron.common.PDFNetException
import timber.log.Timber

class PDFNetInitializer : ContentProvider() {
    override fun onCreate(): Boolean {
        val applicationContext = this.context
        val key = getLicenseKey(applicationContext)
        if (key != null && applicationContext != null) {
            try {
//                PDFNet.initialize(applicationContext, R.raw.pdfnet, key)
            } catch (var4: PDFNetException) {
                var4.printStackTrace()
            }
        } else {
            Timber.w(
                "PDFNetInitializer",
                "Tried to auto-initialize PDFTron SDK but no license key was found.\nA trial demo key will be generated for you to use for the next session if internet connection is available.\nTo add your own license key, see guide: https://www.pdftron.com/documentation/android/guides/getting-started/add-license."
            )
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        checkPackage(this.context, this)
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        private const val TAG = "PDFNetInitializer"
        const val MSG =
            "Tried to auto-initialize PDFTron SDK but no license key was found.\nA trial demo key will be generated for you to use for the next session if internet connection is available.\nTo add your own license key, see guide: https://www.pdftron.com/documentation/android/guides/getting-started/add-license."

        fun checkPackage(context: Context?, contentProvider: ContentProvider) {
            var callingPackage: String? = null
            if (VERSION.SDK_INT >= 26 && VERSION.SDK_INT <= 28) {
                callingPackage = contentProvider.callingPackage
                if (context == null || callingPackage == null || callingPackage != context.packageName) {
                    throw SecurityException("Provider should not be called outside of app!")
                }
            }
        }

        fun getLicenseKey(applicationContext: Context?): String? {
            return generateTrialKey()
        }
    }
}
