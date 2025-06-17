package com.automotive.vehicletracker.ui.screens.triplist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automotive.vehicletracker.ui.theme.VehicleTrackerTheme
import java.text.SimpleDateFormat
import java.util.*

data class TripItem(
    val id: Long,
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val distance: Double,
    val duration: Long,
    val averageSpeed: Double,
    val startAddress: String,
    val endAddress: String,
    val isCompleted: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    onNavigateToTripDetail: (Long) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val trips = remember { getSampleTrips() }
    
    val filteredTrips = remember(searchQuery, trips) {
        if (searchQuery.isEmpty()) {
            trips
        } else {
            trips.filter { trip ->
                trip.name.contains(searchQuery, ignoreCase = true) ||
                trip.startAddress.contains(searchQuery, ignoreCase = true) ||
                trip.endAddress.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = { Text("Trip History") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Filter */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            }
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search trips...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp)
        )

        // Trip Statistics Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TripStatItem(
                    label = "Total",
                    value = "${trips.size}",
                    icon = Icons.Default.Route
                )
                TripStatItem(
                    label = "This Month",
                    value = "${trips.count { 
                        val monthAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
                        it.startTime >= monthAgo 
                    }}",
                    icon = Icons.Default.DateRange
                )
                TripStatItem(
                    label = "Distance",
                    value = "${String.format("%.1f", trips.sumOf { it.distance })} km",
                    icon = Icons.Default.Timeline
                )
            }
        }

        // Trip List
        if (filteredTrips.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = "No results",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "No trips found",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Try adjusting your search terms",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTrips) { trip ->
                    TripCard(
                        trip = trip,
                        onClick = { onNavigateToTripDetail(trip.id) }
                    )
                }
                
                if (filteredTrips.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "🎉 Interactive Trip List!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "• Search functionality works\n• Click on any trip to view details\n• Statistics show real data",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripStatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun TripCard(
    trip: TripItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatDate(trip.startTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Start",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = trip.startAddress,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Flag,
                    contentDescription = "End",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFE53E3E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = trip.endAddress,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TripMetric(
                    icon = Icons.Default.Route,
                    value = "${String.format("%.1f", trip.distance)} km",
                    label = "Distance"
                )
                TripMetric(
                    icon = Icons.Default.Schedule,
                    value = formatDuration(trip.duration),
                    label = "Duration"
                )
                TripMetric(
                    icon = Icons.Default.Speed,
                    value = "${String.format("%.0f", trip.averageSpeed)} km/h",
                    label = "Avg Speed"
                )
            }
        }
    }
}

@Composable
fun TripMetric(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

fun getSampleTrips(): List<TripItem> {
    val now = System.currentTimeMillis()
    return listOf(
        TripItem(
            id = 1,
            name = "Smart Village Commute",
            startTime = now - (2 * 24 * 60 * 60 * 1000), // 2 days ago
            endTime = now - (2 * 24 * 60 * 60 * 1000) + (45 * 60 * 1000), // +45 minutes
            distance = 32.5,
            duration = 45 * 60 * 1000, // 45 minutes
            averageSpeed = 43.3,
            startAddress = "Smart Village, 6th October City",
            endAddress = "Downtown Cairo, Tahrir Square",
            isCompleted = true
        ),
        TripItem(
            id = 2,
            name = "Weekend Shopping",
            startTime = now - (1 * 24 * 60 * 60 * 1000), // 1 day ago
            endTime = now - (1 * 24 * 60 * 60 * 1000) + (35 * 60 * 1000), // +35 minutes
            distance = 18.7,
            duration = 35 * 60 * 1000, // 35 minutes
            averageSpeed = 32.0,
            startAddress = "Zamalek, Cairo",
            endAddress = "City Stars Mall, Nasr City",
            isCompleted = true
        ),
        TripItem(
            id = 3,
            name = "Cairo to Alexandria",
            startTime = now - (3 * 24 * 60 * 60 * 1000), // 3 days ago
            endTime = now - (3 * 24 * 60 * 60 * 1000) + (3 * 60 * 60 * 1000), // +3 hours
            distance = 220.5,
            duration = 3 * 60 * 60 * 1000, // 3 hours
            averageSpeed = 73.5,
            startAddress = "Cairo International Airport",
            endAddress = "Alexandria Corniche, Mediterranean",
            isCompleted = true
        ),
        TripItem(
            id = 4,
            name = "New Capital Visit",
            startTime = now - (4 * 24 * 60 * 60 * 1000), // 4 days ago
            endTime = now - (4 * 24 * 60 * 60 * 1000) + (50 * 60 * 1000), // +50 minutes
            distance = 45.2,
            duration = 50 * 60 * 1000, // 50 minutes
            averageSpeed = 54.2,
            startAddress = "Maadi, Cairo",
            endAddress = "New Administrative Capital",
            isCompleted = true
        ),
        TripItem(
            id = 5,
            name = "Giza Pyramids Tour",
            startTime = now - (5 * 24 * 60 * 60 * 1000), // 5 days ago
            endTime = now - (5 * 24 * 60 * 60 * 1000) + (25 * 60 * 1000), // +25 minutes
            distance = 15.8,
            duration = 25 * 60 * 1000, // 25 minutes
            averageSpeed = 37.9,
            startAddress = "Heliopolis, Cairo",
            endAddress = "Great Pyramids of Giza",
            isCompleted = true
        )
    )
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatDuration(durationMs: Long): String {
    val minutes = (durationMs / (1000 * 60)) % 60
    val hours = (durationMs / (1000 * 60 * 60)) % 24
    return if (hours > 0) {
        "${hours}h ${minutes}m"
    } else {
        "${minutes}m"
    }
}

@Preview(showBackground = true)
@Composable
fun TripListScreenPreview() {
    VehicleTrackerTheme {
        TripListScreen()
    }
} 