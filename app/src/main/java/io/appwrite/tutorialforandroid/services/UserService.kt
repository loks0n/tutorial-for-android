package io.appwrite.tutorialforandroid.services

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.User
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Session
import io.appwrite.services.Account

class UserService {
    private var account: Account

    constructor(client: Client) {
        this.account = Account(client)
    }
    
    suspend fun getLoggedIn(): User<Map<String, Any>>? {
        return try {
            account.get()
        } catch (e: AppwriteException) {
            null;
        }
    }

    suspend fun login(email: String, password: String): Session? {
        return try {
            account.createEmailSession(email, password)
        } catch (e: AppwriteException) {
            null
        }
    }

    suspend fun register(email: String, password: String): Session? {
        return try {
            account.create(ID.unique(), email, password)
            login(email, password)
        } catch (e: AppwriteException) {
            null
        }
    }

    suspend fun logout() {
        account.deleteSession("current")
    }
}