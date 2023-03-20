package com.hrithik.stegoshield.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.hrithik.stegoshield.ui.components.HomeButton
import com.hrithik.stegoshield.ui.components.HomeButtonType

@Composable
fun Home(navController: NavHostController? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeButton(onClick = { navController?.navigate("encrypt") }, type = HomeButtonType.Encrypt)
        HomeButton(onClick = { navController?.navigate("decrypt") }, type = HomeButtonType.Decrypt)
    }
}

@Preview
@Composable
fun HomePreview() {
    Home()
}