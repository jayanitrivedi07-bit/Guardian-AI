package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.repository.GuardianRepository
import com.example.ui.components.RecordingIndicatorBar
import com.example.ui.components.TopHeaderBar
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

  private val repository = GuardianRepository()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      GuardianAiTheme {
        GuardianApp(repository = repository)
      }
    }
  }
}

@Composable
fun GuardianApp(repository: GuardianRepository) {
  var selectedTab by remember { mutableStateOf(0) }
  var vaultSubTab by remember { mutableStateOf(0) } // 0: Vault, 1: Fake Call, 2: Report, 3: Privacy, 4: Test Mode
  var directorySubTab by remember { mutableStateOf(0) } // 0: Directory, 1: AI Companion

  var isTestMode by remember { mutableStateOf(false) }

  val currentLanguage by repository.currentLanguage.collectAsState()
  val isRecording by repository.isRecordingAudio.collectAsState()
  val recordingSeconds by repository.recordingSeconds.collectAsState()

  // Handle Android Back button
  BackHandler(enabled = selectedTab != 0 || vaultSubTab != 0 || directorySubTab != 0) {
    if (selectedTab == 4 && vaultSubTab != 0) {
      vaultSubTab = 0
    } else if (selectedTab == 3 && directorySubTab != 0) {
      directorySubTab = 0
    } else {
      selectedTab = 0
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = WarmBackground,
    topBar = {
      Column(
        modifier = Modifier
          .background(WarmBackground)
          .statusBarsPadding()
      ) {
        TopHeaderBar(
          currentLanguage = currentLanguage,
          isTestMode = isTestMode,
          onLanguageSelected = { repository.setLanguage(it) },
          onToggleTestMode = { isTestMode = !isTestMode }
        )

        // Persistent Recording Indicator Bar
        RecordingIndicatorBar(
          isRecording = isRecording,
          elapsedSeconds = recordingSeconds,
          onStop = { repository.stopRecordingEvidence() },
          modifier = Modifier.padding(horizontal = 18.dp, vertical = 2.dp)
        )
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = WarmSurface,
        tonalElevation = 0.dp,
        modifier = Modifier
          .border(width = 1.dp, color = BorderSubtle)
          .navigationBarsPadding()
          .testTag("bottom_nav_bar")
      ) {
        // Tab 0: Dashboard (SOS)
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          icon = { Icon(Icons.Default.Security, contentDescription = "Dashboard", modifier = Modifier.size(20.dp)) },
          label = { Text("SOS", fontSize = 11.sp, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SoftPinkDark,
            selectedTextColor = SoftPinkDark,
            indicatorColor = PaleBlush,
            unselectedIconColor = CharcoalMuted,
            unselectedTextColor = CharcoalMuted
          )
        )

        // Tab 1: Journey
        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          icon = { Icon(Icons.Default.Navigation, contentDescription = "Journey", modifier = Modifier.size(20.dp)) },
          label = { Text("Journey", fontSize = 11.sp, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SoftPinkDark,
            selectedTextColor = SoftPinkDark,
            indicatorColor = PaleBlush,
            unselectedIconColor = CharcoalMuted,
            unselectedTextColor = CharcoalMuted
          )
        )

        // Tab 2: Circle
        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          icon = { Icon(Icons.Default.People, contentDescription = "Circle", modifier = Modifier.size(20.dp)) },
          label = { Text("Circle", fontSize = 11.sp, fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SoftPinkDark,
            selectedTextColor = SoftPinkDark,
            indicatorColor = PaleBlush,
            unselectedIconColor = CharcoalMuted,
            unselectedTextColor = CharcoalMuted
          )
        )

        // Tab 3: Helplines & AI
        NavigationBarItem(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          icon = { Icon(Icons.Default.ContactPhone, contentDescription = "Support & AI", modifier = Modifier.size(20.dp)) },
          label = { Text("Helpline", fontSize = 11.sp, fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SoftPinkDark,
            selectedTextColor = SoftPinkDark,
            indicatorColor = PaleBlush,
            unselectedIconColor = CharcoalMuted,
            unselectedTextColor = CharcoalMuted
          )
        )

        // Tab 4: Vault & Tools
        NavigationBarItem(
          selected = selectedTab == 4,
          onClick = { selectedTab = 4 },
          icon = { Icon(Icons.Default.Apps, contentDescription = "Vault & Tools", modifier = Modifier.size(20.dp)) },
          label = { Text("Vault", fontSize = 11.sp, fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = SoftPinkDark,
            selectedTextColor = SoftPinkDark,
            indicatorColor = PaleBlush,
            unselectedIconColor = CharcoalMuted,
            unselectedTextColor = CharcoalMuted
          )
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .background(WarmBackground)
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        0 -> DashboardScreen(
          repository = repository,
          isTestMode = isTestMode,
          onNavigateToTab = { selectedTab = it }
        )
        1 -> JourneyScreen(repository = repository)
        2 -> CircleScreen(repository = repository)
        3 -> {
          Column(modifier = Modifier.fillMaxSize()) {
            // Sub-nav for Directory vs Grounded AI Companion
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              SoftSubTabPill(
                title = "Verified Helplines",
                isSelected = directorySubTab == 0,
                onClick = { directorySubTab = 0 },
                modifier = Modifier.weight(1f)
              )
              SoftSubTabPill(
                title = "Grounded Guide",
                isSelected = directorySubTab == 1,
                onClick = { directorySubTab = 1 },
                modifier = Modifier.weight(1f)
              )
            }

            if (directorySubTab == 0) {
              SupportDirectoryScreen(repository = repository)
            } else {
              AiCompanionScreen(repository = repository)
            }
          }
        }
        4 -> {
          Column(modifier = Modifier.fillMaxSize()) {
            // Sub-nav for Vault & Tools
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              SoftSubTabPill("Vault", vaultSubTab == 0, { vaultSubTab = 0 }, Modifier.weight(1f))
              SoftSubTabPill("Fake Call", vaultSubTab == 1, { vaultSubTab = 1 }, Modifier.weight(1f))
              SoftSubTabPill("Report", vaultSubTab == 2, { vaultSubTab = 2 }, Modifier.weight(1f))
              SoftSubTabPill("Privacy", vaultSubTab == 3, { vaultSubTab = 3 }, Modifier.weight(1f))
              SoftSubTabPill("Test", vaultSubTab == 4, { vaultSubTab = 4 }, Modifier.weight(1f))
            }

            when (vaultSubTab) {
              0 -> EvidenceVaultScreen(repository = repository)
              1 -> FakeCallScreen()
              2 -> IncidentReportScreen(repository = repository)
              3 -> PrivacyCentreScreen(repository = repository)
              4 -> TestModeScreen(
                repository = repository,
                isTestMode = isTestMode,
                onToggleTestMode = { isTestMode = !isTestMode }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SoftSubTabPill(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) PaleBlush else WarmSurface)
      .border(1.dp, if (isSelected) SoftPink else BorderSubtle, RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(vertical = 7.dp)
  ) {
    Text(
      text = title,
      color = if (isSelected) SoftPinkDark else CharcoalSecondary,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
    )
  }
}
