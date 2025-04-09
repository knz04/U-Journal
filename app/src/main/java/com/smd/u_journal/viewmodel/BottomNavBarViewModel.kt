package com.smd.u_journal.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BottomNavBarViewModel : ViewModel() {

    enum class NavBarMode {
        MAIN,
        NEW_ENTRY,
        ENTRY_NAV
    }

    private val _navBarMode = MutableStateFlow(NavBarMode.MAIN)
    val navBarMode: StateFlow<NavBarMode> = _navBarMode

    fun switchToMain() {
        _navBarMode.value = NavBarMode.MAIN
    }

    fun switchToNewEntry() {
        _navBarMode.value = NavBarMode.NEW_ENTRY
    }

    fun switchToEntryNav() {
        _navBarMode.value = NavBarMode.ENTRY_NAV
    }
}


