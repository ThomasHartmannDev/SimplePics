package ch.hartmannsdev.simplepics.ui.screens.feed

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.data.CommentData
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding

/**
 * Composable function for the comment screen.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param vm The ViewModel for managing comments.
 * @param postId The ID of the post to which comments belong.
 */
@Composable
fun CommentScreen(navController: NavController, vm: SimplePicsViewModel, postId: String) {
    var commentText by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val comments = vm.comments.value
    val commentsProgress = vm.commentsProgress.value

    ProvideWindowInsets {
        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .navigationBarsWithImePadding()
                ) {
                    // Text field for entering comments
                    TextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.LightGray),
                    )
                    // Button to submit the comment
                    Button(
                        onClick = {
                            vm.createComment(postId = postId, text = commentText)
                            commentText = ""
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Comment")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Back button
                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                // Display progress spinner while loading comments
                if (commentsProgress) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CommomProgressSpinner()
                    }
                } else if (comments.isEmpty()) {
                    // Display message if there are no comments
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No comments available")
                    }
                } else {
                    // Display list of comments
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(items = comments) { comment ->
                            CommentRow(comment)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying a single comment row.
 *
 * @param comment The comment data to display.
 */
@Composable
fun CommentRow(comment: CommentData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Display username in bold
        Text(text = comment.username ?: "", fontWeight = FontWeight.Bold)
        // Display comment text
        Text(text = comment.text ?: "", modifier = Modifier.padding(start = 8.dp))
    }
}
