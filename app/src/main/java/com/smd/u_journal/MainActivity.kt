package com.smd.u_journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.screens.MainScreen
import com.smd.u_journal.ui.theme.UJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UJournalTheme { // Applies the app's theme
                val navController = rememberNavController() // Create a NavController instance

                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(navController = navController) // Pass it to MainScreen
                }
            }
        }
    }
}
