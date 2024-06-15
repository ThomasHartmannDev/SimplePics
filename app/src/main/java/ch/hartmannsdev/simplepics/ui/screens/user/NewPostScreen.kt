package ch.hartmannsdev.simplepics.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel

@Composable
fun NewPostScreen(navController: NavController, vm: SimplePicsViewModel, encodedUri: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "NEW POST SCREEN", style = MaterialTheme.typography.headlineLarge, color = Color.Black)
    }
}