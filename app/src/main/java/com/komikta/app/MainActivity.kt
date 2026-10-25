package com.komikta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.komikta.app.ui.KomiktaApp
import com.komikta.app.ui.theme.KomiktaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.BLACK
        window.navigationBarColor = android.graphics.Color.BLACK
        setContent {
            KomiktaTheme {
                Surface(modifier = Modifier.fillMaxSize().background(Color(0xFF080808)), color = Color(0xFF080808)) {
                    KomiktaApp()
                }
            }
        }
    }
}
