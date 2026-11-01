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
import com.example.data.model.SaferRoute
import com.example.data.repository.GuardianRepository
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun JourneyScreen(
  repository: GuardianRepository,
  modifier: Modifier = Modifier
) {
  val activeJourney by repository.activeJourney.collectAsState()
  var destinationInput by remember { mutableStateOf("Bandra Kurla Complex (BKC)") }
  var selectedRouteId by remember { mutableStateOf("r1") }
  val routes = repository.availableRoutes

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
          text = "Journey sharing",
          color = CharcoalPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = (-0.3).sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Share planned route and arrival time with your safety circle",
          color = CharcoalSecondary,
          fontSize = 13.sp
        )
      }
    }

    // ACTIVE JOURNEY CARD
    if (activeJourney != null) {
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = WarmSurface),
          border = BorderStroke(1.5.dp, SoftPink),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("active_journey_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Navigation, contentDescription = "Active Journey", tint = SoftPinkDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Journey in progress", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(PaleBlush)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text("Sharing active", color = SoftPinkDark, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("To: ${activeJourney!!.destination}", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text("Route: ${activeJourney!!.plannedRouteName}", color = CharcoalSecondary, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))
            val lastCheckInFormatted = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(activeJourney!!.lastCheckInAt))
            Text("Last check-in at $lastCheckInFormatted", color = CharcoalMuted, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
              OutlinedButton(
                onClick = { repository.checkInJourney() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CharcoalPrimary),
                border = BorderStroke(1.dp, BorderSubtle),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Check, contentDescription = "I am safe", tint = SuccessGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("I'm safe", fontSize = 12.sp)
              }

              Button(
                onClick = { repository.confirmSafeArrival() },
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("I have arrived", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
              onClick = { repository.endJourney() },
              modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
              Text("End journey sharing", color = CharcoalSecondary, fontSize = 11.sp)
            }
          }
        }
      }
    } else {
      // START NEW JOURNEY
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = WarmSurface),
          border = BorderStroke(1.dp, BorderSubtle),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("Plan journey", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = destinationInput,
              onValueChange = { destinationInput = it },
              label = { Text("Destination landmark or address") },
              leadingIcon = { Icon(Icons.Default.Place, contentDescription = "Destination", tint = SoftPinkDark, modifier = Modifier.size(18.dp)) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text("Route alternatives", color = CharcoalPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(8.dp))
            routes.forEach { route ->
              val isSelected = route.id == selectedRouteId
              RouteOptionItem(
                route = route,
                isSelected = isSelected,
                onSelect = { selectedRouteId = route.id }
              )
              Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
              onClick = {
                val selectedRoute = routes.find { it.id == selectedRouteId } ?: routes.first()
                repository.startJourney(
                  destination = destinationInput,
                  routeName = selectedRoute.name,
                  arrivalMinutes = selectedRoute.travelTimeMinutes
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = SoftPinkDark),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("start_journey_button")
            ) {
              Icon(Icons.Default.ShareLocation, contentDescription = "Start Journey", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Start journey & share with circle", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }
        }
      }
    }

    // JOURNEY GUIDELINES CARD
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = WarmSurface),
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Route information notes", color = CharcoalPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            "• Route characteristics are based on verified street lighting surveys and official police chowki locations.\n" +
            "• We never claim a route is guaranteed to be safe, but help you choose well-lit paths.\n" +
            "• Taking a detour is treated as neutral and will not trigger false emergency alarms.",
            color = CharcoalSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}

@Composable
private fun RouteOptionItem(
  route: SaferRoute,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
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
      .clickable { onSelect() }
      .padding(12.dp)
  ) {
    Column {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = route.name,
          color = CharcoalPrimary,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          modifier = Modifier.weight(1f)
        )
        if (route.isRecommended) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(PaleBlush)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text("Well-lit", color = SoftPinkDark, fontSize = 10.sp, fontWeight = FontWeight.Medium)
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "${route.distanceKm} km • ~${route.travelTimeMinutes} mins • ${route.policeBoothsOnPath} police chowkis",
        color = CharcoalSecondary,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = route.safetyRationale,
        color = CharcoalSecondary,
        fontSize = 11.sp
      )
    }
  }
}
