package com.automotive.vehicletracker.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automotive.vehicletracker.ui.theme.VehicleTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    var autoStartTracking by remember { mutableStateOf(true) }
    var voiceNavigation by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }
    var locationAccuracy by remember { mutableStateOf("High") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tracking Settings
            item {
                SettingsSection(title = "Tracking") {
                    SettingsSwitch(
                        icon = Icons.Default.PlayArrow,
                        title = "Auto-start Tracking",
                        subtitle = "Automatically start when driving detected",
                        checked = autoStartTracking,
                        onCheckedChange = { autoStartTracking = it }
                    )
                    
                    SettingsSwitch(
                        icon = Icons.Default.RecordVoiceOver,
                        title = "Voice Navigation",
                        subtitle = "Enable voice guidance during trips",
                        checked = voiceNavigation,
                        onCheckedChange = { voiceNavigation = it }
                    )
                    
                    SettingsItem(
                        icon = Icons.Default.GpsFixed,
                        title = "Location Accuracy",
                        subtitle = locationAccuracy,
                        onClick = { /* Show accuracy options */ }
                    )
                }
            }

            // App Settings
            item {
                SettingsSection(title = "Appearance") {
                    SettingsSwitch(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                    
                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = "Language",
                        subtitle = "English",
                        onClick = { /* Show language options */ }
                    )
                }
            }

            // Data & Privacy
            item {
                SettingsSection(title = "Data & Privacy") {
                    SettingsItem(
                        icon = Icons.Default.Download,
                        title = "Export Data",
                        subtitle = "Download your trip data",
                        onClick = { /* Export data */ }
                    )
                    
                    SettingsItem(
                        icon = Icons.Default.DeleteForever,
                        title = "Clear All Data",
                        subtitle = "Remove all trips and settings",
                        onClick = { /* Clear data */ }
                    )
                    
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        subtitle = "View privacy policy",
                        onClick = { /* Show privacy policy */ }
                    )
                }
            }

            // About
            item {
                SettingsSection(title = "About") {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "App Version",
                        subtitle = "1.0.0",
                        onClick = { /* Show version info */ }
                    )
                    
                    SettingsItem(
                        icon = Icons.Default.BugReport,
                        title = "Report Bug",
                        subtitle = "Send feedback to developers",
                        onClick = { /* Report bug */ }
                    )
                    
                    SettingsItem(
                        icon = Icons.Default.Star,
                        title = "Rate App",
                        subtitle = "Rate us on the Play Store",
                        onClick = { /* Rate app */ }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SettingsSwitch(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    VehicleTrackerTheme {
        SettingsScreen()
    }
} 