package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FakeCallScreen(
  modifier: Modifier = Modifier
) {
  var isCallActive by remember { mutableStateOf(false) }
  var isCallAnswered by remember { mutableStateOf(false) }
  var callerName by remember { mutableStateOf("Mom") }
  var callDurationSeconds by remember { mutableStateOf(0) }

  LaunchedEffect(isCallAnswered) {
    if (isCallAnswered) {
      callDurationSeconds = 0
      while (isCallAnswered) {
        delay(1000)
        callDurationSeconds += 1
      }
    }
  }

  if (isCallActive) {
    // REALISTIC INCOMING / ACTIVE CALL SCREEN
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF1E1920))
        .padding(24.dp)
        .testTag("fake_call_active_screen")
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
      ) {
        // Caller Info
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 48.dp)
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .background(Color(0xFF2E2632))
          ) {
            Icon(Icons.Default.Person, contentDescription = "Caller", tint = Color.White, modifier = Modifier.size(44.dp))
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(callerName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            if (isCallAnswered) {
              val m = callDurationSeconds / 60
              val s = callDurationSeconds % 60
              String.format("%02d:%02d", m, s)
            } else "Incoming call...",
            color = if (isCallAnswered) SuccessGreen else Color(0xFFB5A8B0),
            fontSize = 14.sp
          )
        }

        // Action Buttons
        if (!isCallAnswered) {
          Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 50.dp)
          ) {
            // Decline
            IconButton(
              onClick = { isCallActive = false },
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(EmergencyRed)
            ) {
              Icon(Icons.Default.CallEnd, contentDescription = "Decline call", tint = Color.White, modifier = Modifier.size(28.dp))
            }

            // Accept
            IconButton(
              onClick = { isCallAnswered = true },
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(SuccessGreen)
            ) {
              Icon(Icons.Default.Call, contentDescription = "Accept call", tint = Color.White, modifier = Modifier.size(28.dp))
            }
          }
        } else {
          // In-Call Hangup
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 50.dp)
          ) {
            IconButton(
              onClick = {
                isCallAnswered = false
                isCallActive = false
              },
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(EmergencyRed)
            ) {
              Icon(Icons.Default.CallEnd, contentDescription = "End call", tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tap to end", color = Color(0xFFB5A8B0), fontSize = 11.sp)
          }
        }
      }
    }
  } else {
    // SETUP SCREEN
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 18.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
          text = "Simulated call",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Discreetly step away from uncomfortable situations",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }

      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Call settings", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            "Triggers a simulated incoming call on your device. Useful for creating a polite reason to leave an uncomfortable conversation.",
            color = CharcoalSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))
          OutlinedTextField(
            value = callerName,
            onValueChange = { callerName = it },
            label = { Text("Caller display name") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = {
              isCallActive = true
              isCallAnswered = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("trigger_fake_call_button")
          ) {
            Icon(Icons.Default.PhoneInTalk, contentDescription = "Trigger call", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Simulate call now", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          }
        }
      }

      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Simulation transparency", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "• This runs strictly on-device and does not place actual phone calls.\n" +
            "• No call credits or cellular network required.\n" +
            "• Never impersonates official emergency services.",
            color = CharcoalSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}
