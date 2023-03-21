package com.hrithik.stegoshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hrithik.stegoshield.ui.layout.Decrypt
import com.hrithik.stegoshield.ui.layout.Encrypt
import com.hrithik.stegoshield.ui.layout.Home
import com.hrithik.stegoshield.ui.theme.StegoShieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContentPreview()
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(navController)
            }
            composable("encrypt") {
                Encrypt()
            }
            composable("decrypt") {
                Decrypt()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    StegoShieldTheme {
        MainContent()
    }
}