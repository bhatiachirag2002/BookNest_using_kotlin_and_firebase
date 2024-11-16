package com.bhatia.booknest.repo

import android.content.Context
import com.bhatia.booknest.auth.FirebaseAuthentication
import com.bhatia.booknest.db.FirebaseSource
import com.bhatia.booknest.db.SharedPreferences
import com.bhatia.booknest.model.Books
import com.bhatia.booknest.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class AppRepo(
    private val firebaseSource: FirebaseSource,
    private val firebaseAuth: FirebaseAuthentication,
    context: Context
) {
    private val auth = firebaseAuth.auth.currentUser?.uid
    private val client = OkHttpClient()
    private val sharedPreferences = SharedPreferences(context)

    // Auth

    suspend fun signup(email: String, password: String): Result<User?> {
        return firebaseAuth.signUp(email, password)
    }

    suspend fun login(email: String, password: String): Result<User?> {
        return firebaseAuth.login(email, password)
    }

    suspend fun forgetPassword(email: String): Result<String> {
        return firebaseAuth.forgetPassword(email)
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<String> {
        return firebaseAuth.changePassword(oldPassword, newPassword)
    }

    //Books
    suspend fun getBooks(): List<Books> {
        return firebaseSource.getAllBooks()
    }

    suspend fun addBookToFavourite(bookId: String): Result<Unit> {
        val userId = auth ?: return Result.failure(Exception("User not logged in"))
        return firebaseSource.addBookToFavourite(userId, bookId)
    }

    suspend fun getFavouriteBooks(): List<String> {
        val userId = auth ?: return emptyList()
        return firebaseSource.getFavouriteBooks(userId)
    }

    suspend fun deleteBookFromFavourite(bookId: String): Result<Unit> {
        val userId = auth ?: return Result.failure(Exception("User not logged in"))
        return firebaseSource.deleteBookFromFavourite(userId, bookId)
    }

    //PDF
    suspend fun downloadPdf(url: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            return@withContext if (response.isSuccessful) {
                response.body?.bytes()
            } else {
                null
            }
        }
    }

    fun saveLastReadPage(pageNumber: Int) {
        sharedPreferences.saveLastReadPage(pageNumber)
    }

    fun getLastReadPage(): Int {
        return sharedPreferences.getLastReadPage()
    }


}

