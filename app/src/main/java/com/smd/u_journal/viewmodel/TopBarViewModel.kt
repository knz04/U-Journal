package com.smd.u_journal.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class TopBarState {
    COLLAPSED,
    EXPANDED,
    NEW_ENTRY,
    ENTRY_NAV,
    EDIT_ENTRY
}

class TopBarViewModel : ViewModel() {
    private val _topBarState = MutableStateFlow(TopBarState.COLLAPSED)
    val topBarState: StateFlow<TopBarState> = _topBarState

    fun setState(state: TopBarState) {
        _topBarState.value = state
    }

    fun collapse() {
        _topBarState.value = TopBarState.COLLAPSED
    }

    fun expand() {
        _topBarState.value = TopBarState.EXPANDED
    }

    fun entryScreen() {
        _topBarState.value = TopBarState.NEW_ENTRY
    }

    fun detailScreen() {
        _topBarState.value = TopBarState.ENTRY_NAV
    }

    fun editScreen() {
        _topBarState.value = TopBarState.EDIT_ENTRY
    }
}
