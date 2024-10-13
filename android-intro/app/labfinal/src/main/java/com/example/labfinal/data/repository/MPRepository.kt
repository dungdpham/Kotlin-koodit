package com.example.labfinal.data.repository

import android.util.Log
import com.example.labfinal.data.dao.MPDataDao
import com.example.labfinal.data.dao.MPDataExtraDao
import com.example.labfinal.data.dao.MPGradingDao
import com.example.labfinal.data.model.MPData
import com.example.labfinal.data.model.MPDataExtra
import com.example.labfinal.data.model.MPGrading
import com.example.labfinal.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Dung Pham 2217963 10.10.24
// Repository in the Data layer on top of Data source (database), providing data access for
// ViewModel from UI layer. Also include method to fetch data from remote data source through Retrofit
// API and insert to local database
class MPRepository(
    private val mpDataDao: MPDataDao,
    private val mpDataExtraDao: MPDataExtraDao,
    private val mpGradingDao: MPGradingDao
    ) {
    suspend fun fetchFromRemote() {
        withContext(Dispatchers.IO) {
            try {
                val dataMPs = RetrofitInstance.api.getMPsData()
                mpDataDao.insertMPs(dataMPs)

                val dataMPsExtra = RetrofitInstance.api.getMPsExtraData()
                mpDataExtraDao.insertMPDataExtras(dataMPsExtra)
            } catch (e: Exception) {
                Log.e("DBG", "Error fetching data from remote source: $e")
            }
        }
    }

    fun getMPs(): Flow<List<MPData>> = mpDataDao.getMPs()

    fun getParties(): Flow<List<String>> = mpDataDao.getDistinctParties()

    fun getMPbyId(hetekaId: Int): Flow<MPData> = mpDataDao.getMPbyId(hetekaId)

    fun getMPsbyParty(party: String): Flow<List<MPData>> = mpDataDao.getMPsbyParty(party)

    fun getMPExtraData(hetekaId: Int): Flow<MPDataExtra> = mpDataExtraDao.getMPDataExtra(hetekaId)

    fun getMPGrading(hetekaId: Int): Flow<List<MPGrading>> = mpGradingDao.getMPGrading(hetekaId)

    suspend fun addGrading(hetekaId: Int, grade: Int, comment: String) {
        withContext(Dispatchers.IO) {
            mpGradingDao.insertGrading(MPGrading(hetekaId = hetekaId, grade = grade, comment = comment))
        }
    }
}