package ch.hartmannsdev.simplepics.ui.screens.user

import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationItem
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationMenu
import ch.hartmannsdev.simplepics.ui.components.ProfileImage
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.navigateTo
import kotlinx.coroutines.launch

@Composable
fun MyPostScreen(navController: NavController, vm: SimplePicsViewModel) {
    val userData = vm.userData.value
    val isLoading = vm.inProgress.value
    val showDialog = remember { mutableStateOf(false) }
    val ErrorCam = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val newPostImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){
        uri: Uri? ->
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

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val encodedUri = Uri.encode(it.toString())
            val route = Router.NewPost.createRoute(encodedUri)
            navController.navigate(route)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            // CHANGE HERE TO USE CAMERA IMAGE
            vm.cameraImageUri?.let { uri: Uri? ->
                uri?.let {
                    val encodedUri = Uri.encode(it.toString())
                    val route = Router.NewPost.createRoute(encodedUri)
                    navController.navigate(route)
                }
            }
        }
    }

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
                                try{
                                    val uri = vm.createImageUri(context.contentResolver)
                                    vm.cameraImageUri = uri
                                    cameraLauncher.launch(uri)
                                    showDialog.value = false
                                } catch (e: Exception) {
                                    vm.handleException(null,"Can't open camera, Allow the camera on the permissions")
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

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row() {
                ProfileImage(userData?.imageUrl) {
                    showDialog.value = true
                }

                Text(
                    text = "15\nPosts", modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "15\nFollowers", modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "15\nFollowing", modifier = Modifier
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

            OutlinedButton(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
                onClick = { navigateTo(navController, Router.Profile) },
                colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp),
                shape = RoundedCornerShape(10)
                ) {
                Text(text = "Edit Profile", color = Color.Black)
            }
            Column(modifier = Modifier.weight(1f)){
                Text("Post List")
            }

        }
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.MYPOSTS,
            navController = navController
        )
    }
    if (isLoading) {
        CommomProgressSpinner()
    }

}