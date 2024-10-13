package com.example.labfinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.labfinal.data.model.MPDataExtra
import kotlinx.coroutines.flow.Flow

// Dung Pham 2217963 10.10.24
// DAO interface for managing data access for MPDataExtra entity
@Dao
interface MPDataExtraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMPDataExtras(mpDataExtras: List<MPDataExtra>)

    @Query("select * from mp_data_extra where hetekaId = :hetekaId")
    fun getMPDataExtra(hetekaId: Int): Flow<MPDataExtra>
}