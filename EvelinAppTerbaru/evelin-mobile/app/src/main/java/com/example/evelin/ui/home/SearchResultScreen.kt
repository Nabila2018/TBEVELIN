package com.example.evelin.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.evelin.ViewModelFactory
import com.example.evelin.ui.component.item.EventItem
import com.example.evelin.data.response.DataItem

@Composable
fun SearchResultsScreen(
    navController: NavController,
    query: String,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    // Collect search results from ViewModel
    val searchResults = viewModel.searchResults.collectAsState().value

    // Perform search when the screen is displayed
    LaunchedEffect(query) {
        viewModel.searchEvents(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display search title
        Text(
            text = "Search Results for \"$query\"",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // If search results are empty, show a placeholder message
        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found for \"$query\".",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Display search results in a lazy column
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(searchResults) { event: DataItem ->
                    EventItem(
                        navController = navController,
                        eventId = event.id.toString(),
                        eventName = event.title ?: "Unnamed Event",
                        eventDescription = event.description ?: "No description",
                        eventImage = rememberAsyncImagePainter(model = event.posterUrl)
                    )
                }
            }
        }
    }
}
