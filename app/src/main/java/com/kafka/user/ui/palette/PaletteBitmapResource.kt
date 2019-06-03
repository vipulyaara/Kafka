package com.kafka.user.ui.palette

import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.util.Util

internal class PaletteBitmapResource(
    private val mPaletteBitmapContainer: PaletteBitmapContainer,
    private val mBitmapPool: BitmapPool
) : Resource<PaletteBitmapContainer> {

    override fun getResourceClass(): Class<PaletteBitmapContainer> {
        return PaletteBitmapContainer::class.java
    }

    override fun get(): PaletteBitmapContainer {
        return this.mPaletteBitmapContainer
    }

    override fun getSize(): Int {
        return Util.getBitmapByteSize(this.mPaletteBitmapContainer.bitmap)
    }

    override fun recycle() {
        this.mBitmapPool.put(this.mPaletteBitmapContainer.bitmap)
    }
}
