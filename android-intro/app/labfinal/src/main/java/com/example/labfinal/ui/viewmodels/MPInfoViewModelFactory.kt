package com.example.labfinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.labfinal.data.repository.MPRepository

// Dung Pham 2217963 12.10.24
// Custom factory class to create MPInfoViewModel instance and inject required
// dependency MPRepository and "hetekaId". Needed because in the Composable, it's not possible to
// directly instantiate MPInfoViewModel(...), but only viewModel() that requires a
// no-argument constructor for viewModel instance.
class MPInfoViewModelFactory(
    private val repository: MPRepository,
    private val hetekaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MPInfoViewModel::class.java))
            return MPInfoViewModel(repository, hetekaId) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}