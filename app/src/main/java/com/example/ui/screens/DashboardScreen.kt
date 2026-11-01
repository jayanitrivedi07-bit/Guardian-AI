package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.GuardianRepository
import com.example.ui.components.DeliveryStatusPill
import com.example.ui.components.SosButton
import com.example.ui.components.StaleLocationBanner
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
  repository: GuardianRepository,
  isTestMode: Boolean,
  onNavigateToTab: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val activeSession by repository.activeEmergencySession.collectAsState()
  val location by repository.currentLocation.collectAsState()
  val contacts by repository.trustedContacts.collectAsState()
  val activeJourney by repository.activeJourney.collectAsState()
  val currentLanguage by repository.currentLanguage.collectAsState()

  var showCancelDialog by remember { mutableStateOf(false) }
  var cancelReason by remember { mutableStateOf("Accidental key press while carrying phone.") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 96.dp, top = 6.dp)
  ) {
    // 1. UNDERSTATED WARM GREETING & STATUS SUMMARY
    item {
      Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
          text = when (currentLanguage) {
            AppLanguage.EN -> "Welcome, Priya"
            AppLanguage.HI -> "नमस्ते, प्रिया"
            AppLanguage.MR -> "नमस्ते, प्रिया"
          },
          color = CharcoalPrimary,
          fontSize = 22.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (activeSession != null) {
            "Emergency session is currently active"
          } else if (activeJourney != null) {
            "Active journey to ${activeJourney!!.destination}"
          } else {
            "Safety circle ready • ${contacts.size} contacts verified"
          },
          color = if (activeSession != null) EmergencyRed else CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // 2. ACTIVE EMERGENCY CARD (If SOS was triggered)
    if (activeSession != null) {
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = EmergencySurface),
          border = BorderStroke(1.5.dp, EmergencyRed),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("active_emergency_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(EmergencyRed)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (activeSession!!.isTest) "Test Emergency Active" else "Emergency Active",
                  color = EmergencyRed,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
              }
              Text(
                text = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(Date(activeSession!!.startedAt)),
                color = CharcoalSecondary,
                fontSize = 11.sp
              )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Alerts sent to your safety circle:",
              color = CharcoalPrimary,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            activeSession!!.deliveryAttempts.forEach { attempt ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
              ) {
                Column {
                  Text(attempt.contactName, color = CharcoalPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text(attempt.contactPhone, color = CharcoalSecondary, fontSize = 11.sp)
                }
                DeliveryStatusPill(status = attempt.status)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Row(
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              OutlinedButton(
                onClick = { showCancelDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningAmber),
                border = BorderStroke(1.dp, WarningAmber),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("Cancel Alert", fontSize = 12.sp)
              }
              Button(
                onClick = { repository.resolveSos() },
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("I am safe now", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
              }
            }
          }
        }
      }
    }

    // 3. PROMINENT UNMISTAKABLE SOS ACTION
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(vertical = 22.dp, horizontal = 16.dp)
        ) {
          SosButton(
            isTestMode = isTestMode,
            onActivated = {
              repository.triggerSos(isTest = isTestMode)
            }
          )
        }
      }
    }

    // 4. QUICK DIRECT DIALS (112 Emergency & 1091 Women Helpline)
    item {
      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Button(
          onClick = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
            context.startActivity(intent)
          },
          colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("dial_112_button")
        ) {
          Icon(Icons.Default.Call, contentDescription = "Dial 112", tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Call 112", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }

        OutlinedButton(
          onClick = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1091"))
            context.startActivity(intent)
          },
          colors = ButtonDefaults.outlinedButtonColors(contentColor = CharcoalPrimary),
          border = BorderStroke(1.dp, BorderSubtle),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("dial_1091_button")
        ) {
          Icon(Icons.Default.PhoneInTalk, contentDescription = "Dial 1091", tint = SoftPinkDark, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("1091 Helpline", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
      }
    }

    // 5. CURRENT LOCATION & STALE WARNINGS
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Place, contentDescription = "Location", tint = SoftPinkDark, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Current location", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Text(
              text = "Refresh",
              color = SoftPinkDark,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              modifier = Modifier
                .clickable { repository.refreshLocation() }
                .padding(4.dp)
            )
          }

          StaleLocationBanner(location = location, modifier = Modifier.padding(vertical = 4.dp))

          Spacer(modifier = Modifier.height(2.dp))
          Text(location.approximateAddress, color = CharcoalPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            "${String.format("%.4f", location.latitude)}° N, ${String.format("%.4f", location.longitude)}° E • Accuracy ±${location.accuracyMeters.toInt()}m",
            color = CharcoalSecondary,
            fontSize = 11.sp
          )
        }
      }
    }

    // 6. QUICK ACCESS: TRUSTED CONTACTS SUMMARY
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToTab(2) }
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.People, contentDescription = "Safety Circle", tint = SoftPinkDark, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Trusted contacts (${contacts.size})", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Text("Manage", color = SoftPinkDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }

          Spacer(modifier = Modifier.height(8.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            contacts.take(3).forEach { contact ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(PaleBlush)
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "${contact.name} (${contact.relationship})",
                  color = CharcoalPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }
    }

    // 7. JOURNEY STATUS SHORTCUT
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToTab(1) }
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Navigation, contentDescription = "Journey", tint = SoftPinkDark, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                if (activeJourney != null) "Active journey: ${activeJourney!!.destination}" else "No active journey",
                color = CharcoalPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
              )
              Text(
                if (activeJourney != null) "Sharing location with contacts" else "Plan a journey with safer route options",
                color = CharcoalSecondary,
                fontSize = 11.sp
              )
            }
          }
          Icon(Icons.Default.ChevronRight, contentDescription = "Open Journey", tint = CharcoalMuted)
        }
      }
    }
  }

  // False Alarm Cancellation Dialog
  if (showCancelDialog) {
    AlertDialog(
      onDismissRequest = { showCancelDialog = false },
      title = { Text("Cancel false alarm", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold) },
      text = {
        Column {
          Text(
            "A cancellation notice will be sent to your trusted contacts so they know you are safe. This event remains in your session history.",
            fontSize = 13.sp,
            color = CharcoalSecondary
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = cancelReason,
            onValueChange = { cancelReason = it },
            label = { Text("Reason for cancellation") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            repository.cancelSos(cancelReason)
            showCancelDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark)
        ) {
          Text("Confirm cancellation")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCancelDialog = false }) {
          Text("Keep active", color = CharcoalSecondary)
        }
      }
    )
  }
}
