package com.carlosmgonzalez.kotflix

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.carlosmgonzalez.kotflix.ui.KotflixApp
import com.carlosmgonzalez.kotflix.ui.theme.KotflixTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        storage = Firebase.storage

//        val videoUri = "https://res.cloudinary.com/difikt7so/video/upload/v1726230451/android-apps/cekcjxepyjydnq2oca5j.mp4"

        enableEdgeToEdge()
        setContent {
            KotflixTheme {
                Surface(
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    KotflixApp()
                }
            }
        }
    }
}