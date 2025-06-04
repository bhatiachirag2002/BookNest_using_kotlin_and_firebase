package com.bhatia.booknest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhatia.booknest.model.Books
import com.bhatia.booknest.model.User
import com.bhatia.booknest.repo.AppRepo

import kotlinx.coroutines.launch

class AppViewModel(private val appRepo: AppRepo) : ViewModel() {
    private val _books = MutableLiveData<List<Books>>()
    val books: LiveData<List<Books>> = _books
    private val _authResult = MutableLiveData<Result<User?>>()
    val authResult: LiveData<Result<User?>> = _authResult
    private val _favouriteBooks = MutableLiveData<List<String>>()
    val favouriteBooks: LiveData<List<String>> = _favouriteBooks
    private val _resetPasswordResult = MutableLiveData<Result<String>>()
    val resetPasswordResult: LiveData<Result<String>> = _resetPasswordResult

    fun getBooks() {
        viewModelScope.launch {
            _books.value = appRepo.getBooks()
        }
    }

    fun downloadPdf(bookId: String, url: String, onResult: (ByteArray?) -> Unit) {
        viewModelScope.launch {
            val pdfBytes = appRepo.getOrDownloadPdf(bookId, url)
            onResult(pdfBytes)
        }
    }

    fun saveLastReadPage(pageNumber: Int) {
        appRepo.saveLastReadPage(pageNumber)
    }

    fun getLastReadPage(): Int {
        return appRepo.getLastReadPage()
    }

    fun addBookToFavourite(bookId: String) {
        viewModelScope.launch {
            appRepo.addBookToFavourite(bookId)
        }
    }

    fun getFavouriteBooks() {
        viewModelScope.launch {
            _favouriteBooks.value = appRepo.getFavouriteBooks()
        }
    }

    fun deleteBookFromFavourite(bookId: String) {
        viewModelScope.launch {
            appRepo.deleteBookFromFavourite(bookId)
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = appRepo.signup(email, password)
            _authResult.value = result
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = appRepo.login(email, password)
            _authResult.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = appRepo.logout()
            _authResult.value = result
        }
    }

    fun forgetPassword(email: String) {
        viewModelScope.launch {
            val forget = appRepo.forgetPassword(email)
            _resetPasswordResult.value = forget
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            val change = appRepo.changePassword(oldPassword, newPassword)
            _resetPasswordResult.value = change
        }
    }
}