package com.hrithik.stegoshield.ui.layout

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hrithik.stegoshield.R
import com.hrithik.stegoshield.data.MyData
import com.hrithik.stegoshield.data.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

var file: File? = null
var message: String = ""
var type: String = "image"

@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Encrypt() {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )
    var requestPermission by remember { mutableStateOf(true) }
    val radioOptions = listOf("image", "audio", "video", "text")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    var messageState by remember { mutableStateOf("") }
    val fileState = remember { mutableStateOf<File?>(null) }
    var submitState by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    submitState = messageState.isNotEmpty() && fileState.value != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("StegoShield") }) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .clickable(interactionSource = interactionSource, indication = null) {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    radioOptions.forEach { text ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    selectedOption = text
                                    type = selectedOption
                                }
                            )
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = {
                                    selectedOption = text
                                    type = selectedOption
                                })
                            Text(
                                text = text.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.US
                                    ) else it.toString()
                                },
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            requestPermission =
                                if (requestPermission && !permissionsState.allPermissionsGranted) {
                                    permissionsState.launchMultiplePermissionRequest()
                                    false
                                } else
                                    false

                        },
                        border = BorderStroke(1.dp, Color.DarkGray),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black,
                            backgroundColor = Color.LightGray
                        )
                    ) {
                        Image(
                            painterResource(R.drawable.upload_icon),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Upload", Modifier.padding(start = 10.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    if (!requestPermission) {
                        UploadFromPhone(context, selectedOption, fileState)
                    }
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = messageState,
                    onValueChange = { newText ->
                        messageState = newText
                        message = messageState
                    },
                    label = { Text("Message") },
                    placeholder = { Text("Enter the message you want to embed") }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                ) {
                    Button(
                        onClick = {
                            onSubmitClicked(context)
                        }, modifier = Modifier
                            .height(60.dp)
                            .width(140.dp)
                            .align(BottomCenter),
                        enabled = submitState
                    ) {
                        Text("SUBMIT")
                    }
                }
            }
        }
    }
}

@Composable
fun UploadFromPhone(context: Context, type: String, fileState: MutableState<File?>) {
    var fileUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fileUri = uri
    }

    LaunchedEffect(Unit) {
        launcher.launch("$type/*")
    }
    fileUri?.let {
        fileState.value = File(fileUri.toString())
        file = fileState.value
        Text(getFileName(fileUri!!, context))

    }
}

fun onSubmitClicked(context: Context) {

    if (file != null && message.isNotEmpty()) {
        val data = MyData(file!!, message, type)
        RetrofitApi.retrofitService.postData(data)?.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                if (response.message() == "OK") {
                    Toast.makeText(
                        context,
                        "File Successfully Uploaded",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Error ${response.code()}: ${response.body()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Toast.makeText(
                    context,
                    "FAILED: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}

fun getFileName(uri: Uri, context: Context): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                result = it.getString(idx)
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result!!.substring(cut + 1)
        }
    }
    return result as String
}

@Preview
@Composable
fun EncryptPreview() {
    Encrypt()
}