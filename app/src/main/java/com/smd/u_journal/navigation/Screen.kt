package com.smd.u_journal.navigation

import androidx.annotation.DrawableRes
import com.smd.u_journal.R

sealed class Screen(
    val route: String,
    @DrawableRes val iconRes: Int = 0,
    val title: String = ""
) {
    // AUTH
    object Onboarding : Screen("onboarding")

    // MAIN
    object Main : Screen("main")
    object Home : Screen("home", R.drawable.home, "Home")
    object Date : Screen("date", R.drawable.date, "Date")
    object Media : Screen("media", R.drawable.media, "Media")
    object Atlas : Screen("atlas", R.drawable.atlas, "Atlas")

    // ENTRY
    object NewEntry : Screen("new_entry")
    object EntryDetail : Screen("entry_details/{entryId}") {  // Plural + entryId
        fun createRoute(entryId: String) = "entry_details/$entryId"
    }
    object Edit : Screen("edit/{entryId}") {
        fun createRoute(entryId: String) = "edit/$entryId"
    }
    object AddImage : Screen("add_image", R.drawable.add_image, "Add Image")
    object AddLocation : Screen("add_location", R.drawable.add_location, "Add Location")
}
