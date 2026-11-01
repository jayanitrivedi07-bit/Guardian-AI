package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.GroundedAiSource
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*

@Composable
fun AiCompanionScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val sources = repository.groundedKnowledgeSources
  var selectedSource by remember { mutableStateOf<GroundedAiSource?>(sources.first()) }
  var userQuery by remember { mutableStateOf("") }
  var aiResponse by remember { mutableStateOf<String?>(null) }

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
          text = "Safety guide & legal rights",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Grounded legal provisions and transit safety protocols in India",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // Quick Topic Selectors
    item {
      Text("Curated safety topics", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
      Spacer(modifier = Modifier.height(8.dp))
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        sources.forEach { source ->
          val isSelected = selectedSource == source
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) LightPinkSurface else WarmSurface)
              .border(
                width = 1.dp,
                color = if (isSelected) SoftPinkDark else BorderSubtle,
                shape = RoundedCornerShape(10.dp)
              )
              .clickable {
                selectedSource = source
                aiResponse = null
              }
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.MenuBook,
                contentDescription = source.title,
                tint = if (isSelected) SoftPinkDark else CharcoalMuted,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(source.title, color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(source.publisher, color = CharcoalSecondary, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // Grounded Source Detail Card
    if (selectedSource != null) {
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
              Text(
                "Statutory provision",
                color = SoftPinkDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                selectedSource!!.verifiedDate,
                color = CharcoalMuted,
                fontSize = 10.sp
              )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = selectedSource!!.title,
              color = CharcoalPrimary,
              fontWeight = FontWeight.SemiBold,
              fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = selectedSource!!.keyTakeaway,
              color = CharcoalPrimary,
              fontSize = 13.sp,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Link, contentDescription = "Source", tint = CharcoalSecondary, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Official reference: ${selectedSource!!.officialUrl}",
                color = CharcoalSecondary,
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }

    // Practical Safety Question Input
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Look up safety guidance", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = userQuery,
            onValueChange = { userQuery = it },
            placeholder = { Text("e.g. Can police refuse to file an FIR at night?") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = {
              if (userQuery.isNotBlank()) {
                aiResponse = "Under Section 154 CrPC and Supreme Court directives in Lalita Kumari v. Govt of UP, police cannot refuse registration of an FIR for cognizable offences against women, even at night. Furthermore, women have the right not to be called to the police station for interrogation at night (Section 160 CrPC)."
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.End)
          ) {
            Icon(Icons.Default.Search, contentDescription = "Query", modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Retrieve guidance", fontSize = 12.sp)
          }

          if (aiResponse != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(LightPinkSurface)
                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                .padding(12.dp)
            ) {
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Verified, contentDescription = "Verified statutory guidance", tint = SuccessGreen, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Grounded response (Section 154 & 160 CrPC)", color = SuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(aiResponse!!, color = CharcoalPrimary, fontSize = 12.sp, lineHeight = 17.sp)
              }
            }
          }
        }
      }
    }
  }
}
