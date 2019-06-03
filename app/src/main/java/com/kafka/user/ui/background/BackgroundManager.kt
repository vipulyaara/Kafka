package com.kafka.user.ui.background

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.palette.graphics.Palette
import com.kafka.user.R
import com.kafka.user.ui.ColorUtils

/**
 * @author Vipul Kumar; dated 14/03/19.
 */
class BackgroundManager(backgroundView: View) {

    private val BITMAP_HEIGHT = 540
    private val BITMAP_SCALE = 0.5f
    private val BITMAP_WIDTH = 960
    private val DARK_MODE_COLOR_DARKEN_FACTOR = 0.5f
    private val DEBUG = false
    private val FALLBACK_COLOR = -16777216
    private val RADIAL_GRADIENT_VERTICAL_SHIFT = -300
    private val STANDARD_COLOR_DARKEN_FACTOR = 0.4f
    private val TAG = "HomeBackground"
    private val TOP_GRADIENT_COLOR_MIX_AMOUNT = 0.2f
    private val TRANSITION_DURATION_MILLIS = 600
    @ColorInt
    private var mColor1: Int = 0
    @ColorInt
    private var mColor2: Int = 0
    @ColorInt
    private var mColor3: Int = 0
    private val mContext: Context by lazy { backgroundView.context }
    private var mDarkMode = false
    private var mGenerateBitmapTask: GenerateBitmapTask? = null
    private var mLinearGradientPaint: Paint? = null
    private var mOverlayBitmap: Bitmap? = null
    private var mOverlayPaint: Paint? = null
    private var mRadialGradientPaint: Paint? = null
    private var mTransitionDrawable: BackgroundTransitionDrawable

    // we are cancelling properly
    @SuppressLint("StaticFieldLeak")
    private inner class GenerateBitmapTask :
        AsyncTask<BitmapDrawable, Void, Void>() {

        override fun doInBackground(vararg bitmapDrawable: BitmapDrawable): Void? {
            this@BackgroundManager.generateBitmap(bitmapDrawable[0])
            return null
        }

        @MainThread
        override fun onPostExecute(result: Void) {
            this@BackgroundManager.mGenerateBitmapTask = null
            this@BackgroundManager.mTransitionDrawable.animateFadeIn(600)
        }
    }

    init {
        this.mTransitionDrawable = BackgroundTransitionDrawable(
            arrayOf(
                createBackgroundDrawable(),
                createBackgroundDrawable(),
                createBackgroundDrawable()
            )
        )
        backgroundView.background = this.mTransitionDrawable
    }

    private fun createBackgroundDrawable(): BitmapDrawable {
        val backgroundBitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888, false)
        } else {
            Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888)
        }
        val drawable = BitmapDrawable(this.mContext.resources, backgroundBitmap)
        drawable.isAutoMirrored = true
        return drawable
    }

    @MainThread
    fun updateBackground(@NonNull palette: Palette) {
        updateBackground(
            ColorUtils.darkenColor(
                palette.getVibrantColor(palette.getMutedColor(-16777216)),
                STANDARD_COLOR_DARKEN_FACTOR
            ),
            ColorUtils.darkenColor(
                palette.getDarkVibrantColor(palette.getDarkMutedColor(-16777216)),
                STANDARD_COLOR_DARKEN_FACTOR
            ),
            ColorUtils.darkenColor(
                palette.getLightVibrantColor(palette.getLightMutedColor(-16777216)),
                STANDARD_COLOR_DARKEN_FACTOR
            )
        )
    }

    @MainThread
    private fun updateBackground(@ColorInt color1: Int, @ColorInt color2: Int, @ColorInt color3: Int) {
        this.mDarkMode = false
        if (this.mColor1 != color1 || this.mColor2 != color2 || this.mColor3 != color3) {
            this.mColor1 = color1
            this.mColor2 = color2
            this.mColor3 = color3
            if (this.mGenerateBitmapTask != null) {
                this.mGenerateBitmapTask!!.cancel(true)
            }
            this.mGenerateBitmapTask = GenerateBitmapTask()
            this.mGenerateBitmapTask!!.execute(this.mTransitionDrawable.backBuffer as BitmapDrawable)
        }
    }

    fun enterDarkMode() {
        if (!this.mDarkMode) {
            updateBackground(
                ColorUtils.darkenColor(this.mColor1, 0.5f),
                ColorUtils.darkenColor(this.mColor2, 0.5f),
                ColorUtils.darkenColor(this.mColor3, 0.5f)
            )
            this.mDarkMode = true
        }
    }

    @ColorInt
    @WorkerThread
    private fun mixColors(@ColorInt color1: Int, @ColorInt color2: Int, amount: Float): Int {
        val inverseAmount = 1.0f - amount
        return Color.rgb(
            (Color.red(color1).toFloat() * amount + Color.red(color2).toFloat() * inverseAmount).toInt(),
            (Color.green(color1).toFloat() * amount + Color.green(color2).toFloat() * inverseAmount).toInt(),
            (Color.blue(color1).toFloat() * amount + Color.blue(color2).toFloat() * inverseAmount).toInt()
        )
    }

    @WorkerThread
    private fun generateBitmap(bitmapDrawable: BitmapDrawable) {
        val linearGradient = LinearGradient(
            0.0f,
            0.0f,
            0.0f,
            540.0f,
            mixColors(this.mColor2, this.mColor1, TOP_GRADIENT_COLOR_MIX_AMOUNT),
            this.mColor2,
            Shader.TileMode.CLAMP
        )
        if (this.mLinearGradientPaint == null) {
            this.mLinearGradientPaint = Paint()
        }
        this.mLinearGradientPaint!!.shader = linearGradient
        val radialGradient = RadialGradient(
            960.0f,
            -300.0f,
            Math.sqrt(1627200.0).toInt().toFloat(),
            this.mColor3,
            0,
            Shader.TileMode.CLAMP
        )
        if (this.mRadialGradientPaint == null) {
            this.mRadialGradientPaint = Paint()
        }
        this.mRadialGradientPaint!!.shader = radialGradient
        if (this.mOverlayBitmap == null) {
            this.mOverlayBitmap = BitmapFactory.decodeResource(
                this.mContext.resources,
                R.drawable.home_background_overlay
            )
            this.mOverlayPaint = Paint()
            this.mOverlayPaint!!.shader =
                BitmapShader(this.mOverlayBitmap!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        }
        val c = Canvas(bitmapDrawable.bitmap)
        c.drawRect(0.0f, 0.0f, 960.0f, 540.0f, this.mLinearGradientPaint!!)
        c.drawRect(0.0f, 0.0f, 960.0f, 540.0f, this.mRadialGradientPaint!!)
        c.drawRect(0.0f, 0.0f, 960.0f, 540.0f, this.mOverlayPaint!!)
    }
}
