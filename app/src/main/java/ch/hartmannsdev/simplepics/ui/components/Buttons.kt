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
import ch.hartmannsdev.simplepics.ui.theme.Purple40


@Composable
fun FilledButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple40,
            contentColor = Color.White
        ),
        onClick = onClick
    ){
        Text(text = text)
    }
}


@Preview
@Composable
private fun Buttons() {
    FilledButton("Login") {}
}