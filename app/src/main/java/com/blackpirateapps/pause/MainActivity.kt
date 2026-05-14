package com.blackpirateapps.pause

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.blackpirateapps.pause.ui.navigation.PauseApp
import com.blackpirateapps.pause.ui.theme.PauseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        val repository = (application as PauseApplication).repository

        setContent {
            PauseTheme {
                PauseApp(repository = repository)
            }
        }
    }
}
