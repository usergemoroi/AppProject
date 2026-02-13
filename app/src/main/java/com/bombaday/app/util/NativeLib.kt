package com.bombaday.app.util

object NativeLib {
    init {
        System.loadLibrary("bombaday")
    }
    
    external fun getVersion(): String
}
