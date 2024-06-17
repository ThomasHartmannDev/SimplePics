package ch.hartmannsdev.simplepics.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.R
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun SinglePostScreen(navController: NavController, vm: SimplePicsViewModel, post: PostData) {
    val scrollState = rememberScrollState()
    post.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .verticalScroll(state = scrollState)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            CommomDivider()

            SinglePostDisplay(navController, vm, post)
        }
    }
}

@Composable
fun SinglePostDisplay(navController: NavController, vm: SimplePicsViewModel, post: PostData) {

    val userData = vm.userData.value
    //User infos
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = rememberAsyncImagePainter(model = post.userImage),
                        contentDescription = post.username,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Text(text = post.username ?: "")
            Text(text = " . ", modifier = Modifier.padding(8.dp))

            if (userData?.userId == post.userId) {
                // Current user post, Don't show anything
            } else {
                Text(text = "Follow", color = Color.Blue,
                    modifier = Modifier.clickable {
                        //Follow User
                    }
                )
            }
        }
    }
    CommomDivider()

    Box(modifier = Modifier) {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommomImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }
    CommomDivider()
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = "Like",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Text(text = " ${post.likes?.size ?: 0} Likes", modifier = Modifier.padding(start = 0.dp))
    }

    Column (modifier = Modifier.padding(8.dp)){
        Text(text = post.username ?: "", fontWeight = FontWeight.Bold)
        Text(text = post.postDescription ?: "", modifier = Modifier.padding(start = 8.dp))
    }

    CommomDivider()
    Row (modifier = Modifier.padding(8.dp)){
        Text(text = "View Comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
    }
    Row (modifier = Modifier.padding(8.dp)){
        Text(text = "View Comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
    }
     Row (modifier = Modifier.padding(8.dp)){
            Text(text = "View Comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
        }
     Row (modifier = Modifier.padding(8.dp)){
            Text(text = "View Comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
        }
     Row (modifier = Modifier.padding(8.dp)){
            Text(text = "View Comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
        }

}