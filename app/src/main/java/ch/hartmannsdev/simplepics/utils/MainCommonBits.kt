package ch.hartmannsdev.simplepics.utils

import android.widget.Toast

import androidx.compose.runtime.Composable

import androidx.compose.ui.platform.LocalContext
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

/*
@Composable
fun NotificationMessage(vm: SimplePicsViewModel) {
    val notifState = vm.popupNotification.value
    val notifMessage = notifState?.getContentOrNull()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (notifMessage != null) {
        LaunchedEffect(snackbarHostState) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = notifMessage,
                    actionLabel = "Dismiss"
                )
                //vm.consumeNotification()
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}
*/
