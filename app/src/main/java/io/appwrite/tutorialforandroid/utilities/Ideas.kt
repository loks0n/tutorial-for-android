import io.appwrite.tutorialforandroid.utilities.App
import io.appwrite.ID
import io.appwrite.Query

val IDEAS_DATABASE_ID = "616c3b5d5f5e4"
val IDEAS_COLLECTION_ID = "616c3b5d5f5e5"

class Ideas {
    fun fetch() {
        databases.listDocuments(
            IDEAS_DATABASE_ID,
            IDEAS_COLLECTION_ID,
            listOf(Query.limit(10))
        )
    }

    fun add(userId: String, title: String, description: String) {
        databases.createDocument(
            IDEAS_DATABASE_ID,
            IDEAS_COLLECTION_ID,
            ID.unique(),
            hashMapOf(
                "userId" to userId,
                "title" to title,
                "description" to description
            )
        )
    }

    fun remove(id: String) {
        databases.deleteDocument(
            IDEAS_DATABASE_ID,
            IDEAS_COLLECTION_ID,
            id
        )
    }
}