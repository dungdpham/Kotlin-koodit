package com.example.labfinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.labfinal.data.repository.MPRepository

// Dung Pham 2217963 11.10.24
// Custom factory class to create PartyMemberViewModel instance and inject required
// dependency MPRepository and party String. Needed because in the Composable, it's not possible to
// directly instantiate PartyMemberViewModel(...), but only viewModel() that requires a
// no-argument constructor for viewModel instance.
class PartyMembersViewModelFactory(
    private val repository: MPRepository,
    private val party: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartyMembersViewModel::class.java))
            return PartyMembersViewModel(repository, party) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}