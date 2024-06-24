package ch.hartmannsdev.simplepics.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.R
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.utils.navigateTo

/**
 * Enum class representing the items in the bottom navigation menu.
 *
 * @param icon The resource ID of the icon for the navigation item.
 * @param navRoute The route associated with the navigation item.
 */
enum class BottomNavigationItem(val icon: Int, val navRoute: Router) {
    FEED(R.drawable.ic_home, Router.Feed),
    SEARCH(R.drawable.ic_search, Router.Search),
    MYPOSTS(R.drawable.ic_posts, Router.MyPosts)
}

/**
 * Composable function to display a bottom navigation menu.
 *
 * @param selectedItem The currently selected item in the navigation menu.
 * @param navController The NavController for handling navigation.
 */
@Composable
fun BottomNavigationMenu(selectedItem: BottomNavigationItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp, bottom = 20.dp) // Adds padding to the top and bottom of the row
            .background(Color.White) // Sets the background color of the row
    ) {
        // Iterate through all items in the BottomNavigationItem enum
        for (item in BottomNavigationItem.values()) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp) // Sets the size of the image
                    .padding(5.dp) // Adds padding around the image
                    .weight(1f) // Ensures each item takes up equal space
                    .clickable {
                        navigateTo(navController, item.navRoute) // Navigates to the selected route
                    },
                colorFilter = if (item == selectedItem) ColorFilter.tint(Color.Black)
                else ColorFilter.tint(Color.Gray) // Tints the icon color based on selection
            )
        }
    }
}
