package ch.hartmannsdev.simplepics.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.hartmannsdev.simplepics.ui.theme.Green10

@Composable
fun CustomTextField(
    valueState: MutableState<String>,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val containerColor = Color(0xFFF5F5F5)
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
        singleLine = true,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            cursorColor = Color.Black,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
        )
    )
}

@Composable
fun NameTextField(nameState: MutableState<String>) {
    CustomTextField(
        valueState = nameState,
        label = "Name"
    )
}

@Composable
fun EmailTextField(emailState: MutableState<String>) {
    CustomTextField(
        valueState = emailState,
        label = "Email",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordTextField(passwordState: MutableState<String>, label: String = "Password") {
    var passwordVisible by remember { mutableStateOf(false) }

    CustomTextField(
        valueState = passwordState,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Text(
                text = if (passwordVisible) "Hide" else "Show",
                color = Green10,
                modifier = Modifier.clickable { passwordVisible = !passwordVisible }
            )
        }
    )
}


