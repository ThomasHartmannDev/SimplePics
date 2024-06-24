package ch.hartmannsdev.simplepics.ui.screens.user

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.data.PostRow
import ch.hartmannsdev.simplepics.ui.theme.Typography
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomImage
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import coil.compose.rememberImagePainter

/**
 * Composable function for the new post screen.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param vm The ViewModel for managing new post data.
 * @param encodedUri The encoded URI of the image to be posted.
 */
@Composable
fun NewPostScreen(navController: NavController, vm: SimplePicsViewModel, encodedUri: String?) {

    // State to manage the description of the post
    val onDescriptionChange = remember { mutableStateOf(TextFieldValue()) }
    // State to manage the image URI
    val imageUri by remember { mutableStateOf(encodedUri) }
    // State to manage scroll position
    val scrollState = rememberScrollState()
    // Manages the focus of the input fields
    val focusManager = LocalFocusManager.current
    // Checks if there is an operation in progress
    val inProgress = vm.inProgress.value

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)

        ) {
            // Row for back button and post button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                // Post button
                Text(text = "Post",
                    style = Typography.bodyLarge,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            focusManager.clearFocus()
                            vm.onNewPost(Uri.parse(imageUri), onDescriptionChange.value.text) {
                                navController.popBackStack()
                            }
                        })
            }
            CommomDivider()

            // Image to be posted
            Image(
                painter = rememberImagePainter(data = imageUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 240.dp)
            )

            CommomDivider()

            // Text field for post description
            Row(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
                OutlinedTextField(
                    value = onDescriptionChange.value,
                    onValueChange = { onDescriptionChange.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = { Text("Description") },
                    singleLine = false,
                )
            }
        }
    }
    // Show progress spinner if an operation is in progress
    if (inProgress) {
        CommomProgressSpinner()
    }
}
