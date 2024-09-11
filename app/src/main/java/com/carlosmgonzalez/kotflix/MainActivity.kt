package com.carlosmgonzalez.kotflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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