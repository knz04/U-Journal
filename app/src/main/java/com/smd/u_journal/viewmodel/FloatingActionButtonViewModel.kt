package com.smd.u_journal.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class FabState {
    ADD, EDIT
}

class FloatingActionButtonViewModel : ViewModel() {

    private val _fabState = MutableStateFlow(FabState.ADD)
    val fabState: StateFlow<FabState> = _fabState

    fun setFabState(state: FabState) {
        _fabState.value = state
    }

    fun performFabAction() {
        when (_fabState.value) {
            FabState.ADD -> {
                // Handle the action for adding a new journal entry
            }
            FabState.EDIT -> {
                // Handle the action for editing the current journal entry
            }
        }
    }
}
