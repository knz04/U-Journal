package com.smd.u_journal.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectedEntryViewModel : ViewModel() {
    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    fun setDate(date: String) {
        _selectedDate.value = date
    }
}
