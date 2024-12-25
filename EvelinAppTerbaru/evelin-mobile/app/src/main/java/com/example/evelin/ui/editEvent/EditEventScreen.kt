package com.example.evelin.ui.editEvent

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import com.example.evelin.ui.addEvent.InputField
import com.example.evelin.ui.component.utils.DatePickerField
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen
import kotlinx.coroutines.launch

@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: String,
    viewModel: EditEventViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    // State for editable fields
    var tanggalWaktu by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Fetch event details when the screen loads
    LaunchedEffect(eventId) {
        viewModel.fetchEventDetails(eventId)
    }

    // Observe event details from ViewModel
    val eventDetails by viewModel.eventDetails.observeAsState()

    // Update local state when event details are fetched
    LaunchedEffect(eventDetails) {
        eventDetails?.let { event ->
            tanggalWaktu = event.eventDate.toString()
            lokasi = event.location.toString()
        }
    }

    // Observe update result
    val updateEventResult by viewModel.updateEventResult.observeAsState()

    // Handle update result
    LaunchedEffect(updateEventResult) {
        if (updateEventResult == true) {
            Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            IconButton(onClick = { navController.navigateUp() }) {
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
            text = "Edit Event",
            fontSize = 28.sp,
            color = Green,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Event Details Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Read-only Fields
                DetailRow(
                    label = "Judul Acara",
                    value = eventDetails?.title ?: "",
                    isEditable = false
                )
                DetailRow(
                    label = "Deskripsi Acara",
                    value = eventDetails?.description ?: "",
                    isEditable = false
                )

                // Editable Fields
                EditableDetailRow(
                    label = "Tanggal & Waktu",
                    value = tanggalWaktu,
                    onValueChange = { tanggalWaktu = it }
                )
                EditableDetailRow(
                    label = "Lokasi",
                    value = lokasi,
                    onValueChange = { lokasi = it }
                )

                // Read-only Field
                DetailRow(
                    label = "Kategori Acara",
                    value = eventDetails?.category ?: "",
                    isEditable = false
                )


            }
        }

        // Event Poster Section
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Event Poster",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )

        // Poster Image Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(LightGreen, RoundedCornerShape(8.dp))
        ) {
            eventDetails?.posterUrl?.let { posterUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = posterUrl),
                    contentDescription = "Event Poster",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Update Button
        Button(
            onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.updateEvent(
                        eventId = eventId,
                        eventDate = tanggalWaktu,
                        location = lokasi,
                        onSuccess = { /* No-op */ },
                        onError = { e ->
                            Toast.makeText(context, "Error updating event: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
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
                text = "Update",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isEditable: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        if (isEditable) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Green,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun EditableDetailRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Green,
                modifier = Modifier.size(24.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Green, RoundedCornerShape(8.dp))
                .background(LightGreen, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}