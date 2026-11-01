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
fun TestModeScreen(
  repository: GuardianRepository,
  isTestMode: Boolean,
  onToggleTestMode: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isTestAlertFired by remember { mutableStateOf(false) }

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
          text = "Test & readiness mode",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Rehearse emergency SOS alerts safely without disturbing authorities",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // Toggle Mode Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Rehearsal test mode", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isTestMode) WarningAmberSurface else PaleBlush)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (isTestMode) "Active" else "Standby",
                color = if (isTestMode) WarningAmber else CharcoalSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            "When enabled, SOS alerts are unmistakably prefixed with [TEST] and 112 emergency calls are disabled.",
            color = CharcoalSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = onToggleTestMode,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isTestMode) WarningAmber else SoftPinkDark
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = if (isTestMode) "Exit test mode" else "Enable test mode",
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            )
          }
        }
      }
    }

    // System Readiness Checklist
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Safety readiness checklist", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

          Spacer(modifier = Modifier.height(10.dp))
          CleanChecklistItem("Location permissions (GPS & network)", isReady = true)
          CleanChecklistItem("Safety circle verified (3 contacts)", isReady = true)
          CleanChecklistItem("Offline emergency queue enabled", isReady = true)
          CleanChecklistItem("Audio evidence recorder verified", isReady = true)
          CleanChecklistItem("112 direct routing route active", isReady = true)
        }
      }
    }

    // Dry-run Test Alert
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Send dry-run alert", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "Sends an isolated test notification to verify your alert delivery pipeline.",
            color = CharcoalSecondary,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(14.dp))
          OutlinedButton(
            onClick = {
              repository.triggerSos(isTest = true)
              isTestAlertFired = true
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CharcoalPrimary),
            border = BorderStroke(1.dp, BorderSubtle),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(42.dp)
              .testTag("fire_test_alert_button")
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send test alert", tint = SoftPinkDark, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Send test alert", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }

          if (isTestAlertFired) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              "✓ Test alert dispatched. Switch to SOS tab to observe simulated delivery receipts.",
              color = SuccessGreen,
              fontSize = 11.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CleanChecklistItem(text: String, isReady: Boolean) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    Icon(
      imageVector = if (isReady) Icons.Default.CheckCircle else Icons.Default.Cancel,
      contentDescription = if (isReady) "Ready" else "Pending",
      tint = if (isReady) SuccessGreen else EmergencyRed,
      modifier = Modifier.size(15.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Text(text, color = CharcoalPrimary, fontSize = 12.sp)
  }
}
