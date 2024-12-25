package com.example.evelin.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.evelin.ui.component.bar.BottomNavBar
import com.example.evelin.ui.component.bar.TopBar
import com.example.evelin.ui.component.item.EventItem
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.evelin.ViewModelFactory
import com.example.evelin.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    // Collect events from ViewModel
    val events by viewModel.events.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            onFilterClick = { /* Handle filter click */ },
            onSearchTextChange = { query -> viewModel.searchEvents(query) },
            navController = navController
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Upcoming Events
        Text(text = "Upcoming Event", modifier = Modifier.padding(start = 16.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(events) { event ->
                EventItem(
                    navController = navController,
                    eventId = event.id.toString(),  // Pass the event ID
                    eventName = event.title ?: "Unnamed Event",
                    eventDescription = event.description ?: "No description",
                    eventImage = rememberImagePainter(data = event.posterUrl)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recommended Events (using the same events list for now)
        Text(text = "Recommendation Event", modifier = Modifier.padding(start = 16.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(events) { event ->
                EventItem(
                    navController = navController,
                    eventId = event.id.toString(),  // Pass the event ID
                    eventName = event.title ?: "Unnamed Event",
                    eventDescription = event.description ?: "No description",
                    eventImage = event.posterUrl?.let { rememberImagePainter(data = it) }
                        ?: painterResource(id = R.drawable.foto_seminar)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}