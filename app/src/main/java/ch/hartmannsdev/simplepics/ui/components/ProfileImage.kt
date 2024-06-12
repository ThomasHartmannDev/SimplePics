package ch.hartmannsdev.simplepics.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ch.hartmannsdev.simplepics.R
import ch.hartmannsdev.simplepics.utils.UserImageCard

@Composable
fun ProfileImage(imageUrl: String?, onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(top = 16.dp)
        .clickable { onClick.invoke() }
    ) {
        UserImageCard(
            userImage = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
        )
        Card(shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_add),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Create a new post",
                modifier = Modifier.background(Color.Blue)
                )
        }
    }
}