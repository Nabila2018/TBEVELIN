@file:Suppress("DEPRECATION")

package com.example.evelin.ui.addEvent

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.evelin.ui.component.utils.DatePickerField
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Composable
fun AddEventScreen(navController: NavController,
                   viewModel: AddEventViewModel = viewModel(factory = ViewModelFactory.getInstance(
                       LocalContext.current))
) {
    var judulAcara by remember { mutableStateOf("") }
    var deskripsiAcara by remember { mutableStateOf("") }
    var tanggalWaktu by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategoriAcara by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val addEventResult by viewModel.addEventResult.observeAsState()

    LaunchedEffect(addEventResult) {
        if (addEventResult == true) {
            Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back navigation row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Input Event",
            fontSize = 28.sp,
            color = Green,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields
        InputField(label = "Judul Acara*", value = judulAcara) { judulAcara = it }
        MultilineInputField(
            label = "Deskripsi Acara*",
            value = deskripsiAcara
        ) { deskripsiAcara = it }
        DatePickerField(label = "Tanggal & Waktu*", selectedDate = tanggalWaktu) { tanggalWaktu = it }
        InputField(label = "Lokasi*", value = lokasi) { lokasi = it }
        InputField(label = "Kategori Acara*", value = kategoriAcara) { kategoriAcara = it }

        // Image Upload Section
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Upload Poster",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )

        // Image Preview and Upload Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(LightGreen, RoundedCornerShape(8.dp))
                .clickable { imagePickerLauncher.launch("image/*") }
        ) {
            if (imageUri != null) {
                // Show image preview
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Event Poster",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder when no image is selected
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = "Upload Icon",
                        tint = Green
                    )
                    Text(
                        text = "Tap to Upload Poster",
                        color = Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        fun Uri.getFilePathFromUri(context: Context): File? {
            return try {
                val inputStream = context.contentResolver.openInputStream(this)
                val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                tempFile.deleteOnExit()
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                tempFile
            } catch (e: Exception) {
                Log.e("FileUpload", "Error converting URI to File", e)
                null
            }
        }

        // Publish Button
        Button(
            onClick = {
                val posterPart = imageUri?.let { uri ->
                    val file = uri.getFilePathFromUri(context)
                    val requestBody = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("posterUrl", file?.name, requestBody!!)
                }

                viewModel.viewModelScope.launch {
                    viewModel.addEvent(
                        title = judulAcara.toRequestBody(),
                        description = deskripsiAcara.toRequestBody(),
                        eventDate = tanggalWaktu.toRequestBody(),
                        location = lokasi.toRequestBody(),
                        category = kategoriAcara.toRequestBody(),
                        posterUrl = posterPart!!,
                        onSuccess = {

                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        onError = { e -> Log.e("AddEvent", "Error adding event", e) }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            Text(
                text = "Publish",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun MultilineInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGreen, RoundedCornerShape(8.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Default // Mendukung baris baru
            ),
            maxLines = Int.MAX_VALUE, // Tidak ada batasan jumlah baris
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Green,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = LightGreen,
                unfocusedContainerColor = LightGreen
            )
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGreen, RoundedCornerShape(8.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Green,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = LightGreen,
                unfocusedContainerColor = LightGreen
            )
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun AddEventScreenPreview() {
    val navController = rememberNavController()
    AddEventScreen(navController = navController)
}