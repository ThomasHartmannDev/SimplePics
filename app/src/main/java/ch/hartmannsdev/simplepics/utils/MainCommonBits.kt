package ch.hartmannsdev.simplepics.utils

import android.os.Parcelable
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.R
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.theme.Orange80
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay


@Composable
fun NotificationMessage(vm: SimplePicsViewModel) {
    val notifState = vm.popupNotification.value
    val notifMessage = notifState?.getContentOrNull()
    if (notifMessage != null) {
        Toast.makeText(LocalContext.current, notifMessage, Toast.LENGTH_SHORT).show()
    }

}

@Composable
fun CommomProgressSpinner(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .alpha(0.5f)
            .background(Orange80)
            .clickable(enabled = false) { }
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
    }
}

data class NavParam(
    val name: String,
    val value: Parcelable
)
fun navigateTo(navControler: NavController, dest: Router, vararg params: NavParam) {
    for (param in params) {
        navControler.currentBackStackEntry?.arguments?.putParcelable(param.name, param.value)
    }
    navControler.navigate(dest.route) {
        popUpTo(dest.route)
        launchSingleTop = true

    }
}

@Composable
fun CheckSignedIn(navController: NavController, vm: SimplePicsViewModel) {
    val alreadyLoggedIn = remember { mutableStateOf(false) }
    val signedIn = vm.signedIn.value
    if (signedIn && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        navController.navigate(Router.Feed.route) {
            // Pop all the screens from the backstack, so if the user presses the back button,
            // they will not be able to get back and will leave the app
            popUpTo(0)
        }
    }
}


/*
* The CommomImage can be use to multiple thing, this composable is just
* a imagine loader using Coil.
* */
@Composable
fun CommomImage(
    data: String?,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.Crop,
) {
    val painter = rememberAsyncImagePainter(model = data)
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = "User Image",
        contentScale = contentScale
    )

    if (painter.state is AsyncImagePainter.State.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CommomProgressSpinner()
        }
    }
}

@Composable
fun UserImageCard(
    userImage: String?,
    modifier: Modifier = Modifier
        .padding(8.dp)
        .size(64.dp)
) {
    Card(shape = CircleShape, modifier = modifier) {
        Box(contentAlignment = Alignment.Center) {
            if (userImage.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Default User Image",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CommomImage(data = userImage)
            }
        }
    }
}

@Composable
fun CommomDivider() {
    HorizontalDivider(
        Modifier
            .alpha(0.3f)
            .padding(8.dp),
        thickness = 1.dp,
        color = Color.LightGray,
    )
}

private enum class LikeIconSize {
    SMALL,
    LARGE
}

@Composable
fun LikeAnimation(like: Boolean = true) {
    var sizeState by remember { mutableStateOf(LikeIconSize.SMALL) }
    val transition = updateTransition(targetState = sizeState, label = "likeAnimation")
    val size by transition.animateDp(
        label = "sizeAnimation",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }
    ) { state ->
        when (state) {
            LikeIconSize.SMALL -> 0.dp
            LikeIconSize.LARGE -> 150.dp
        }
    }

    Image(
        painter = painterResource(id = if (like) R.drawable.ic_like else R.drawable.ic_dislike),
        contentDescription = null,
        modifier = Modifier.size(size = size),
        colorFilter = ColorFilter.tint(if (like) Color.Red else Color.Gray)
    )

    LaunchedEffect(Unit) {
        sizeState = LikeIconSize.LARGE
        delay(1000)
        sizeState = LikeIconSize.SMALL
    }
}