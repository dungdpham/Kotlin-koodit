package com.example.labfinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.labfinal.data.model.MPGrading
import kotlinx.coroutines.flow.Flow

// Dung Pham 2217963 10.10.24
// DAO interface for managing data access for MPGrading entity.
// Providing methods to insert grade with comment into, and get from, database.
@Dao
interface MPGradingDao {
    @Insert
    suspend fun insertGrading(grading: MPGrading)

    @Query("select * from mp_grading where hetekaId = :hetekaId")
    fun getMPGrading(hetekaId: Int): Flow<List<MPGrading>>
}