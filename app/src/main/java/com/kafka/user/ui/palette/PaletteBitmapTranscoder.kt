package com.kafka.user.ui.palette

import android.content.Context
import android.graphics.Bitmap

import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import androidx.palette.graphics.Palette

internal class PaletteBitmapTranscoder(context: Context) :
    ResourceTranscoder<Bitmap, PaletteBitmapContainer> {
    private val mBitmapPool: BitmapPool by lazy { Glide.get(context).bitmapPool }

    override fun transcode(
        toTranscode: Resource<Bitmap>,
        options: Options
    ): Resource<PaletteBitmapContainer>? {
        val bitmap = toTranscode.get()
        return PaletteBitmapResource(
            PaletteBitmapContainer(
                bitmap,
                Palette.from(bitmap).generate()
            ), this.mBitmapPool
        )
    }
}
