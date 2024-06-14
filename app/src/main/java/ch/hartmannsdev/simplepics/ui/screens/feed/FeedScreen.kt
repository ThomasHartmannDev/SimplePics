package ch.hartmannsdev.simplepics.ui.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationItem
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationMenu
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel

@Composable
fun FeedScreen(navController: NavController, vm : SimplePicsViewModel) {

    Column(modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier.weight(1f)){
            Text("Feed Screen")
        }
        BottomNavigationMenu(selectedItem = BottomNavigationItem.FEED, navController = navController)
    }
}