package ch.hartmannsdev.simplepics.ui.screens.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationItem
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationMenu
import ch.hartmannsdev.simplepics.ui.components.PostList
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.navigateTo
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * Composable function for the My Posts screen.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param vm The ViewModel for managing user posts and profile data.
 */
@Composable
fun MyPostScreen(navController: NavController, vm: SimplePicsViewModel) {
    val userData = vm.userData.value
    val isLoading = vm.inProgress.value
    val postLoading = vm.refreshPostsProgress.value
    val posts = vm.posts.value
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val followers = vm.followers.value

    // Function to get the position of the snackbar
    fun getSnackbarPosition(): Float {
        val insets = ViewCompat.getRootWindowInsets(view)
        val isKeyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) == true
        val keyboardHeight = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0

        return if (isKeyboardVisible) {
            with(density) { keyboardHeight.toDp().value }
        } else {
            0f
        }
    }

    // Launcher for selecting a new post image from gallery
    val newPostImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val encodedUri = Uri.encode(it.toString())
            val route = Router.NewPost.createRoute(encodedUri)
            navController.navigate(route)
        }
    }

    // Observe Snackbar messages
    val snackbarMessage = vm.snackbarMessage.value?.getContentOrNull()
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    // Launcher for selecting an image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val encodedUri = Uri.encode(it.toString())
            val route = Router.NewPost.createRoute(encodedUri)
            navController.navigate(route)
        }
    }

    // Launcher for taking a picture with the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            vm.cameraImageUri?.let { uri: Uri? ->
                uri?.let {
                    val encodedUri = Uri.encode(it.toString())
                    val route = Router.NewPost.createRoute(encodedUri)
                    navController.navigate(route)
                }
            }
        }
    }

    // Dialog for choosing the image source (Gallery or Camera)
    if (showDialog.value) {
        Dialog(onDismissRequest = {
            showDialog.value = false
        }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Choose Image Source",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                galleryLauncher.launch("image/*")
                                showDialog.value = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Gallery", color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                try {
                                    val uri = vm.createImageUri(context.contentResolver)
                                    vm.cameraImageUri = uri
                                    cameraLauncher.launch(uri)
                                    showDialog.value = false
                                } catch (e: Exception) {
                                    vm.handleException(
                                        null,
                                        "Can't open camera, Allow the camera on the permissions"
                                    )
                                    showDialog.value = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Camera", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }

    Scaffold (
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.MYPOSTS,
                navController = navController
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Box(modifier = Modifier.padding(bottom = getSnackbarPosition().dp)) {
                Snackbar(snackbarData = data)
            }
        } }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(modifier = Modifier.weight(1f)) {
                Row() {
                    ch.hartmannsdev.simplepics.ui.components.ProfileImage(userData?.imageUrl) {
                        showDialog.value = true
                    }

                    Text(
                        text = "${posts.size}\nPosts", modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${followers}\nFollowers", modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${userData?.following?.size ?: 0}\nFollowing", modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }

                Column(modifier = Modifier.padding(8.dp)) {
                    val usernameDisplay =
                        if (userData?.username == null) "" else "@${userData.username}"
                    Text(text = userData?.name ?: "", fontWeight = FontWeight.Bold)
                    Text(text = usernameDisplay)
                    Text(text = userData?.bio ?: "")
                }

                OutlinedButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = { navigateTo(navController, Router.Profile) },
                    colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(10)
                ) {
                    Text(text = "Edit Profile", color = Color.Black)
                }

                CommomDivider()

                PostList(
                    isContextLoading = isLoading,
                    postsLoading = postLoading,
                    posts = posts,
                    modifier = Modifier
                        .weight(1f)
                        .padding(1.dp)
                        .fillMaxWidth(),
                ) { post ->
                    val postDataJson = Uri.encode(Gson().toJson(post))
                    navController.navigate("singlepost/$postDataJson")
                }
            }
        }
        if (isLoading) {
            CommomProgressSpinner()
        }
    }
}
