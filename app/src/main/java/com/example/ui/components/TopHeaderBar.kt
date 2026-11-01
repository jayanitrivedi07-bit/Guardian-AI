package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

@Composable
fun TopHeaderBar(
  currentLanguage: AppLanguage,
  isTestMode: Boolean,
  onLanguageSelected: (AppLanguage) -> Unit,
  onToggleTestMode: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showLangMenu by remember { mutableStateOf(false) }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 14.dp)
  ) {
    // Brand Logo & Understated Title
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(38.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(PaleBlush)
          .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = "Guardian AI",
          tint = SoftPinkDark,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Guardian AI",
            color = CharcoalPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            letterSpacing = (-0.3).sp
          )
          if (isTestMode) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(WarningAmberSurface)
                .border(1.dp, WarningAmber.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
              Text(
                text = "TEST MODE",
                color = WarningAmber,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
              )
            }
          }
        }
        Text(
          text = when (currentLanguage) {
            AppLanguage.EN -> "Personal safety companion"
            AppLanguage.HI -> "व्यक्तिगत सुरक्षा साथी"
            AppLanguage.MR -> "वैयक्तिक सुरक्षा साथी"
          },
          color = CharcoalSecondary,
          fontSize = 11.sp
        )
      }
    }

    // Top Right Controls (Language & Test Mode)
    Row(verticalAlignment = Alignment.CenterVertically) {
      // Language Selector Pill
      Box {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(WarmSurface)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .clickable { showLangMenu = true }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("language_selector")
        ) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = CharcoalSecondary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = when (currentLanguage) {
              AppLanguage.EN -> "EN"
              AppLanguage.HI -> "हिन्दी"
              AppLanguage.MR -> "मराठी"
            },
            color = CharcoalPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        DropdownMenu(
          expanded = showLangMenu,
          onDismissRequest = { showLangMenu = false }
        ) {
          DropdownMenuItem(
            text = { Text("English (EN)") },
            onClick = {
              onLanguageSelected(AppLanguage.EN)
              showLangMenu = false
            }
          )
          DropdownMenuItem(
            text = { Text("हिंदी (Hindi)") },
            onClick = {
              onLanguageSelected(AppLanguage.HI)
              showLangMenu = false
            }
          )
          DropdownMenuItem(
            text = { Text("मराठी (Marathi)") },
            onClick = {
              onLanguageSelected(AppLanguage.MR)
              showLangMenu = false
            }
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Test Mode Button
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(14.dp))
          .background(if (isTestMode) WarningAmberSurface else WarmSurface)
          .border(
            1.dp,
            if (isTestMode) WarningAmber.copy(alpha = 0.5f) else BorderSubtle,
            RoundedCornerShape(14.dp)
          )
          .clickable { onToggleTestMode() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("test_mode_toggle")
      ) {
        Text(
          text = if (isTestMode) "Exit Test" else "Test",
          color = if (isTestMode) WarningAmber else CharcoalSecondary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}
