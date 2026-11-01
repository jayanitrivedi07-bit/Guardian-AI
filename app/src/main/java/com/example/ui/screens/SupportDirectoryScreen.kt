package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SupportCategory
import com.example.data.model.SupportContact
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*

@Composable
fun SupportDirectoryScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedCategory by remember { mutableStateOf<SupportCategory?>(null) }
  val allContacts = repository.verifiedSupportDirectory

  val filteredContacts = remember(selectedCategory) {
    if (selectedCategory == null) allContacts
    else allContacts.filter { it.category == selectedCategory }
  }

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
          text = "Emergency helplines",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Official government & statutory support numbers in India",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // Category Filter Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedCategory == null,
            onClick = { selectedCategory = null },
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = PaleBlush,
              selectedLabelColor = SoftPinkDark,
              containerColor = WarmSurface,
              labelColor = CharcoalSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              borderColor = BorderSubtle,
              selectedBorderColor = SoftPink,
              enabled = true,
              selected = selectedCategory == null
            )
          )
        }
        items(SupportCategory.values()) { category ->
          val isSelected = selectedCategory == category
          FilterChip(
            selected = isSelected,
            onClick = { selectedCategory = category },
            label = {
              Text(
                when (category) {
                  SupportCategory.EMERGENCY -> "Emergency (112)"
                  SupportCategory.WOMEN_HELPLINE -> "Women (1091)"
                  SupportCategory.DOMESTIC_ABUSE -> "Sakhi (181)"
                  SupportCategory.RAILWAY_SAFETY -> "Railway (139)"
                  SupportCategory.LEGAL_AID -> "Legal aid (15100)"
                }
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = PaleBlush,
              selectedLabelColor = SoftPinkDark,
              containerColor = WarmSurface,
              labelColor = CharcoalSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              borderColor = BorderSubtle,
              selectedBorderColor = SoftPink,
              enabled = true,
              selected = isSelected
            )
          )
        }
      }
    }

    // Directory Contact Cards
    items(filteredContacts.size) { index ->
      val contact = filteredContacts[index]
      SupportContactCard(
        contact = contact,
        onCall = {
          val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.number}"))
          context.startActivity(intent)
        }
      )
    }

    // Verification Invariant Disclaimer
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Directory verification policy", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "• Numbers are verified against official Government of India gazettes.\n" +
            "• 112 is the single pan-India emergency number integrating Police, Fire, and Ambulance.\n" +
            "• Last verification audit: October 2026.",
            color = CharcoalSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
          )
        }
      }
    }
  }
}

@Composable
private fun SupportContactCard(
  contact: SupportContact,
  onCall: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = WarmSurface),
    border = BorderStroke(1.dp, BorderSubtle),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(contact.name, color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
          Spacer(modifier = Modifier.width(4.dp))
          Icon(Icons.Default.Verified, contentDescription = "Verified official source", tint = SuccessGreen, modifier = Modifier.size(13.dp))
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(contact.agency, color = CharcoalSecondary, fontSize = 11.sp)
        Text("Coverage: ${contact.region}", color = CharcoalMuted, fontSize = 10.sp)

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = contact.number,
          color = if (contact.number == "112") EmergencyRed else SoftPinkDark,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }

      OutlinedButton(
        onClick = onCall,
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = if (contact.number == "112") EmergencyRed else SoftPinkDark
        ),
        border = BorderStroke(1.dp, if (contact.number == "112") EmergencyRed.copy(alpha = 0.5f) else SoftPink),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        modifier = Modifier
          .height(36.dp)
          .testTag("call_${contact.number}")
      ) {
        Icon(Icons.Default.Call, contentDescription = "Call Helpline", modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Call", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
      }
    }
  }
}
