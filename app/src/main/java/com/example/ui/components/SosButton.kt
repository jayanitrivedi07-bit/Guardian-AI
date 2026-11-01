package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SosButton(
  isTestMode: Boolean = false,
  onActivated: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isPressing by remember { mutableStateOf(false) }
  var holdProgress by remember { mutableStateOf(0f) }

  val animatedProgress by animateFloatAsState(
    targetValue = holdProgress,
    animationSpec = tween(durationMillis = 80),
    label = "sosProgress"
  )

  LaunchedEffect(isPressing) {
    if (isPressing) {
      val startTime = System.currentTimeMillis()
      val duration = 2000L // 2 seconds deliberate hold
      while (isPressing && (System.currentTimeMillis() - startTime) < duration) {
        val elapsed = System.currentTimeMillis() - startTime
        holdProgress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
        delay(30)
      }
      if (isPressing && holdProgress >= 0.98f) {
        holdProgress = 1f
        onActivated()
        isPressing = false
        holdProgress = 0f
      }
    } else {
      holdProgress = 0f
    }
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.fillMaxWidth()
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(196.dp)
        .testTag("sos_button")
        .pointerInput(Unit) {
          detectTapGestures(
            onPress = {
              isPressing = true
              tryAwaitRelease()
              isPressing = false
            }
          )
        }
    ) {
      // Soft shadow
      Box(
        modifier = Modifier
          .size(178.dp)
          .shadow(
            elevation = if (isPressing) 12.dp else 6.dp,
            shape = CircleShape,
            spotColor = Color(0x33C92A2A)
          )
      )

      // Outer Progress Ring Canvas
      Canvas(modifier = Modifier.size(192.dp)) {
        // Track
        drawCircle(
          color = Color(0x22C92A2A),
          style = Stroke(width = 6.dp.toPx())
        )
        // Progress sweep
        if (animatedProgress > 0f) {
          drawArc(
            color = if (isTestMode) WarningAmber else EmergencyRed,
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
          )
        }
      }

      // High-contrast, serious emergency SOS button (unmistakable, never pale pink)
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(164.dp)
          .clip(CircleShape)
          .background(
            brush = if (isTestMode) {
              Brush.linearGradient(listOf(Color(0xFFE67E22), Color(0xFFD35400)))
            } else {
              Brush.linearGradient(listOf(EmergencyRed, EmergencyRedDark))
            }
          )
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Emergency Alert",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isTestMode) "TEST SOS" else "EMERGENCY\nSOS",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))
    Text(
      text = if (isPressing) "Keep holding (${(animatedProgress * 100).toInt()}%)" else "Hold for 2 seconds to activate",
      color = if (isPressing) EmergencyRed else CharcoalSecondary,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium
    )
  }
}
