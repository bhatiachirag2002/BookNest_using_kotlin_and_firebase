package com.bhatia.booknest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bhatia.booknest.model.PdfEntity

@Dao
interface PdfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdf: PdfEntity)

    @Query("SELECT * FROM pdf_table WHERE bookId = :bookId LIMIT 1")
    suspend fun getPdfByBookId(bookId: String): PdfEntity?
}
