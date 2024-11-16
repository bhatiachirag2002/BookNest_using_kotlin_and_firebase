package com.bhatia.booknest.db

import android.util.Log
import com.bhatia.booknest.model.Books
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseSource {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllBooks(): List<Books> {
        val bookList = mutableListOf<Books>()
        try {
            val snapshot = firestore.collection("Books").get().await()
            for (document in snapshot.documents) {
                val book = document.toObject(Books::class.java)
                book?.let { bookList.add(it)}
            }
            Log.d("Firebase", "Firebase data fetched successfully: ${bookList.size} books")
        } catch (e: Exception) {
            Log.e("Firebase", "Error fetching books from Firebase", e)
        }
        return bookList
    }



    suspend fun getFavouriteBooks(userId: String): List<String> {
        return try {
            val document = firestore.collection("Users").document(userId).get().await()
            val purchasedBooks = document.get("favouriteBooks") as? List<String> ?: emptyList()
            purchasedBooks
        }
        catch (e: Exception) {
            Log.e("Firebase", "Error fetching purchased books", e)
            emptyList()
        }
    }

    suspend fun deleteBookFromFavourite(userId: String, bookId: String): Result<Unit> {
        return try {
            firestore.collection("Users").document(userId)
                .update("favouriteBooks", FieldValue.arrayRemove(bookId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("Firebase", "Error deleting book from favourites", e)
            Result.failure(e)
        }
    }


    suspend fun addBookToFavourite(userId: String, bookId: String):Result<Unit>{
        return  try {
            firestore.collection("Users").document(userId)
                .update("favouriteBooks", FieldValue.arrayUnion(bookId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
