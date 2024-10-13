package com.example.labfinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Dung Pham 2217963 10.10.24
// Extra MP data (twitter accounts, years born, and constituencies) for some MPs
@Entity(tableName = "mp_data_extra")
data class MPDataExtra(
    @PrimaryKey val hetekaId: Int,
    val twitter: String?,
    val bornYear: Int?,
    val constituency: String?
)