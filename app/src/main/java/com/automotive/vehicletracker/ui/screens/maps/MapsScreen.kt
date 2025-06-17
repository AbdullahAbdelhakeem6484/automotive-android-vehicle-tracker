package com.automotive.vehicletracker.ui.screens.maps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automotive.vehicletracker.ui.theme.VehicleTrackerTheme
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    onNavigateBack: () -> Unit = {}
) {
    var isTracking by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableStateOf(0f) }
    var distance by remember { mutableStateOf(0.0f) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var routePoints by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val startTime = remember { mutableStateOf(0L) }

    // Simulate real-time tracking data
    LaunchedEffect(isTracking) {
        if (isTracking) {
            startTime.value = System.currentTimeMillis()
            while (isTracking) {
                kotlinx.coroutines.delay(2000) // Update every 2 seconds
                if (isTracking) {
                    currentSpeed = Random.nextFloat() * 25f + 15f // 15-40 km/h
                    distance += (currentSpeed / 3.6f * 2f) / 1000f // Add distance based on speed (2 second intervals)
                    elapsedTime = System.currentTimeMillis() - startTime.value
                    routePoints += 1
                }
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
            title = { Text("Live Tracking") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Center on location */ }) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Center")
                }
            }
        )

        // Enhanced Egyptian Map with Route Visualization
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isTracking) Color(0xFFE8F5E8) else Color(0xFFF8F9FA)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Egyptian Map Background Grid
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val gridSpacing = 30.dp.toPx()
                    
                    // Draw grid lines
                    for (i in 0..(size.width / gridSpacing).toInt()) {
                        drawLine(
                            color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.1f),
                            start = androidx.compose.ui.geometry.Offset(i * gridSpacing, 0f),
                            end = androidx.compose.ui.geometry.Offset(i * gridSpacing, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    
                    for (i in 0..(size.height / gridSpacing).toInt()) {
                        drawLine(
                            color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.1f),
                            start = androidx.compose.ui.geometry.Offset(0f, i * gridSpacing),
                            end = androidx.compose.ui.geometry.Offset(size.width, i * gridSpacing),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    
                    // Draw route if tracking
                    if (isTracking && routePoints > 0) {
                        val path = androidx.compose.ui.graphics.Path()
                        
                        // Smart Village (starting point)
                        val startX = size.width * 0.2f
                        val startY = size.height * 0.3f
                        path.moveTo(startX, startY)
                        
                        // 6th October City
                        if (routePoints > 2) {
                            val middleX = size.width * 0.5f
                            val middleY = size.height * 0.4f
                            path.lineTo(middleX, middleY)
                        }
                        
                        // Giza area
                        if (routePoints > 5) {
                            val gizaX = size.width * 0.6f
                            val gizaY = size.height * 0.6f
                            path.lineTo(gizaX, gizaY)
                        }
                        
                        // Downtown Cairo
                        if (routePoints > 8) {
                            val endX = size.width * 0.8f
                            val endY = size.height * 0.7f
                            path.lineTo(endX, endY)
                        }
                        
                        drawPath(
                            path = path,
                            color = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = 4.dp.toPx(),
                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        )
                    }
                }

                // Location Markers
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top row - Smart Village and 6th October
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LocationMarker(
                            name = "Smart Village",
                            isActive = isTracking,
                            color = if (isTracking) Color(0xFF4CAF50) else Color(0xFF2196F3)
                        )
                        
                        if (routePoints > 2) {
                            LocationMarker(
                                name = "6th October",
                                isActive = false,
                                color = Color(0xFFFF9800)
                            )
                        }
                    }

                    // Center - Current status
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isTracking) {
                            // Animated tracking pulse
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        Color(0xFF4CAF50),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(
                                            Color.White,
                                            shape = androidx.compose.foundation.shape.CircleShape
                                        )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🔴 LIVE TRACKING",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFE53E3E),
                                fontWeight = FontWeight.Bold
                            )
                            if (routePoints > 0) {
                                Text(
                                    text = "Egypt Route: $routePoints points",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        } else {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = "Map",
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF81C784)
                            )
                            Text(
                                text = "Egypt Interactive Map",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF388E3C)
                            )
                            Text(
                                text = "Ready for tracking in Cairo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    // Bottom row - Giza and Downtown Cairo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (routePoints > 5) {
                            LocationMarker(
                                name = "Giza",
                                isActive = false,
                                color = Color(0xFF9C27B0)
                            )
                        }
                        
                        LocationMarker(
                            name = "Downtown Cairo",
                            isActive = false,
                            color = Color(0xFFE91E63)
                        )
                    }
                }

                // Stats overlay
                if (routePoints > 0) {
                    Card(
                        modifier = Modifier.align(Alignment.TopEnd),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$routePoints",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "GPS Points",
                                fontSize = 10.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "🇪🇬 Egypt",
                                fontSize = 10.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
        }

        // Real-time Stats
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isTracking) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTracking) "🔴 Tracking Active" else "Ready to Track",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Switch(
                        checked = isTracking,
                        onCheckedChange = { 
                            isTracking = it
                            if (!it) {
                                // Reset data when stopping
                                currentSpeed = 0f
                                distance = 0f
                                elapsedTime = 0L
                                routePoints = 0
                            }
                        }
                    )
                }
                
                if (isTracking) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TripInfoItem(
                            label = "Speed",
                            value = "${String.format("%.1f", currentSpeed)} km/h",
                            icon = Icons.Default.Speed
                        )
                        TripInfoItem(
                            label = "Distance",
                            value = if (distance >= 1f) "${String.format("%.2f", distance)} km" else "${String.format("%.0f", distance * 1000)} m",
                            icon = Icons.Default.Route
                        )
                        TripInfoItem(
                            label = "Time",
                            value = formatTime(elapsedTime),
                            icon = Icons.Default.Schedule
                        )
                    }
                }
            }
        }

        // Error display
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { 
                    if (isTracking) {
                        // Simulate saving trip
                        errorMessage = null
                        isTracking = false
                        // Show success message briefly
                        errorMessage = "Trip saved successfully!"
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = isTracking
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Trip")
            }
            
            Button(
                onClick = { 
                    isTracking = !isTracking
                    errorMessage = null
                    if (isTracking) {
                        // Simulate start tracking
                        errorMessage = "Tracking started successfully!"
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    if (isTracking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isTracking) "Stop" else "Start")
            }
        }
        
        // Clear error message after some time
        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                kotlinx.coroutines.delay(3000)
                errorMessage = null
            }
        }
    }
}

@Composable
fun TripInfoItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

fun formatTime(timeInMillis: Long): String {
    val seconds = (timeInMillis / 1000) % 60
    val minutes = (timeInMillis / (1000 * 60)) % 60
    val hours = (timeInMillis / (1000 * 60 * 60)) % 24
    
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

@Composable
fun LocationMarker(
    name: String,
    isActive: Boolean,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) color else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                fontSize = 10.sp,
                color = if (isActive) Color.White else Color(0xFF333333),
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapsScreenPreview() {
    VehicleTrackerTheme {
        MapsScreen()
    }
} 