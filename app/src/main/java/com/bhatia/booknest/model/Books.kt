package com.bhatia.booknest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Books(
    val name: String = "",
    val coverImage: String = "",
    val authorName: String = "",
    val category: String = "",
    val language: String = "",
    val description: String = "",
    val pdfLink: String = "",
    val show: Boolean = false,
) : Parcelable