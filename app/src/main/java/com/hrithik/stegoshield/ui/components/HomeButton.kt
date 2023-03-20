package com.hrithik.stegoshield.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrithik.stegoshield.ui.theme.ButtonDecrypt
import com.hrithik.stegoshield.ui.theme.ButtonEncrypt

enum class HomeButtonType {
    Encrypt, Decrypt
}

@Composable
fun HomeButton(onClick: () -> Unit, type: HomeButtonType) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(100.dp)
            .width(200.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (type == HomeButtonType.Encrypt) ButtonEncrypt else ButtonDecrypt),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Text(text = type.toString(), fontSize = 25.sp, color = Color.White)
    }
}

@Preview
@Composable
fun HomeButtonPreview() {
    HomeButton(onClick = {}, type = HomeButtonType.Encrypt)
}