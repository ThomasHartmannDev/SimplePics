package ch.hartmannsdev.simplepics.ui.screens.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.components.EditTextField
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomImage
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.navigateTo

@Composable
fun ProfileScreen(navController: NavController, vm: SimplePicsViewModel) {
    val isLoading = vm.inProgress.value
    if (isLoading) {
        CommomProgressSpinner()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var userName by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }

        ProfileContent(
            vm,
            name,
            userName,
            bio,
            onUserNameChange = { userName = it },
            onNameChange = { name = it },
            onBioChange = { bio = it },
            onSave = { vm.updateProfileData(name, userName, bio) },
            onBack = { navigateTo(navController, Router.MyPosts) },
            onLogout = {
                vm.onLogout()
                navController.navigate(Router.Login.route){
                    popUpTo(0)
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileContent(
    vm: SimplePicsViewModel,
    name: String,
    userName: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val imageUrl = vm.userData?.value?.imageUrl

   Scaffold {
       Column(
           modifier = Modifier
               .verticalScroll(scrollState)
               .padding(it)
               .padding(16.dp)
       ) {
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(8.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
           ) {
               Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
               Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
           }
           CommomDivider()

           //User Image
           ProfileImage(imageURL = imageUrl, vm)

           CommomDivider()

           Spacer(modifier = Modifier.height(16.dp))

           EditTextField(label = "Name", value = name, onValueChange = onNameChange)
           CommomDivider()
           EditTextField(label = "Username", value = userName, onValueChange = onUserNameChange)
           CommomDivider()
           EditTextField(label = "Bio", value = bio, onValueChange = onBioChange)
           CommomDivider()

           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(top = 16.dp, bottom = 16.dp),
               horizontalArrangement = Arrangement.Center
           ) {
               Text(text = "Logout", modifier = Modifier.clickable { onLogout.invoke() })
           }
           Box(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.ime))
       }
   }
}

@Composable
fun ProfileImage(imageURL: String?, vm: SimplePicsViewModel) {
    val isLoading = vm.inProgress.value
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { vm.uploadProfileImage(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            vm.cameraImageUri?.let { vm.uploadProfileImage(it) }
        }
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
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
                                val uri = vm.createImageUri(context.contentResolver)
                                vm.cameraImageUri = uri
                                cameraLauncher.launch(uri)
                                showDialog.value = false
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

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {

        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { showDialog.value = true },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp),
            ){
                CommomImage(data = imageURL)
            }
            Text(text = "Change profile picture")
        }


        if (isLoading) {
            CommomProgressSpinner()
        }

    }
}