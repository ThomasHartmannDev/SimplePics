// Viewmodel

package ch.hartmannsdev.simplepics.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.hartmannsdev.simplepics.data.Event
import ch.hartmannsdev.simplepics.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

var USERS = "users"
@HiltViewModel
class SimplePicsViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel() {
    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf<Event<String>?>(null)
    val snackbarMessage = mutableStateOf<Event<String>?>(null) // Add this line

    // init is called when the ViewModel is created
    init {
        //auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let {uid ->
            getUserData(uid)
        }
    }

    fun onSignUp(username: String ,email: String, password: String) {
        inProgress.value = true
        db.collection(USERS).whereEqualTo("username", username).get()
            .addOnSuccessListener {documents ->
                if(documents.size() > 0){
                    handleException(customMessage = "Username already exists")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener{task ->
                            if(task.isSuccessful){
                                signedIn.value = true
                                createOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "Sign up failed")
                            }
                            inProgress.value = false
                        }
                }

            }
            .addOnFailureListener{exception ->
                handleException(exception, "Something went wrong")
            }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid // uid from the authenticated user!
        val userData = UserData(
            userId = uid,
            name = name?: userData.value?.name,
            username = username?: userData.value?.username,
            bio = bio?: userData.value?.bio,
            imageUrl = imageUrl?: userData.value?.imageUrl,
            following = userData.value?.following,
        )
        uid?.let {uid ->
            inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if(it.exists()){
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                inProgress.value = false
                            }
                            .addOnFailureListener{
                                handleException(it, "Cannot update user")
                                inProgress.value = false

                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        inProgress.value = false
                    }
                }
                .addOnFailureListener{
                    handleException(it, "Cannot create user")
                    inProgress.value = false
                }
        }




    }

    fun onLogin(email: String, password: String){
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {  task ->
                if(task.isSuccessful){
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {uid ->
                        //handleException(customMessage = "Login successful")
                        getUserData(uid)
                    }
                } else {
                    handleException(task.exception, "Login failed")
                    inProgress.value = false
                }


            }
            .addOnFailureListener{exc ->
                handleException(exc, "Login failed")
                inProgress.value = false
            }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                userData.value = it.toObject(UserData::class.java)
                signedIn.value = true
                inProgress.value = false
                snackbarMessage.value = Event("User data retrieved successfully")
            }

            .addOnFailureListener{exc ->
                handleException(exc, "Cannot get user data")
                inProgress.value = false
            }
    }

    fun signOut(){
        auth.signOut()
        signedIn.value = false
        userData.value = null
    }

    fun forgotPassword(email: String) {
        inProgress.value = true
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    inProgress.value = false
                    snackbarMessage.value = Event("Password reset email sent")
                } else {
                    handleException(task.exception, "Password reset failed - Please insert a valid email")
                }
            }
            .addOnFailureListener{
                handleException(it, "Password reset failed - Please insert a valid email")
                inProgress.value = false
            }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage?: ""
        val message = if(customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        //popupNotification.value = Event(message)
        snackbarMessage.value = Event(message) // Add this line
    }


    fun updateProfileData(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
    ){
        createOrUpdateProfile(name, username, bio)
    }

    private fun uploadImage(uri: Uri, onSucess: (Uri) -> Unit){
        inProgress.value = true

        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask
            .addOnSuccessListener {
                val result = it.metadata?.reference?.downloadUrl
                result?.addOnSuccessListener(onSucess)

            }
            .addOnFailureListener {exc ->
                handleException(exc, "Cannot upload image")
                inProgress.value = false
            }
    }

    fun uploadProfileImage(uri: Uri){
        uploadImage(uri){
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    fun onLogout(){
        auth.signOut()
        signedIn.value = false
        userData.value = null
    }

}

