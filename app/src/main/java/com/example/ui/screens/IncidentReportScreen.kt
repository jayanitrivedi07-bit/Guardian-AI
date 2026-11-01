package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*

@Composable
fun IncidentReportScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val reports by repository.incidentReports.collectAsState()
  val sampleReport = reports.firstOrNull()

  var userNotes by remember { mutableStateOf(sampleReport?.userRecollection ?: "") }

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
          text = "Incident timeline & report",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Assemble timestamps, location history, and personal notes into an editable draft",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // Report Summary Details
    if (sampleReport != null) {
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
              Text(sampleReport.title, color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(PaleBlush)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text("Draft only", color = SoftPinkDark, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Date: ${sampleReport.incidentDate}", color = CharcoalSecondary, fontSize = 12.sp)
            Text("Location: ${sampleReport.locationText}", color = CharcoalSecondary, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(14.dp))
            Text("Chronological timeline", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(8.dp))
            sampleReport.timeline.forEach { event ->
              Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (event.isRecordedFact) SuccessGreenSurface else InfoSkySurface)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = event.timeFormatted,
                    color = if (event.isRecordedFact) SuccessGreen else InfoSky,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(event.description, color = CharcoalPrimary, fontSize = 12.sp)
                  Text(
                    if (event.isRecordedFact) "Verified sensor/server timestamp" else "User recollection",
                    color = CharcoalMuted,
                    fontSize = 10.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Your notes & recollection (editable)", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
              value = userNotes,
              onValueChange = { userNotes = it },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth(),
              minLines = 3
            )

            Spacer(modifier = Modifier.height(14.dp))
            Button(
              onClick = {
                val sendIntent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(
                    Intent.EXTRA_TEXT,
                    "GUARDIAN AI - DRAFT INCIDENT REPORT\n\n" +
                    "Title: ${sampleReport.title}\n" +
                    "Date: ${sampleReport.incidentDate}\n" +
                    "Location: ${sampleReport.locationText}\n\n" +
                    "Notes: $userNotes\n\n" +
                    "(Draft compiled privately via Guardian AI)"
                  )
                  type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Export draft incident report"))
              },
              colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("export_report_button")
            ) {
              Icon(Icons.Default.Share, contentDescription = "Export report", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Export draft report", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }
        }
      }
    }

    // Invariant Notice
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Privacy and legal disclosure", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "• Draft reports are never automatically filed with police, employers, or third parties.\n" +
            "• You have complete control to edit, review, and choose whether to export this information.\n" +
            "• Serves as a personal record of events, not a formal legal deposition.",
            color = CharcoalSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}
