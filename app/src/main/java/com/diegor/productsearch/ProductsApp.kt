package com.diegor.productsearch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProductsApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}