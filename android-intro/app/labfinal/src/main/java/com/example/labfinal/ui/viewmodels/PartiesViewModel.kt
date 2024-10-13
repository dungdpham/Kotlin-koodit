package com.example.labfinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.labfinal.data.repository.MPRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Dung Pham 2217963 11.10.24
// Manages UI related data for the party list that would be used by corresponding composable. Fetches
// the distinct party names from repository asynchronously and exposes them as StateFlow
class PartiesViewModel(private val repository: MPRepository) : ViewModel() {
    private val _parties = MutableStateFlow<List<String>>(emptyList())
    val parties: StateFlow<List<String>> = _parties

    init {
        getParties()
    }

    private fun getParties() {
        viewModelScope.launch {
            repository.getParties().collect { partyList ->
                _parties.value = partyList.sorted()
            }
        }
    }
}