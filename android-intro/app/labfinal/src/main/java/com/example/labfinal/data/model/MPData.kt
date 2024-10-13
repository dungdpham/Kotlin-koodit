package com.example.labfinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Dung Pham 2217963 10.10.24
// Entity for Room Database. Each instance of MPData class corresponds to a row in table "mp_data".
@Entity(tableName = "mp_data")
data class MPData(
    @PrimaryKey val hetekaId: Int,
    val seatNumber: Int?,
    val lastname: String?,
    val firstname: String?,
    val party: String?,
    val minister: Boolean?,
    val pictureUrl: String?
)