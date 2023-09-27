package io.appwrite.tutorialforandroid

import IdeasService
import io.appwrite.Client
import io.appwrite.models.Document
import io.appwrite.models.Session
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
import io.appwrite.tutorialforandroid.pages.IdeasPage
import io.appwrite.tutorialforandroid.pages.LoginPage
import io.appwrite.tutorialforandroid.services.UserService
import io.appwrite.tutorialforandroid.ui.theme.TutorialForAndroidTheme
import kotlinx.coroutines.launch

private const val APPWRITE_ENDPOINT = "https://cloud.appwrite.io/v1"
private const val PROJECT_ID = "[PROJECT_ID]"

enum class Screen {
    User,
    Ideas
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val client = Client(applicationContext)
            .setEndpoint(APPWRITE_ENDPOINT)
            .setProject(PROJECT_ID)

        val userService = UserService(client)
        val ideasService = IdeasService(client)

        super.onCreate(savedInstanceState)
        setContent {
            TutorialForAndroidTheme {
                AppMainScreen(userService, ideasService)
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
private fun AppMainScreen(userService: UserService, ideasService: IdeasService) {
    var user = remember { mutableStateOf<Session?>(null) }
    var ideas = remember { mutableStateOf<List<Document<Map<String, Any>>>>(listOf()) }
    var screen = remember { mutableStateOf(Screen.Ideas) }

    Scaffold(bottomBar = { AppBottomBar(screen) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (screen.value) {
                Screen.User -> UserScreen(userService, user)
                else -> IdeasScreen(ideasService, ideas, user.value)
            }
        }
    }
}

@Composable
private fun UserScreen(userService: UserService, userState: MutableState<Session?>) {
    val coroutineScope = rememberCoroutineScope()

    LoginPage(
        user = userState.value,
        onLogin = { email, password ->
            coroutineScope.launch {
                userState.value = userService.login(email, password)
            }
        },
        onRegister = { email, password ->
            coroutineScope.launch {
                userState.value = userService.register(email, password)
            }
        },
        onLogout = {
            coroutineScope.launch {
                userService.logout()
                userState.value = null
            }
        }
    )
}

@Composable
private fun IdeasScreen(ideasService: IdeasService, ideasState: MutableState<List<Document<Map<String, Any>>>>, user: Session?) {
    val coroutineScope = rememberCoroutineScope()

    IdeasPage(
        ideas = ideasState.value,
        user = user,
        onSubmit = { title, description ->
            coroutineScope.launch {
                val newIdea = ideasService.add(user?.id ?: return@launch, title, description)
                ideasState.value = ideasState.value.plus(newIdea)
            }
        },
        onRemoveIdea = { ideaId ->
            coroutineScope.launch {
                ideasService.remove(ideaId)
                ideasState.value = ideasState.value.filter { idea -> idea.id != ideaId }
            }
        }
    )
}
