package ch.hartmannsdev.simplepics.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.CommomDivider
import ch.hartmannsdev.simplepics.utils.CommomProgressSpinner
import ch.hartmannsdev.simplepics.utils.navigateTo
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, vm: SimplePicsViewModel) {
    val isLoading = vm.inProgress.value
    if (isLoading) {
        CommomProgressSpinner()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var userName by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }

        ProfileContent(
            vm,
            name,
            userName,
            bio,
            onUserNameChange = { name = it },
            onNameChange = { userName = it },
            onBioChange = { bio = it },
            onSave = {},
            onBack = { navigateTo(navController, Router.MyPosts) },
            onLogout = { }
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    vm: SimplePicsViewModel,
    name: String,
    userName: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(8.dp, top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
        }
        CommomDivider()

        //User Image
        Column(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        ) {

        }

        CommomDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                colors = OutlinedTextFieldDefaults.colors(Color.Transparent)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = userName,
                onValueChange = onUserNameChange,
                colors = OutlinedTextFieldDefaults.colors(Color.Transparent)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Bio", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = bio,
                onValueChange = onBioChange,
                colors = OutlinedTextFieldDefaults.colors(Color.Transparent),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        
        ) {
            Text(text = "Logout", modifier = Modifier.clickable { onLogout.invoke() })
        }
    }

}