package com.np.apiaplikacija

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.np.apiaplikacija.navigation.AppNavigation
import com.np.apiaplikacija.ui.theme.ApiAplikacijaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiAplikacijaTheme {
                AppNavigation()
            }
        }
    }
}
