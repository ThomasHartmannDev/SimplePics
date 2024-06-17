package ch.hartmannsdev.simplepics.ui.viewmodels

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ch.hartmannsdev.simplepics.data.Event
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

/* Constants */
var USERS = "users"
var POSTS = "posts"


/**
 * ViewModel for managing user authentication, profile data, and posts in a simple picture sharing app.
 *
 * @property auth Firebase Authentication instance for handling user sign-in, sign-up, and sign-out operations.
 * @property db Firebase Firestore instance for interacting with the database.
 * @property storage Firebase Storage instance for handling image uploads.
 */
@HiltViewModel
class SimplePicsViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel() {

    /**
     * Represents whether a user is signed in.
     */
    val signedIn = mutableStateOf(false)

    /**
     * Represents whether an operation is in progress.
     */
    val inProgress = mutableStateOf(false)

    /**
     * Holds the current user data.
     */
    val userData = mutableStateOf<UserData?>(null)

    /**
     * Holds the current popup notification message.
     */
    val popupNotification = mutableStateOf<Event<String>?>(null)

    /**
     * Holds the current snackbar message.
     */
    val snackbarMessage = mutableStateOf<Event<String>?>(null)

    /**
     * URI for the camera image.
     */
    var cameraImageUri: Uri? = null

    /**
     * Represents whether the refresh posts operation is in progress.
     */
    val refreshPostsProgress = mutableStateOf(false)

    /**
     * Holds the list of posts for the current user.
     */
    val posts = mutableStateOf<List<PostData>>(listOf())

    /**
     * Initializes the ViewModel and checks the current user's authentication status.
     */
    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    /**
     * Creates a new image URI.
     *
     * @param contentResolver The content resolver used to create the URI.
     * @return The created image URI.
     */
    fun createImageUri(contentResolver: ContentResolver): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "new_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    /**
     * Handles user sign-up.
     *
     * @param username The desired username of the user.
     * @param email The email address of the user.
     * @param password The password for the user account.
     */
    fun onSignUp(username: String, email: String, password: String) {
        inProgress.value = true
        db.collection(USERS).whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    handleException(customMessage = "Username already exists")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "Sign up failed")
                            }
                            inProgress.value = false
                        }
                }
            }
            .addOnFailureListener { exception ->
                handleException(exception, "Something went wrong")
            }
    }

    /**
     * Creates or updates the user profile data in the Firestore database.
     *
     * @param name The name of the user.
     * @param username The username of the user.
     * @param bio The bio of the user.
     * @param imageUrl The URL of the user's profile image.
     */
    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            username = username ?: userData.value?.username,
            bio = bio ?: userData.value?.bio,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
            following = userData.value?.following,
        )
        uid?.let { uid ->
            inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                inProgress.value = false
                            }
                            .addOnFailureListener {
                                handleException(it, "Cannot update user")
                                inProgress.value = false
                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        inProgress.value = false
                    }
                }
                .addOnFailureListener {
                    handleException(it, "Cannot create user")
                    inProgress.value = false
                }
        }
    }

    /**
     * Handles user login.
     *
     * @param email The email address of the user.
     * @param password The password for the user account.
     */
    fun onLogin(email: String, password: String) {
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let { uid ->
                        getUserData(uid)
                    }
                } else {
                    handleException(task.exception, "Login failed")
                    inProgress.value = false
                }
            }
            .addOnFailureListener { exc ->
                handleException(exc, "Login failed")
                inProgress.value = false
            }
    }

    /**
     * Retrieves user data from the Firestore database.
     *
     * @param uid The user ID of the user whose data is to be retrieved.
     */
    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                userData.value = it.toObject(UserData::class.java)
                signedIn.value = true
                inProgress.value = false
                refreshPosts()
            }
            .addOnFailureListener { exc ->
                handleException(exc, "Cannot get user data")
                inProgress.value = false
            }
    }

    /**
     * Signs out the current user.
     */
    fun signOut() {
        auth.signOut()
        signedIn.value = false
        userData.value = null
    }

    /**
     * Sends a password reset email to the user.
     *
     * @param email The email address of the user.
     */
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
            .addOnFailureListener {
                handleException(it, "Password reset failed - Please insert a valid email")
                inProgress.value = false
            }
    }

    /**
     * Handles exceptions and displays a snackbar message.
     *
     * @param exception The exception that occurred.
     * @param customMessage A custom message to be displayed.
     */
    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        snackbarMessage.value = Event(message)
    }

    /**
     * Updates the user profile data.
     *
     * @param name The name of the user.
     * @param username The username of the user.
     * @param bio The bio of the user.
     */
    fun updateProfileData(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
    ) {
        createOrUpdateProfile(name, username, bio)
    }

    /**
     * Uploads an image to Firebase Storage.
     *
     * @param uri The URI of the image to be uploaded.
     * @param onSucess A callback function to be invoked on successful upload.
     */
    private fun uploadImage(uri: Uri, onSucess: (Uri) -> Unit) {
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
            .addOnFailureListener { exc ->
                handleException(exc, "Cannot upload image")
                inProgress.value = false
            }
    }

    /**
     * Uploads a profile image for the user.
     *
     * @param uri The URI of the profile image to be uploaded.
     */
    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
            updatePostUserImageData(it.toString())
        }
    }
    /**
     * Uploads a profile image for every single post.
     *
     * @param imageUrl The image URL of the profile image to be uploaded.
     */
    private fun updatePostUserImageData(imageUrl: String){
        val currentUid = auth.currentUser?.uid
        db.collection(POSTS).whereEqualTo("userId", currentUid).get()
            .addOnSuccessListener {
                val posts = mutableStateOf<List<PostData>>(arrayListOf())
                convertPosts(it, posts)
                val refs = arrayListOf<DocumentReference>()
                for(post in posts.value){
                    post.postId?.let { postId ->
                        refs.add(db.collection(POSTS).document(postId))
                    }
                }
                if(refs.isNotEmpty()){
                    db.runBatch{batch ->
                        for(ref in refs){
                            batch.update(ref, "userImage", imageUrl)
                        }
                    }
                        .addOnSuccessListener {
                            refreshPosts()
                        }
                }
            }
    }

    /**
     * Logs out the current user.
     */
    fun onLogout() {
        auth.signOut()
        signedIn.value = false
        userData.value = null
    }

    /**
     * Creates a new post with an uploaded image.
     *
     * @param uri The URI of the image to be uploaded.
     * @param description The description of the post.
     * @param onPostSuccess A callback function to be invoked on successful post creation.
     */
    fun onNewPost(uri: Uri, description: String, onPostSuccess: () -> Unit) {
        uploadImage(uri) {
            onCreatePost(it, description, onPostSuccess)
        }
    }

    /**
     * Creates a new post in the Firestore database.
     *
     * @param imageUri The URI of the uploaded image.
     * @param description The description of the post.
     * @param onPostSuccess A callback function to be invoked on successful post creation.
     */
    private fun onCreatePost(imageUri: Uri, description: String? = null, onPostSuccess: () -> Unit) {
        inProgress.value = true
        val currentUid = auth.currentUser?.uid
        val currentUsername = userData.value?.username
        val currentUserImageUrl = userData.value?.imageUrl.toString()

        if (currentUid.isNullOrEmpty()) {
            handleException(customMessage = "Cannot create post")
            onLogout()
            inProgress.value = false
        } else {
            val postUuid = UUID.randomUUID().toString()
            val post = PostData(
                postUuid,
                currentUid,
                currentUsername,
                currentUserImageUrl,
                imageUri.toString(),
                description,
                System.currentTimeMillis(),
                listOf<String>()
            )

            db.collection(POSTS).document(postUuid).set(post)
                .addOnSuccessListener {
                    snackbarMessage.value = Event("Post created successfully")
                    inProgress.value = false
                    refreshPosts()
                    onPostSuccess.invoke()
                }
                .addOnFailureListener {
                    handleException(it, "Cannot create post")
                }
        }
    }

    /**
     * Refreshes the list of posts for the current user.
     */
    private fun refreshPosts() {
        val currentUid = auth.currentUser?.uid
        if (currentUid.isNullOrEmpty()) {
            handleException(customMessage = "Not logged in, Cannot refresh posts")
            onLogout()
        } else {
            refreshPostsProgress.value = true
            db.collection(POSTS).whereEqualTo("userId", currentUid).get()
                .addOnSuccessListener { documents ->
                    convertPosts(documents, posts)
                    refreshPostsProgress.value = false
                }
                .addOnFailureListener { exc ->
                    handleException(exc, "Cannot fetch Posts")
                    refreshPostsProgress.value = false
                }
        }
    }

    /**
     * Converts the documents from Firestore into a list of PostData objects.
     *
     * @param documents The QuerySnapshot containing the documents.
     * @param outState The MutableState that will hold the list of PostData objects.
     */
    private fun convertPosts(documents: QuerySnapshot, outState: MutableState<List<PostData>>) {
        val newPosts = mutableListOf<PostData>()
        documents.forEach { doc ->
            val post = doc.toObject<PostData>()
            newPosts.add(post)
        }
        val sortedPosts = newPosts.sortedByDescending { it.time }
        outState.value = sortedPosts
    }
}
