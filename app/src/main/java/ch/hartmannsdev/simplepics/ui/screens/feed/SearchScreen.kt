package ch.hartmannsdev.simplepics.ui.screens.feed

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationItem
import ch.hartmannsdev.simplepics.ui.components.BottomNavigationMenu
import ch.hartmannsdev.simplepics.ui.components.PostList
import ch.hartmannsdev.simplepics.ui.components.SearchBar
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import com.google.gson.Gson

/**
 * Composable function for the search screen.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param vm The ViewModel for managing search functionality.
 */
@Composable
fun SearchScreen(navController: NavController, vm: SimplePicsViewModel) {
    val searchedPostLoading = vm.searchedPostProgress.value
    val searchedPost = vm.searchedPosts.value
    var searchTerm by rememberSaveable { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.SEARCH,
                navController = navController
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Search bar for inputting search term
            SearchBar(
                searchTerm = searchTerm,
                onSearchChange = { searchTerm = it },
                onSearch = { vm.searchPosts(searchTerm) }
            )
            // Display the list of posts matching the search term
            PostList(
                isContextLoading = false,
                postsLoading = searchedPostLoading,
                posts = searchedPost,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp),
            ) { post ->
                val postDataJson = Uri.encode(Gson().toJson(post))
                navController.navigate("singlepost/$postDataJson")
            }
        }
    }
}
