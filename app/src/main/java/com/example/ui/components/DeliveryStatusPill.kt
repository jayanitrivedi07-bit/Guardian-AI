package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryStatus
import com.example.ui.theme.*

@Composable
fun DeliveryStatusPill(
  status: DeliveryStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, borderColor, icon, label) = when (status) {
    DeliveryStatus.QUEUED -> Quintuple(
      WarningAmberSurface,
      WarningAmber,
      WarningAmber.copy(alpha = 0.3f),
      Icons.Default.HourglassTop,
      "Queued"
    )
    DeliveryStatus.SUBMITTED -> Quintuple(
      InfoSkySurface,
      InfoSky,
      InfoSky.copy(alpha = 0.3f),
      Icons.Default.Send,
      "Dispatched"
    )
    DeliveryStatus.DELIVERED -> Quintuple(
      SuccessGreenSurface,
      SuccessGreen,
      SuccessGreen.copy(alpha = 0.3f),
      Icons.Default.CheckCircle,
      "Delivered"
    )
    DeliveryStatus.ACKNOWLEDGED -> Quintuple(
      SuccessGreenSurface,
      SuccessGreen,
      SuccessGreen.copy(alpha = 0.4f),
      Icons.Default.Done,
      "Acknowledged"
    )
    DeliveryStatus.FAILED -> Quintuple(
      EmergencySurface,
      EmergencyRed,
      EmergencyRed.copy(alpha = 0.3f),
      Icons.Default.ErrorOutline,
      "Failed (Retrying)"
    )
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(bgColor)
      .border(1.dp, borderColor, RoundedCornerShape(6.dp))
      .padding(horizontal = 8.dp, vertical = 3.dp)
      .testTag("delivery_status_pill")
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = textColor,
      modifier = Modifier.size(12.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium
    )
  }
}

private data class Quintuple<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
