package com.bhatia.booknest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "pdf_table")
@Parcelize
data class PdfEntity(
    @PrimaryKey val bookId: String,
    val filePath: String
):Parcelable

