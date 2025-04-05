package com.smd.u_journal.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopBarViewModel : ViewModel() {
    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded

    fun expand() {
        _isExpanded.value = true
    }

    fun collapse() {
        _isExpanded.value = false
    }

    fun toggle() {
        _isExpanded.value = !_isExpanded.value
    }
}