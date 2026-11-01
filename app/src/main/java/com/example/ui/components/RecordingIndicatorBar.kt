package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmergencyRed

@Composable
fun RecordingIndicatorBar(
  isRecording: Boolean,
  elapsedSeconds: Int,
  onStop: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(visible = isRecording) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
      initialValue = 0.4f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(
        animation = tween(600, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "recordBlink"
    )

    val minutes = elapsedSeconds / 60
    val secs = elapsedSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, secs)

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(EmergencyRed)
        .padding(horizontal = 14.dp, vertical = 10.dp)
        .testTag("recording_indicator_bar")
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.Default.Mic,
          contentDescription = "Active Recording",
          tint = Color.White,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Recording audio evidence",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Duration: $timeFormatted (saved securely on device)",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.sp
          )
        }
      }

      Button(
        onClick = onStop,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = EmergencyRed
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        modifier = Modifier.height(32.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Stop,
          contentDescription = "Stop Recording",
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Stop",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
