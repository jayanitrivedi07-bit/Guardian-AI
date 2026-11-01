package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*

@Composable
fun PrivacyCentreScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val settings by repository.privacySettings.collectAsState()
  var showWipeDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 96.dp, top = 6.dp)
  ) {
    // Header
    item {
      Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
          text = "Privacy controls",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Manage location sharing, retention, and on-device storage",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // Granular Toggles
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Sharing & permissions", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

          Spacer(modifier = Modifier.height(12.dp))

          // 1. Share live location with circle
          PrivacyToggleRow(
            title = "Share live location with circle",
            description = "Transmits location updates only while a journey or SOS session is active.",
            checked = settings.shareLiveLocationWithCircle,
            onCheckedChange = { repository.updatePrivacySettings(settings.copy(shareLiveLocationWithCircle = it)) }
          )

          HorizontalDivider(color = DividerLight, modifier = Modifier.padding(vertical = 12.dp))

          // 2. Low-connectivity offline queue
          PrivacyToggleRow(
            title = "Low-connectivity offline queue",
            description = "Stores unsent emergency events locally to retry automatically when signal returns.",
            checked = settings.enableOfflineQueue,
            onCheckedChange = { repository.updatePrivacySettings(settings.copy(enableOfflineQueue = it)) }
          )

          HorizontalDivider(color = DividerLight, modifier = Modifier.padding(vertical = 12.dp))

          // 3. Auto-start audio recording on SOS
          PrivacyToggleRow(
            title = "Auto-record audio on SOS",
            description = "Automatically launches microphone capture with visible red banner during SOS activation.",
            checked = settings.autoStartAudioRecordOnSos,
            onCheckedChange = { repository.updatePrivacySettings(settings.copy(autoStartAudioRecordOnSos = it)) }
          )
        }
      }
    }

    // Data Retention Policy
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Location retention", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "Location history is purged automatically after ${settings.retainLocationHistoryDays} days. Historical tracks are never shared or monetized.",
            color = CharcoalSecondary,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(12.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1, 7, 30).forEach { days ->
              val isSelected = settings.retainLocationHistoryDays == days
              OutlinedButton(
                onClick = { repository.updatePrivacySettings(settings.copy(retainLocationHistoryDays = days)) },
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (isSelected) PaleBlush else WarmSurface,
                  contentColor = if (isSelected) SoftPinkDark else CharcoalSecondary
                ),
                border = BorderStroke(1.dp, if (isSelected) SoftPink else BorderSubtle),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("$days days", fontSize = 12.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }
      }
    }

    // Local Data Deletion
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Delete local safety data", color = EmergencyRed, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "Permanently delete all on-device emergency sessions, evidence recordings, and cached journey data.",
            color = CharcoalSecondary,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(12.dp))
          OutlinedButton(
            onClick = { showWipeDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
            border = BorderStroke(1.dp, EmergencyRed.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("wipe_data_button")
          ) {
            Icon(Icons.Default.DeleteForever, contentDescription = "Wipe data", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Delete all local data", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
  }

  if (showWipeDialog) {
    AlertDialog(
      onDismissRequest = { showWipeDialog = false },
      title = { Text("Delete local data?", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold) },
      text = {
        Text(
          "This will purge all local emergency audit logs, cached coordinates, and audio files from this phone. This cannot be undone.",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            repository.wipeAllLocalData()
            showWipeDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Delete data")
        }
      },
      dismissButton = {
        TextButton(onClick = { showWipeDialog = false }) {
          Text("Cancel", color = CharcoalSecondary)
        }
      }
    )
  }
}

@Composable
private fun PrivacyToggleRow(
  title: String,
  description: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(title, color = CharcoalPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(description, color = CharcoalSecondary, fontSize = 11.sp, lineHeight = 15.sp)
    }
    Spacer(modifier = Modifier.width(12.dp))
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = SoftPinkDark,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = Color(0xFFD8CCD0)
      )
    )
  }
}
