package com.example.labfinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.labfinal.data.model.MPData
import kotlinx.coroutines.flow.Flow

// Dung Pham 2217963 10.10.24
// DAO interface for managing data access for MPData entity, providing methods to interact with database
@Dao
interface MPDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMPs(mps: List<MPData>)

    @Query("select * from mp_data")
    fun getMPs(): Flow<List<MPData>>

    @Query("select distinct party from mp_data where party is not null")
    fun getDistinctParties(): Flow<List<String>>

    @Query("select * from mp_data where hetekaId = :hetekaId")
    fun getMPbyId(hetekaId: Int): Flow<MPData>

    @Query("select * from mp_data where party = :party")
    fun getMPsbyParty(party: String): Flow<List<MPData>>
}