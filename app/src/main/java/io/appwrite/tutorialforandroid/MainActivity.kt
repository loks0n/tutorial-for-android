package io.appwrite.tutorialforandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.appwrite.tutorialforandroid.pages.HomePage
import io.appwrite.tutorialforandroid.pages.LoginPage
import io.appwrite.tutorialforandroid.ui.theme.TutorialforandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorialforandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

enum class Screen {
    User,
    Ideas
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent() {
    var user by remember { mutableStateOf<String?>(null) }
    var ideas by remember { mutableStateOf<List<String>>(listOf()) }
    var screen by remember { mutableStateOf(Screen.Ideas) }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { screen = Screen.Ideas }) {
                        Icon(Icons.Default.List, contentDescription = "Submissions")
                        Text("Submissions")
                    }
                    IconButton(onClick = { screen = Screen.User }) {
                        Icon(Icons.Default.Person, contentDescription = "User")
                        Text("User")
                    }
                }
            }
        }
    ) {padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (screen) {
                Screen.User -> LoginPage(
                    user,
                    onLogin = { username, password ->
                        if (password == "123") return@LoginPage;
                        user = username
                    },
                    onRegister = { username, password ->
                        if (password == "123") return@LoginPage;
                        user = username
                    },
                    onLogout = { user = null }
                )
                else -> HomePage(
                    ideas,
                    user,
                    onSubmit = { title, description ->
                        ideas = ideas.plus(title)
                    }
                )
            }
        }
    }
}

