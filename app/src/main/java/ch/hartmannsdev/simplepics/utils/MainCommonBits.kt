package ch.hartmannsdev.simplepics.utils

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.theme.Orange80
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel




//composable with Toast in case the snackbar is not working


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
    Row (
        modifier = Modifier
            .alpha(0.5f)
            .background(Orange80)
            .clickable(enabled = false) { }
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        CircularProgressIndicator()
    }
}

fun navigateTo(navControler: NavController, dest: Router) {
    navControler.navigate(dest.route){
        popUpTo(dest.route)
        launchSingleTop = true

    }
}

@Composable
fun CheckSignedIn(navControler: NavController, vm: SimplePicsViewModel) {
    val alreadyLoggedIn = remember { mutableStateOf(false) }
    val signedIn = vm.signedIn.value
    if (signedIn && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        navControler.navigate(Router.Feed.route) {
            // Pop all the screens from the backstack, so if the user presses the back button,
            // they will not be able to get back and will leave the app
            popUpTo(0)
        }
    }
}