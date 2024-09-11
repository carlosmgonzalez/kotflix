package com.carlosmgonzalez.kotflix.ui.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _uiState = MutableStateFlow(
        AuthUiState(
            auth.currentUser
        )
    )

    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _uiState.update {
                AuthUiState(firebaseAuth.currentUser)
            }
        }
    }

    fun updateUserPhoto(uri: Uri, updateUi: (uri: Uri) -> Unit) {
        val storageRef = storage.reference
        val userUid = auth.currentUser!!.uid
        val fileName = "${System.currentTimeMillis()}.jpg"

        val reference = storageRef.child("userPhoto/$userUid/$fileName")

        val currentPhoto = storageRef.child("userPhoto/${userUid}")

        currentPhoto.listAll().addOnSuccessListener { list ->
            if (list.items.isNotEmpty()) {
                val deleteTask = list.items.map { it.delete() }
                Tasks.whenAllComplete(deleteTask).addOnSuccessListener {
                    val uploadTask = reference.putFile(uri)

                    uploadTask.addOnFailureListener { error ->
                        Log.e("updateUserPhoto", "Error upload user photo", error)
                    }.addOnSuccessListener {
                        reference.downloadUrl.addOnSuccessListener { downloadUri ->
                            val profileUpdate = userProfileChangeRequest {
                                photoUri = downloadUri
                            }

                            updateUi(downloadUri)

                            auth.currentUser!!.updateProfile(profileUpdate)
                                .addOnCompleteListener { taskUpdateProfile ->
                                    if (taskUpdateProfile.isSuccessful) {
                                        Log.d("updateUserPhoto", "User photo updated")
                                    } else {
                                        Log.e(
                                            "updateUserPhoto",
                                            "Error update user photo",
                                            taskUpdateProfile.exception
                                        )
                                    }
                                }

                        }.addOnFailureListener { error ->
                            Log.e("updateUserPhoto", "Error getting download URL", error)
                        }
                    }
                }
            }
        }
    }

    fun sighOut() {
        auth.signOut()
    }
}

data class AuthUiState(
    val user: FirebaseUser?
)