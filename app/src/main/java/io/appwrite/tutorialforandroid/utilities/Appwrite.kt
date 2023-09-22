import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases

val client = Client()
    .setEndpoint("https://cloud.appwrite.io/v1") // Your Appwrite Endpoint
    .setProject("[PROJECT_ID]")                // Your project ID

val account = Account(client)
val databases = Databases(client)