package ch.hartmannsdev.simplepics.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.R
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomImage
import ch.hartmannsdev.simplepics.utils.LikeAnimation
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

@Composable
fun SinglePostScreen(navController: NavController, vm: SimplePicsViewModel, post: PostData) {

    val comments = vm.comments.value

    LaunchedEffect (key1 = Unit){
        vm.getComments(post.postId)
    }

    val scrollState = rememberScrollState()
    post.userId?.let {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(it)
                    .verticalScroll(state = scrollState)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                CommomDivider()

                SinglePostDisplay(navController, vm, post, comments.size)
            }
        }
    }
}

@Composable
fun SinglePostDisplay(navController: NavController, vm: SimplePicsViewModel, post: PostData, nbComments: Int = 0) {

    val userData = vm.userData.value
    val likeAnimation = remember { mutableStateOf(false) }
    val dislikeAnimation = remember { mutableStateOf(false) }
    val likes = remember { mutableStateOf(post.likes?.size ?: 0) }
    val userHasLiked = remember { mutableStateOf(post.likes?.contains(userData?.userId) == true) }

    // User infos
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
            Text(text = " ", modifier = Modifier.padding(8.dp))

            if (userData?.userId == post.userId) {
                // Current user post, Don't show anything
            } else if (userData?.following?.contains(post.userId) == true) {
                Text(
                    text = "Following", color = Color.Gray,
                    modifier = Modifier.clickable {
                        vm.onFollowClick(post.userId!!)
                    }
                )
            } else {
                Text(
                    text = "Follow", color = Color.Blue,
                    modifier = Modifier.clickable {
                        vm.onFollowClick(post.userId!!)
                    }
                )
            }
        }
    }
    CommomDivider()

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (userHasLiked.value) {
                            dislikeAnimation.value = true
                            likes.value -= 1
                        } else {
                            likeAnimation.value = true
                            likes.value += 1
                        }
                        userHasLiked.value = !userHasLiked.value
                        vm.onLikePost(post)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommomImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )

        if (likeAnimation.value) {
            LikeAnimation(like = true)
            LaunchedEffect(likeAnimation.value) {
                delay(1000)
                likeAnimation.value = false
            }
        }

        if (dislikeAnimation.value) {
            LikeAnimation(like = false)
            LaunchedEffect(dislikeAnimation.value) {
                delay(1000)
                dislikeAnimation.value = false
            }
        }
    }
    CommomDivider()
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = "Like",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Red),
        )
        Text(text = " ${likes.value} Likes", modifier = Modifier.padding(start = 0.dp))
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = post.username ?: "", fontWeight = FontWeight.Bold)
        Text(text = post.postDescription ?: "", modifier = Modifier.padding(start = 8.dp))
    }

    CommomDivider()
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = "View ${nbComments} Comments",
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable {
                    post.postId?.let {
                        navController.navigate(Router.CommentsScreen.createRoute(it))
                    }
                }
        )
    }
}