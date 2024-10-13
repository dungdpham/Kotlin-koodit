package com.example.labfinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.labfinal.data.repository.MPRepository

// Dung Pham 2217963 11.10.24
// Custom factory class to create PartiesViewModel instance and inject required
// dependency MPRepository. Needed because in the Composable, it's not possible to directly instantiate
// PartiesViewModel(...), but only viewModel() that requires a no-argument constructor for viewModel
// instance
class PartiesViewModelFactory(private val repository: MPRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartiesViewModel::class.java))
            return PartiesViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}