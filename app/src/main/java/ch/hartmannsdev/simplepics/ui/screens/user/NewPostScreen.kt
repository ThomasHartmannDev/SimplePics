package ch.hartmannsdev.simplepics.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.Style
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.ui.theme.Typography
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomImage

@Composable
fun NewPostScreen(navController: NavController, vm: SimplePicsViewModel, encodedUri: String?) {

    val onDescriptionChange = remember { mutableStateOf(TextFieldValue()) }
    val scrollState = rememberScrollState()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Text(text = "Send", style = Typography.bodyLarge)
            }
            CommomDivider()
            
            CommomImage(data = encodedUri)

            CommomDivider()

            OutlinedTextField(
                value = "Description",
                onValueChange = { onDescriptionChange },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = false
            )
        }
    }
}