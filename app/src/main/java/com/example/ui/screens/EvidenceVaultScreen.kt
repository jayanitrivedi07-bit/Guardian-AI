package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EvidenceItem
import com.example.data.model.EvidenceType
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EvidenceVaultScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val vaultItems by repository.evidenceVault.collectAsState()
  val isRecording by repository.isRecordingAudio.collectAsState()
  val recordingSeconds by repository.recordingSeconds.collectAsState()

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
          text = "Evidence vault",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Privately document notes and audio recordings during incidents",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // Recorder Card
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
            Text("Audio evidence recorder", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(PaleBlush)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text("Encrypted locally", color = SoftPinkDark, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            "Record audio memos with verifiable timestamps. Recordings are stored locally on device and never uploaded without your consent.",
            color = CharcoalSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))
          if (!isRecording) {
            Button(
              onClick = { repository.startRecordingEvidence() },
              colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("start_recording_button")
            ) {
              Icon(Icons.Default.Mic, contentDescription = "Start recording", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Start recording audio", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          } else {
            val mins = recordingSeconds / 60
            val secs = recordingSeconds % 60
            Button(
              onClick = { repository.stopRecordingEvidence() },
              colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("stop_recording_button")
            ) {
              Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                "Stop recording (${String.format("%02d:%02d", mins, secs)})",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }

    // Vault Item List
    item {
      Text(
        "Saved recordings and notes (${vaultItems.size})",
        color = CharcoalPrimary,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
      )
    }

    items(vaultItems.size) { index ->
      val item = vaultItems[index]
      EvidenceCard(
        item = item,
        onDelete = { repository.deleteEvidence(item.id) }
      )
    }

    // Privacy Guarantee Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Evidence integrity policy", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "• No covert recording: a red indicator is always visible during recording.\n" +
            "• Every saved file includes a SHA-256 digital fingerprint to verify it has not been modified.\n" +
            "• You can export or permanently delete your files at any time.",
            color = CharcoalSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}

@Composable
private fun EvidenceCard(
  item: EvidenceItem,
  onDelete: () -> Unit
) {
  val dateFormatted = SimpleDateFormat("dd MMM, h:mm a", Locale.getDefault()).format(Date(item.timestamp))

  Card(
    colors = CardDefaults.cardColors(containerColor = WarmSurface),
    border = BorderStroke(1.dp, BorderSubtle),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(PaleBlush)
          ) {
            Icon(
              imageVector = if (item.type == EvidenceType.AUDIO) Icons.Default.Audiotrack else Icons.Default.Description,
              contentDescription = "Evidence Type",
              tint = SoftPinkDark,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(item.title, color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(dateFormatted, color = CharcoalMuted, fontSize = 11.sp)
          }
        }

        IconButton(onClick = onDelete) {
          Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = CharcoalMuted, modifier = Modifier.size(18.dp))
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(item.notes, color = CharcoalSecondary, fontSize = 12.sp)

      Spacer(modifier = Modifier.height(6.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("SHA-256: ${item.checksumSha256.take(10)}...", color = CharcoalMuted, fontSize = 10.sp)
        if (item.durationSeconds > 0) {
          Text("Duration: ${item.durationSeconds}s", color = SoftPinkDark, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
      }
    }
  }
}
