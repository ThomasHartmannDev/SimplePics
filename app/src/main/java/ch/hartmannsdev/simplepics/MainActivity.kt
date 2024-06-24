package ch.hartmannsdev.simplepics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.ui.screens.auth.ForgotPasswordScreen
import ch.hartmannsdev.simplepics.ui.screens.auth.LoginScreen
import ch.hartmannsdev.simplepics.ui.screens.auth.SignupScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.CommentScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.FeedScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.SearchScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.SinglePostScreen
import ch.hartmannsdev.simplepics.ui.screens.user.MyPostScreen
import ch.hartmannsdev.simplepics.ui.screens.user.NewPostScreen
import ch.hartmannsdev.simplepics.ui.screens.user.ProfileScreen
import ch.hartmannsdev.simplepics.ui.theme.SimplePicsTheme
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.NotificationMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the SimplePics application. This activity sets up the UI and navigation
 * for the app using Jetpack Compose and Hilt for dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display
        setContent {
            SimplePicsTheme {
                SimplePicsApp()
            }
        }
    }
}

/**
 * Main composable function for the SimplePics application. Sets up the navigation and theme.
 *
 * @param modifier Modifier for the composable.
 */
@Composable
fun SimplePicsApp(modifier: Modifier = Modifier) {
    val vm = hiltViewModel<SimplePicsViewModel>() // ViewModel for the app, injected by Hilt
    val navController = rememberNavController() // Controller for navigation
    NotificationMessage(vm = vm) // Displays notifications

    // Sets up the navigation graph
    NavHost(navController = navController, startDestination = Router.Login.route) {
        composable(Router.Signup.route){ SignupScreen(navController = navController, vm = vm) }
        composable(Router.Login.route){ LoginScreen(navController = navController, vm = vm) }
        composable(Router.ForgotPassword.route){ ForgotPasswordScreen(navController = navController, vm = vm) }
        composable(Router.Feed.route){ FeedScreen(navController = navController, vm = vm) }
        composable(Router.Search.route){ SearchScreen(navController = navController, vm = vm) }
        composable(Router.MyPosts.route){ MyPostScreen(navController = navController, vm = vm) }
        composable(Router.Profile.route){ ProfileScreen(navController = navController, vm = vm) }
        composable(Router.NewPost.route){navBackStackEntry ->
            val imageUri = navBackStackEntry.arguments?.getString("imageUri")
            imageUri?.let {
                NewPostScreen(navController = navController, vm = vm,  encodedUri = it)
            }
        }
        composable(
            route = Router.SinglePost.route,
            arguments = listOf(navArgument("postData") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val postDataJson = navBackStackEntry.arguments?.getString("postData")
            val postData = Gson().fromJson(postDataJson, PostData::class.java)
            postData?.let {
                SinglePostScreen(
                    vm = vm, navController = navController, post = it
                )
            }
        }

        composable(Router.CommentsScreen.route){navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getString("postId")
            postId?.let {
                CommentScreen(navController = navController, vm = vm, postId = it)
            }
        }

    }
}

/**
 * Preview composable function for the SimplePicsApp.
 * This function is used to preview the app's UI in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimplePicsTheme {
        SimplePicsApp()
    }
}
