package com.bombaday.app.util

import android.graphics.Bitmap

object VideoCompressor {
    init {
        System.loadLibrary("bombaday")
    }
    
    external fun compressImageNative(bitmap: Bitmap, quality: Int): ByteArray?
    external fun applyBlurNative(bitmap: Bitmap, radius: Int)
    
    fun compressImage(bitmap: Bitmap, quality: Int = 75): ByteArray? {
        return try {
            compressImageNative(bitmap, quality)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun applyBlur(bitmap: Bitmap, radius: Int = 10) {
        try {
            applyBlurNative(bitmap, radius)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
