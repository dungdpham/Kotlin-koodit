package com.example.labfinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labfinal.data.model.MPData
import com.example.labfinal.data.model.MPDataExtra
import com.example.labfinal.data.model.MPGrading
import com.example.labfinal.data.repository.MPRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Dung Pham 2217963 12.10.24
// Fetches and combines data from multiple tables from the database related to one MP specify
// by his/her "hetekaId". Standard data, extra data, gradings data (grades and comments)
// and calculated average are gathered into a single "MPDetailInfo" object for the UI.
// Also has method to add grading into the database's "mp_grading" table.
class MPInfoViewModel(
    private val repository: MPRepository,
    private val hetekaId: Int
) : ViewModel() {
    private val _mpDetailInfo = MutableStateFlow<MPDetailInfo?>(null)
    val mpDetailInfo: StateFlow<MPDetailInfo?> = _mpDetailInfo

    init {
        getMPDetailInfo()
    }

    private fun getMPDetailInfo() {
        viewModelScope.launch {
            val mpData = repository.getMPbyId(hetekaId)
            val mpDataExtra = repository.getMPExtraData(hetekaId)
            val mpGrading = repository.getMPGrading(hetekaId)

            // combine 3 data flows into a single flow, calculate average and create MPDetailInfo object
            combine(mpData, mpDataExtra, mpGrading) { data, dataExtra, gradings ->
                val average = gradings.map { it.grade }.average()
                MPDetailInfo(data, dataExtra, gradings, average)
            }.collect { detailInfo ->
                _mpDetailInfo.value = detailInfo
            }
        }
    }

    fun addGrading(grade: Int, comment: String) {
        viewModelScope.launch {
            repository.addGrading(hetekaId, grade, comment)
            getMPDetailInfo()
        }
    }
}

// Holds the combined data: standard data, extra data, gradings, and calculated average based
// on the grades from gradings data
data class MPDetailInfo(
    val mpData: MPData,
    val mpDataExtra: MPDataExtra?,
    val gradings: List<MPGrading>,
    val averageGrade: Double
)