package com.example.labfinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labfinal.data.model.MPData
import com.example.labfinal.data.repository.MPRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Dung Pham 2217963 11.10.24
// Manages UI related data for the list of given party's MPs that would be used by corresponding
// composable. Fetches the list of MPs whose party matches given "party" parameter from
// repository asynchronously and exposes them as StateFlow
class PartyMembersViewModel(
    private val repository: MPRepository,
    private val party: String
) : ViewModel() {
    private val _partyMPs = MutableStateFlow<List<MPData>>(emptyList())
    val partyMPs: StateFlow<List<MPData>> = _partyMPs

    init {
        getMPsbyParty()
    }

    private fun getMPsbyParty() {
        viewModelScope.launch {
            repository.getMPsbyParty(party).collect { mps ->
                _partyMPs.value = mps.sortedBy { it.hetekaId }
            }
        }
    }
}