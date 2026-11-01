package com.example.data.model

enum class DeliveryStatus {
  QUEUED,
  SUBMITTED,
  DELIVERED,
  ACKNOWLEDGED,
  FAILED
}

enum class LocationFreshness {
  CURRENT,
  STALE,
  APPROXIMATE,
  UNAVAILABLE
}

enum class SessionStatus {
  ACTIVE,
  CANCELLED,
  RESOLVED
}

enum class EvidenceType {
  AUDIO,
  NOTE,
  PHOTO
}

enum class SupportCategory {
  EMERGENCY,
  WOMEN_HELPLINE,
  DOMESTIC_ABUSE,
  RAILWAY_SAFETY,
  LEGAL_AID
}

enum class AppLanguage {
  EN,
  HI,
  MR
}

data class LocationData(
  val latitude: Double = 19.0760, // Default Mumbai reference if GPS pending
  val longitude: Double = 72.8777,
  val accuracyMeters: Float = 12.5f,
  val timestamp: Long = System.currentTimeMillis(),
  val freshness: LocationFreshness = LocationFreshness.CURRENT,
  val approximateAddress: String = "Dr Annie Besant Rd, Worli, Mumbai"
) {
  val isStale: Boolean
    get() = (System.currentTimeMillis() - timestamp) > 5 * 60 * 1000L || freshness == LocationFreshness.STALE
}

data class DeliveryAttempt(
  val contactId: String,
  val contactName: String,
  val contactPhone: String,
  val channel: String = "SMS & Push",
  val status: DeliveryStatus = DeliveryStatus.QUEUED,
  val timestamp: Long = System.currentTimeMillis(),
  val latencyMs: Long = 0,
  val failureReason: String? = null
)

data class EmergencySession(
  val sessionId: String,
  val idempotencyKey: String,
  val ownerId: String = "user_primary",
  val isTest: Boolean = false,
  val status: SessionStatus = SessionStatus.ACTIVE,
  val startedAt: Long = System.currentTimeMillis(),
  val location: LocationData = LocationData(),
  val deliveryAttempts: List<DeliveryAttempt> = emptyList(),
  val cancellationReason: String? = null,
  val cancelledAt: Long? = null,
  val audioRecordedSeconds: Int = 0
)

data class TrustedContact(
  val id: String,
  val name: String,
  val phone: String,
  val relationship: String,
  val priorityOrder: Int,
  val isVerified: Boolean = true,
  val canReceiveSOS: Boolean = true,
  val canReceiveLiveLocation: Boolean = true,
  val canReceiveJourneyUpdates: Boolean = true
)

data class Journey(
  val id: String,
  val destination: String,
  val origin: String = "Current Location",
  val plannedRouteName: String,
  val expectedArrivalMinutes: Int,
  val checkInIntervalMinutes: Int = 15,
  val startedAt: Long = System.currentTimeMillis(),
  val lastCheckInAt: Long = System.currentTimeMillis(),
  val isSharingActive: Boolean = true,
  val deviations: List<String> = emptyList(),
  val isSafeArrivalConfirmed: Boolean = false
)

data class SaferRoute(
  val id: String,
  val name: String,
  val distanceKm: Double,
  val travelTimeMinutes: Int,
  val lightingRating: String, // "High (LED Corridors)", "Moderate"
  val policeBoothsOnPath: Int,
  val publicTransitNodes: Int,
  val safetyRationale: String,
  val sourceInfo: String,
  val isRecommended: Boolean = false
)

data class SupportContact(
  val id: String,
  val name: String,
  val number: String,
  val category: SupportCategory,
  val agency: String,
  val region: String, // "All India", "Maharashtra / Mumbai", "Delhi NCR"
  val officialSource: String,
  val verifiedDate: String = "Oct 2026",
  val isTollFree: Boolean = true
)

data class EvidenceItem(
  val id: String,
  val title: String,
  val timestamp: Long = System.currentTimeMillis(),
  val type: EvidenceType,
  val durationSeconds: Int = 0,
  val sizeBytes: Long = 0,
  val notes: String = "",
  val checksumSha256: String = "e3b0c44298fc1c149afbf4c8996fb924",
  val isEncrypted: Boolean = true
)

data class TimelineEvent(
  val timeFormatted: String,
  val description: String,
  val isRecordedFact: Boolean = true // true = sensor/server event; false = recollection
)

data class IncidentReport(
  val id: String,
  val title: String,
  val incidentDate: String,
  val locationText: String,
  val timeline: List<TimelineEvent> = emptyList(),
  val userRecollection: String = "",
  val aiDraftSummary: String = "",
  val isDraft: Boolean = true,
  val exportedAt: Long? = null
)

data class SafetyPlan(
  val emergencyNotes: String = "Carry inhaler in front pocket. Blood group B+.",
  val preferredTransport: String = "Metro / Registered Pre-paid Taxi",
  val emergencySmsTemplate: String = "EMERGENCY: I need help immediately. My latest GPS location is attached. Please check Guardian AI.",
  val homeAddress: String = "Bandra West, Mumbai",
  val workAddress: String = "Nariman Point, Mumbai"
)

data class PrivacySettings(
  val shareLiveLocationWithCircle: Boolean = true,
  val autoStartAudioRecordOnSos: Boolean = false,
  val retainLocationHistoryDays: Int = 7,
  val allowAiCompanionAccess: Boolean = true,
  val enableOfflineQueue: Boolean = true,
  val lowConnectivitySmsFallback: Boolean = true
)

data class GroundedAiSource(
  val title: String,
  val publisher: String,
  val verifiedDate: String,
  val keyTakeaway: String,
  val officialUrl: String
)
