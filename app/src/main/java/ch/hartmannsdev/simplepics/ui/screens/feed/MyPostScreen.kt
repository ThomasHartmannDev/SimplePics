package ch.hartmannsdev.simplepics.ui.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationItem
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationMenu
import ch.hartmannsdev.simplepics.ui.components.ProfileImage
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.navigateTo

@Composable
fun MyPostScreen(navController: NavController, vm: SimplePicsViewModel) {
    val userData = vm.userData.value
    val isLoading = vm.inProgress.value

    Column(modifier = Modifier.fillMaxSize().padding(top = 10.dp)) {

        Column(modifier = Modifier.weight(1f)) {
            Row() {
                ProfileImage(userData?.imageUrl) {}

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