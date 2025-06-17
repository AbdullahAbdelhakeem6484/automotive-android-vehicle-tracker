package com.automotive.vehicletracker.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automotive.vehicletracker.ui.theme.VehicleTrackerTheme

@Composable
fun DashboardScreen(
    onNavigateToMaps: () -> Unit = {},
    onNavigateToTripList: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    var sampleDataCreated by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(primaryColor, primaryColor.copy(alpha = 0.8f))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vehicle Tracker",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
                
                Text(
                    text = "Welcome back! Ready for your next journey?",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp
                )
            }
        }

        // Content Section
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Cards
            item {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(getInteractiveStatsCards(sampleDataCreated)) { card ->
                        StatsCard(
                            title = card.title,
                            value = card.value,
                            icon = card.icon,
                            color = card.color,
                            modifier = Modifier.width(160.dp)
                        )
                    }
                }
            }

            // Quick Actions
            item {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Start Trip",
                        subtitle = "Begin tracking",
                        icon = Icons.Default.PlayArrow,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToMaps
                    )
                    
                    QuickActionCard(
                        title = "View Map",
                        subtitle = "Live tracking",
                        icon = Icons.Default.Map,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToMaps
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Trip History",
                        subtitle = "View all trips",
                        icon = Icons.Default.History,
                        color = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToTripList
                    )
                    
                    QuickActionCard(
                        title = "Analytics",
                        subtitle = "View insights",
                        icon = Icons.Default.Analytics,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Interactive Sample Data Section
            item {
                if (!sampleDataCreated) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Welcome to Vehicle Tracker!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Navigation is now fully functional! Tap the buttons above to explore different screens.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Button(onClick = { sampleDataCreated = true }) {
                                Text("Show Sample Data")
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "🎉 App is Fully Interactive!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• Navigation between screens works\n• Maps screen has real tracking simulation\n• Trip list shows organized data\n• Settings screen is interactive",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

data class StatsCardData(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

fun getInteractiveStatsCards(sampleDataCreated: Boolean): List<StatsCardData> {
    return if (sampleDataCreated) {
        listOf(
            StatsCardData("Total Trips", "12", Icons.Default.Route, Color(0xFF4CAF50)),
            StatsCardData("Distance", "856 km", Icons.Default.Timeline, Color(0xFF2196F3)),
            StatsCardData("Time", "24h 30m", Icons.Default.Schedule, Color(0xFFFF9800)),
            StatsCardData("Avg Speed", "42 km/h", Icons.Default.Speed, Color(0xFF9C27B0))
        )
    } else {
        listOf(
            StatsCardData("Total Trips", "0", Icons.Default.Route, Color(0xFF4CAF50)),
            StatsCardData("Distance", "0 km", Icons.Default.Timeline, Color(0xFF2196F3)),
            StatsCardData("Time", "0h 0m", Icons.Default.Schedule, Color(0xFFFF9800)),
            StatsCardData("Avg Speed", "0 km/h", Icons.Default.Speed, Color(0xFF9C27B0))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    VehicleTrackerTheme {
        DashboardScreen()
    }
} 