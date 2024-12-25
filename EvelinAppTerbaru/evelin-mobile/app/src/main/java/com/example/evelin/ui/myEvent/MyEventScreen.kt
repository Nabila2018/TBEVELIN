package com.example.evelin.ui.myEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import com.example.evelin.ui.theme.LightGreen

@Composable
fun MyEventScreen(onBackClick: () -> Unit = {}, navController: NavController,viewModel: MyEventViewModel = viewModel(factory = ViewModelFactory.getInstance(
    LocalContext.current))
) {

    val events by viewModel.events.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        // Header Text
        Text(
            text = "Kegiatan yang diselenggarakan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // Activities List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(events) { event ->
                ActivityRow(
                    title = event.title.toString(),
                    eventId = event.id.toString(),
                    navController = navController
                )
            }

        }
    }
}

@Composable
fun ActivityRow(
    title: String,
    eventId: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGreen)
            .clickable { navController.navigate("editEvent/$eventId") } // Added navigation on entire row
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Lihat detail",
            fontSize = 14.sp,
            color = Color.Blue,
            textAlign = TextAlign.End,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewEventHistoryScreen() {
//    EventHistoryScreen()
//}