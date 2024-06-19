package ch.hartmannsdev.simplepics.ui.screens.feed

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationItem
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationMenu
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomImage
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.LikeAnimation
import ch.hartmannsdev.simplepics.utils.UserImageCard
import com.google.gson.Gson
import kotlinx.coroutines.delay

@Composable
fun FeedScreen(navController: NavController, vm: SimplePicsViewModel) {
    val userDataLoading = vm.inProgress.value
    val userData = vm.userData.value
    val feed = vm.postsFeed.value
    val feedLoading = vm.postsFeedProgress.value

    Scaffold(
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.FEED,
                navController = navController
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserImageCard(userImage = userData?.imageUrl)
                Text(text = userData?.username ?: "", modifier = Modifier.padding(8.dp))
            }

            PostsList(
                modifier = Modifier.weight(1f),
                posts = feed,
                loading = feedLoading,
                navController = navController,
                vm = vm,
                currentUserId = userData?.userId ?: ""
            )
        }
    }
}

@Composable
fun PostsList(
    posts: List<PostData>,
    modifier: Modifier,
    loading: Boolean,
    navController: NavController,
    vm: SimplePicsViewModel,
    currentUserId: String
) {
    Box(modifier = modifier) {
        LazyColumn {
            items(items = posts) { post ->
                Post(
                    post = post,
                    currentUserId = currentUserId,
                    vm = vm,
                ) {
                    val postDataJson = Uri.encode(Gson().toJson(post))
                    navController.navigate("singlepost/$postDataJson")
                }
            }
        }
        if (loading) {
            CommomProgressSpinner()
        }
    }
}

@Composable
fun Post(
    post: PostData,
    currentUserId: String,
    vm: SimplePicsViewModel,
    onPostClick: () -> Unit
) {
    val likeAnimation = remember { mutableStateOf(false) }
    val dislikeAnimation = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(shape = CircleShape, modifier = Modifier
                    .padding(4.dp)
                    .size(32.dp)) {
                    CommomImage(data = post.userImage, contentScale = ContentScale.Crop)
                }
                Text(text = post.username ?: "", modifier = Modifier.padding(4.dp))
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 250.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (post.likes?.contains(currentUserId) == true) {
                                    dislikeAnimation.value = true
                                } else {
                                    likeAnimation.value = true
                                }
                                vm.onLikePost(post)
                            },
                            onTap = {
                                onPostClick.invoke()
                            }
                        )
                    }
                CommomImage(data = post.postImage, modifier = modifier, contentScale = ContentScale.FillWidth)

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
        }
    }
}