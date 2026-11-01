package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationData
import com.example.data.model.LocationFreshness
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StaleLocationBanner(
  location: LocationData,
  modifier: Modifier = Modifier
) {
  if (!location.isStale && location.freshness == LocationFreshness.CURRENT) {
    return
  }

  val isStale = location.isStale || location.freshness == LocationFreshness.STALE
  val timeFormatted = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(location.timestamp))

  val bgColor = if (isStale) WarningAmberSurface else InfoSkySurface
  val accentColor = if (isStale) WarningAmber else InfoSky
  val title = if (isStale) "Location fix is over 5 minutes old" else "Approximate location"
  val desc = if (isStale) {
    "Recorded at $timeFormatted. Looking for a fresh GPS signal."
  } else {
    "Accuracy ±${location.accuracyMeters.toInt()}m. Refining satellite coordinates."
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(bgColor)
      .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
      .padding(12.dp)
  ) {
    Icon(
      imageVector = if (isStale) Icons.Default.Warning else Icons.Default.Info,
      contentDescription = title,
      tint = accentColor,
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = title,
        color = CharcoalPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
      )
      Text(
        text = desc,
        color = CharcoalSecondary,
        fontSize = 11.sp,
        lineHeight = 15.sp
      )
    }
  }
}
