package com.bhatia.booknest.auth

import com.bhatia.booknest.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore


import kotlinx.coroutines.tasks.await


class FirebaseAuthentication {
    val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun signUp(email: String, password: String): Result<User?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(authResult.user?.uid ?: "", email)
            firestore.collection("Users").document(user.uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userDoc =
                firestore.collection("Users").document(authResult.user?.uid!!).get().await()
            val user = userDoc.toObject(User::class.java)
            Result.success(user)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("The email and password is incorrect."))
        } catch (e: Exception) {
            Result.failure(Exception("Login failed: ${e.message}"))
        }

    }

    suspend fun forgetPassword(email: String): Result<String> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success("")
        } catch (
            e: FirebaseAuthInvalidUserException
        ) {
            Result.failure(Exception("No account found with this email address."))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to send the reset link: ${e.message}"))
        }

    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<String> {
        return try {
            val user = auth.currentUser
            val credential = EmailAuthProvider.getCredential(user?.email!!, oldPassword)

            user.reauthenticate(credential).await()

            user.updatePassword(newPassword).await()
            Result.success("")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Old password is incorrect."))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to change password: ${e.message}"))
        }
    }
}