package com.hrithik.stegoshield.ui.layout

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Encrypt() {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )
    var requestPermission by remember { mutableStateOf(true) }
    val radioOptions = listOf("Image", "Audio", "Video", "Text")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("StegoShield") }) }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Choose an option where you want to embed your message:")
                radioOptions.forEach { text ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) })
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Button(onClick = {
                    requestPermission =
                        if (requestPermission && !permissionsState.allPermissionsGranted) {
                            permissionsState.launchMultiplePermissionRequest()
                            false
                        } else
                            false

                }) {
                    Text("Upload")
                }
                if (!requestPermission) {
                    UploadFromGallery()
                }
            }
        }
    }
}

@Composable
fun UploadFromGallery() {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    var bitmap: Bitmap
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    LaunchedEffect(Unit) {
        launcher.launch("image/*")
    }

    imageUri?.let {

        val source = ImageDecoder.createSource(context.contentResolver, it)
        bitmap = ImageDecoder.decodeBitmap(source)

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    }
}

@Preview
@Composable
fun EncryptPreview() {
    Encrypt()
}