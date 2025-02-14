package com.bhatia.booknest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val uid: String = "",
    val email: String = "",
    val favouriteBooks: List<String> = emptyList()
) : Parcelable