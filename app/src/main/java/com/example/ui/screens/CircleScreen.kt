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
import com.example.data.model.TrustedContact
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*

@Composable
fun CircleScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val contacts by repository.trustedContacts.collectAsState()
  var showAddDialog by remember { mutableStateOf(false) }
  var newName by remember { mutableStateOf("") }
  var newPhone by remember { mutableStateOf("") }
  var newRelationship by remember { mutableStateOf("Family") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 96.dp, top = 6.dp)
  ) {
    // Header
    item {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
      ) {
        Column {
          Text(
            text = "Safety circle",
            color = CharcoalPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.3).sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Contacts who receive your alerts and journey updates",
            color = CharcoalSecondary,
            fontSize = 13.sp
          )
        }

        Button(
          onClick = { showAddDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .height(36.dp)
            .testTag("add_contact_button")
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Contact", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // Contacts List
    items(contacts.size) { index ->
      val contact = contacts[index]
      ContactItemCard(
        contact = contact,
        onRemove = { repository.removeContact(contact.id) }
      )
    }

    // Escalation Principles
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("How alerts reach your circle", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "• Primary contacts (#1 and #2) receive immediate SMS and push notifications upon SOS activation.\n" +
            "• If an alert remains unacknowledged after 60 seconds, it escalates to secondary contacts.\n" +
            "• Location sharing terminates immediately once you end your journey or cancel an alert.",
            color = CharcoalSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }
  }

  // Add Contact Modal Dialog
  if (showAddDialog) {
    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Add trusted contact", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Full name") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newPhone,
            onValueChange = { newPhone = it },
            label = { Text("Phone number (+91)") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = newRelationship,
            onValueChange = { newRelationship = it },
            label = { Text("Relationship (e.g. Sister, Partner)") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newName.isNotBlank() && newPhone.isNotBlank()) {
              repository.addContact(newName, newPhone, newRelationship)
              newName = ""
              newPhone = ""
              showAddDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Save contact")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel", color = CharcoalSecondary)
        }
      }
    )
  }
}

@Composable
private fun ContactItemCard(
  contact: TrustedContact,
  onRemove: () -> Unit
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
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(PaleBlush)
        ) {
          Text(
            text = "#${contact.priorityOrder}",
            color = SoftPinkDark,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(contact.name, color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            if (contact.isVerified) {
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                Icons.Default.Verified,
                contentDescription = "Verified contact",
                tint = SuccessGreen,
                modifier = Modifier.size(14.dp)
              )
            }
          }
          Text("${contact.relationship} • ${contact.phone}", color = CharcoalSecondary, fontSize = 11.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SoftBadgeTag("SOS alerts")
            SoftBadgeTag("Live location")
          }
        }
      }

      IconButton(onClick = onRemove) {
        Icon(Icons.Default.DeleteOutline, contentDescription = "Remove contact", tint = CharcoalMuted)
      }
    }
  }
}

@Composable
private fun SoftBadgeTag(text: String) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(4.dp))
      .background(LightPinkSurface)
      .padding(horizontal = 6.dp, vertical = 2.dp)
  ) {
    Text(text, color = SoftPinkDark, fontSize = 9.sp, fontWeight = FontWeight.Medium)
  }
}
