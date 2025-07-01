package com.example.starfest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import com.example.starfest.ui.theme.StarfestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            StarfestTheme {
                EventScreen()
            }
        }
    }
}
