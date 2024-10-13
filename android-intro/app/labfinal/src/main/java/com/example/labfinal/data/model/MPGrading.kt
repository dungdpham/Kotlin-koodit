package com.example.labfinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Dung Pham 2217963 10.10.24
// Entity for Room Database for grade and comment added by user.
// Each instance of MPData class corresponds to a row in table "mp_grading".
@Entity(tableName = "mp_grading")
data class MPGrading(
    @PrimaryKey(autoGenerate = true) val gradingId: Int = 0,
    val hetekaId: Int,
    val grade: Int,
    val comment: String
)