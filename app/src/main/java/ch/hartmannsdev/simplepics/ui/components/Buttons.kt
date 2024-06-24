package ch.hartmannsdev.simplepics.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hartmannsdev.simplepics.ui.theme.Orange05
import ch.hartmannsdev.simplepics.ui.theme.Orange20
import ch.hartmannsdev.simplepics.ui.theme.Purple40

/**
 * Composable function to display a filled button with custom colors and text.
 *
 * @param text The text to be displayed on the button.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun FilledButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp), // Adds padding to the start and end of the button
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange20, // Sets the background color of the button
            contentColor = Color.White // Sets the text color of the button
        ),
        onClick = onClick // The action to be performed when the button is clicked
    ) {
        Text(text = text) // The text displayed on the button
    }
}
