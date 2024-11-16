package com.bhatia.booknest.util

import android.util.Patterns

object Validator {

    fun emailValid(email:String): String?{
        return when{
            email.isBlank() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address"
            else -> null
        }
    }

    fun passwordValid(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must be at least 6 characters long"
            else -> null
        }
    }

    fun confirmPassword(password: String, confirmPassword:String):String?{
        return when{
            confirmPassword.isBlank() -> "Confirm password cannot be empty"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
    }
}