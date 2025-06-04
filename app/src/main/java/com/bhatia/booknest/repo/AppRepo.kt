package com.bhatia.booknest.repo

import android.content.Context
import com.bhatia.booknest.api.ApiInterface
import com.bhatia.booknest.api.ApiUtilites
import com.bhatia.booknest.auth.FirebaseAuthentication
import com.bhatia.booknest.db.FirebaseSource
import com.bhatia.booknest.db.SharedPreferences
import com.bhatia.booknest.model.Books
import com.bhatia.booknest.model.PdfEntity
import com.bhatia.booknest.model.User
import com.example.budgettracker.db.AppDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AppRepo(
    private val firebaseSource: FirebaseSource,
    private val firebaseAuth: FirebaseAuthentication,
    db: AppDB,
    private val context: Context
) {
    private val auth = firebaseAuth.auth.currentUser?.uid
    private val apiInterface = ApiUtilites.getInstance().create(ApiInterface::class.java)
    private val sharedPreferences = SharedPreferences(context)
    private val pdfDao = db.getPdfDao()

    // Auth

    suspend fun signup(email: String, password: String): Result<User?> {
        return firebaseAuth.signUp(email, password)
    }

    suspend fun login(email: String, password: String): Result<User?> {
        return firebaseAuth.login(email, password)
    }

     fun logout(): Result<User?> {
        return firebaseAuth.logout()
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
    suspend fun getOrDownloadPdf(bookId: String, url: String): ByteArray? {
        val existing = getPdfByBookId(bookId)
        return if (existing != null) {
            val file = File(existing.filePath)
            if (file.exists()) file.readBytes() else downloadAndSavePdf(bookId, url)
        } else {
            downloadAndSavePdf(bookId, url)
        }
    }


    private suspend fun getPdfByBookId(bookId: String) = pdfDao.getPdfByBookId(bookId)

    private suspend fun insertPdf(pdf: PdfEntity) = pdfDao.insertPdf(pdf)

    private suspend fun downloadAndSavePdf(bookId: String, url: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            val response = apiInterface.downloadPdfFile(url)
            if (response.isSuccessful) {
                val bytes = response.body()?.bytes()
                if (bytes != null) {
                    val fileName = "book_$bookId.pdf"
                    val file = File(context.filesDir, fileName)
                    file.writeBytes(bytes)

                    // Save PDF info in Room
                    insertPdf(PdfEntity(bookId, file.absolutePath))

                    return@withContext bytes
                }
            }
            null
        }
    }



    fun saveLastReadPage(pageNumber: Int) {
        sharedPreferences.saveLastReadPage(pageNumber)
    }

    fun getLastReadPage(): Int {
        return sharedPreferences.getLastReadPage()
    }


}

