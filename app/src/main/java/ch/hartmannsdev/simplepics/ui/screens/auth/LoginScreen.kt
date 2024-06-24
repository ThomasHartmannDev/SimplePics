package ch.hartmannsdev.simplepics.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.R
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.components.FilledButton
import ch.hartmannsdev.simplepics.ui.theme.Orange05
import ch.hartmannsdev.simplepics.ui.theme.PurpleDark02
import ch.hartmannsdev.simplepics.ui.theme.Typography
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CheckSignedIn
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.navigateTo
import kotlinx.coroutines.launch

/**
 * Composable function for the Login screen.
 *
 * @param vm The ViewModel for managing authentication.
 * @param navController The navigation controller for navigating between screens.
 */
@Composable
fun LoginScreen(vm: SimplePicsViewModel, navController: NavController) {
    // Before everything, check if the user is already logged in to avoid unnecessary login attempts
    CheckSignedIn(navController = navController, vm = vm)

    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val passwordVisible = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val density = LocalDensity.current
    val focus = LocalFocusManager.current
    val isLoading = vm.inProgress.value

    /**
     * Gets the position of the Snackbar based on the keyboard visibility.
     *
     * @return The position of the Snackbar.
     */
    fun getSnackbarPosition(): Float {
        val insets = ViewCompat.getRootWindowInsets(view)
        val isKeyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) == true
        val keyboardHeight = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0

        return if (isKeyboardVisible) {
            with(density) { keyboardHeight.toDp().value }
        } else {
            0f
        }
    }

    // Observe Snackbar messages
    val snackbarMessage = vm.snackbarMessage.value?.getContentOrNull()
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Box(modifier = Modifier.padding(bottom = getSnackbarPosition().dp)) {
                Snackbar(snackbarData = data)
            }
        } }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo image
            Image(
                painter = painterResource(id = R.drawable.simplepiclogo_name),
                contentDescription = "SimplePics Logo",
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )

            // Title text
            Text(
                text = "Login",
                style = Typography.headlineLarge + (TextStyle(fontWeight = FontWeight.Bold)),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email input field
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 24.dp, start = 24.dp),
                singleLine = true,
            )

            // Password input field
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        val icon = if (passwordVisible.value) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        val description = if (passwordVisible.value) "Hide password" else "Show password"
                        Icon(icon, contentDescription = description )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 24.dp, start = 24.dp),
                singleLine = true,
            )

            // Forgot Password link
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text("Forgot Password?",
                    style = Typography.bodyLarge + (TextStyle(fontWeight = FontWeight.Bold)),
                    color = Orange05,
                    modifier = Modifier.clickable { navigateTo(navController, Router.ForgotPassword) },
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            // Login button
            FilledButton(
                text = "Login",
                onClick = {
                    val email = emailState.value.text
                    val password = passwordState.value.text

                    when {
                        email.isEmpty() -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Email cannot be empty")
                            }
                        }
                        password.isEmpty() -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Password cannot be empty")
                            }
                        }
                        else -> {
                            focus.clearFocus(force = true)
                            vm.onLogin(email, password)
                        }
                    }
                }
            )

            // Navigation to Signup screen
            Text("Don't have an account? Sign up",
                color = Orange05,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navigateTo(navController, Router.Signup) }
            )

            // Space for bottom inset
            Box(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.ime))
        }
        if (isLoading) {
            CommomProgressSpinner()
        }
    }
}
