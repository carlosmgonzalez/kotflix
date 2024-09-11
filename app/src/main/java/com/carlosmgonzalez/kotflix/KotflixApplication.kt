package com.carlosmgonzalez.kotflix

import android.app.Application
import com.carlosmgonzalez.kotflix.data.AppContainer
import com.carlosmgonzalez.kotflix.data.DefaultAppContainer

class KotflixApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}