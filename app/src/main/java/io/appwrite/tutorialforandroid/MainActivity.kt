package io.appwrite.tutorialforandroid

import IdeasService
import io.appwrite.Client
import io.appwrite.models.Document
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.appwrite.models.User
import io.appwrite.tutorialforandroid.ui.screens.IdeasScreen
import io.appwrite.tutorialforandroid.ui.screens.UserScreen
import io.appwrite.tutorialforandroid.services.UserService
import io.appwrite.tutorialforandroid.ui.theme.TutorialForAndroidTheme

private const val APPWRITE_ENDPOINT = "https://cloud.appwrite.io/v1"
private const val PROJECT_ID = "650870066ca28da3d330"

enum class Screen {
    User,
    Ideas
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val client = Client(applicationContext)
            .setEndpoint(APPWRITE_ENDPOINT)
            .setProject(PROJECT_ID)

        val userService = UserService(client)
        val ideasService = IdeasService(client)

        super.onCreate(savedInstanceState)
        setContent {
            TutorialForAndroidTheme {
                AppContent(userService, ideasService)
            }
        }
    }
}

@Composable
private fun AppBottomBar(screen: MutableState<Screen>) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { screen.value = Screen.Ideas }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.List, contentDescription = "Ideas")
                    Text("Ideas")
                }
            }
            IconButton(onClick = { screen.value = Screen.User }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Person, contentDescription = "User")
                    Text("User")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppContent(userService: UserService, ideasService: IdeasService) {
    var user = remember { mutableStateOf<User<Map<String, Any>>?>(null) }
    var screen = remember { mutableStateOf(Screen.Ideas) }

    LaunchedEffect(screen) {
        user.value = userService.getLoggedIn()
    }

    Scaffold(bottomBar = { AppBottomBar(screen) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (screen.value) {
                Screen.User -> UserScreen(user, userService)
                else -> IdeasScreen(user.value, ideasService)
            }
        }
    }
}