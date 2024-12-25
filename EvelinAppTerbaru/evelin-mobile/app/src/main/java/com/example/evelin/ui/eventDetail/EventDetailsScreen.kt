package com.example.evelin.ui.eventDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen

@Composable
fun EventDetailsScreen(
    navController: NavController,
    eventId: String?,
    viewModel: EventDetailViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    LaunchedEffect(eventId) {
        eventId?.let {
            viewModel.getEvent(it)
        }
    }

    val event by viewModel.events.collectAsState()
    val scrollState = rememberScrollState()

    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val eventData = event!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            HeaderImage(eventData.posterUrl)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = eventData.title ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                EventInfoRow(
                    icon = painterResource(id = R.drawable.ic_calendar),
                    title = eventData.eventDate ?: ""
                )

                Spacer(modifier = Modifier.height(8.dp))

                EventInfoRow(
                    icon = painterResource(id = R.drawable.ic_location),
                    title = eventData.location ?: ""
                )

                Spacer(modifier = Modifier.height(8.dp))

                EventInfoRow(
                    icon = painterResource(id = R.drawable.ic_organizer),
                    title = eventData.university ?: ""
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "About Event",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = eventData.description ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = if (eventData.isRegistered == 1) "You are registered for this event" else "You are not registered for this event",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (eventData.isRegistered == 1) Color.Green else Color.Red
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterButton(navController, eventId, eventData.isRegistered == 1, viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                CancelButton(navController = navController)
            }
        }
    }
}

@Composable
fun HeaderImage(posterUrl: String?) {
    val painter = rememberAsyncImagePainter(
        model = posterUrl,
        placeholder = painterResource(id = R.drawable.image_example),
        error = painterResource(id = R.drawable.foto_seminar)
    )
    Image(
        painter = painter,
        contentDescription = "Event Header Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
@Composable
fun EventInfoRow(icon: Painter, title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun RegisterButton(navController: NavController, eventId: String?, isRegistered: Boolean, viewModel: EventDetailViewModel) {
    Button(
        onClick = {
            eventId?.let {
                navController.navigate("registerEvent/$it")
                viewModel.refreshEvent(it) // Refresh event data after registration
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26A541)),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = !isRegistered
    ) {
        Text(
            text = "Register Here",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CancelButton(navController: NavController) {
    Button(
        onClick = { navController.popBackStack() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = "Cancel",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Green,
            textAlign = TextAlign.Center
        )
    }
}