package io.appwrite.tutorialforandroid.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.appwrite.models.Document
import io.appwrite.models.Session

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeasPage(
    ideas: List<Document<Map<String, Any>>>,
    user: Session?,
    onSubmit: (title: String, description: String) -> Unit,
    onRemoveIdea: (ideaId: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hide the form if user is not logged in
        if (user != null) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Button(onClick = {
                onSubmit(title, description)
                title = ""
                description = ""
            }) {
                Text("Submit")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(ideas) { idea ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = idea.data["title"]?.toString() ?: "")
                    Text(text = idea.data["description"]?.toString() ?: "")
                    Button(onClick = { onRemoveIdea(idea.id) }) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}
