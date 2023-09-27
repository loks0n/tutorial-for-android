import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import io.appwrite.services.Databases

const val IDEAS_DATABASE_ID = "616c3b5d5f5e4"
const val IDEAS_COLLECTION_ID = "616c3b5d5f5e5"

class IdeasService {
    private var databases: Databases

    constructor(client: Client) {
        this.databases = Databases(client)
    }

    suspend fun fetch() {
        databases.listDocuments(
            IDEAS_DATABASE_ID,
            IDEAS_COLLECTION_ID,
            listOf(Query.limit(10))
        )
    }

    suspend fun add(userId: String, title: String, description: String): Document<Map<String, Any>> {
        return databases.createDocument(
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

    suspend fun remove(id: String) {
        databases.deleteDocument(
            IDEAS_DATABASE_ID,
            IDEAS_COLLECTION_ID,
            id
        )
    }
}