package com.example.mindfuel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mindfuel.navigation.AppNavHost
import com.example.mindfuel.ui.theme.MindFuelTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MindFuelTheme {
                AppNavHost()
            }
        }
    }
}