package com.smd.u_journal.navigation

import androidx.annotation.DrawableRes
import com.smd.u_journal.R

sealed class Screen(
    val route: String,
    @DrawableRes val iconRes: Int = 0, // Default value for non-nav items
    val title: String = "" // Default value for non-nav items
) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home", R.drawable.home, "Home")
    object Date : Screen("date", R.drawable.date, "Date")
    object Media : Screen("media", R.drawable.media, "Media")
    object Atlas : Screen("atlas", R.drawable.atlas, "Atlas")
    object NewEntry : Screen("new_entry") // No need for icon or title
}
