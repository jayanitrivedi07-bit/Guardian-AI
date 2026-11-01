package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GuardianRepository {

  private val coroutineScope = CoroutineScope(Dispatchers.Default)

  // 1. Language state
  private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  fun setLanguage(lang: AppLanguage) {
    _currentLanguage.value = lang
  }

  // 2. Active Emergency Session
  private val _activeEmergencySession = MutableStateFlow<EmergencySession?>(null)
  val activeEmergencySession: StateFlow<EmergencySession?> = _activeEmergencySession.asStateFlow()

  // 3. Emergency Sessions History (Auditable)
  private val _emergencyHistory = MutableStateFlow<List<EmergencySession>>(emptyList())
  val emergencyHistory: StateFlow<List<EmergencySession>> = _emergencyHistory.asStateFlow()

  // 4. Trusted Safety Circle
  private val _trustedContacts = MutableStateFlow(
    listOf(
      TrustedContact(
        id = "c1",
        name = "Pooja Sharma",
        phone = "+91 98201 12345",
        relationship = "Sister",
        priorityOrder = 1,
        isVerified = true
      ),
      TrustedContact(
        id = "c2",
        name = "Dr. Rohan Patel",
        phone = "+91 98190 67890",
        relationship = "Partner",
        priorityOrder = 2,
        isVerified = true
      ),
      TrustedContact(
        id = "c3",
        name = "Ananya Sen",
        phone = "+91 98205 54321",
        relationship = "Colleague / Friend",
        priorityOrder = 3,
        isVerified = true
      )
    )
  )
  val trustedContacts: StateFlow<List<TrustedContact>> = _trustedContacts.asStateFlow()

  // 5. Current Location State
  private val _currentLocation = MutableStateFlow(
    LocationData(
      latitude = 19.0760,
      longitude = 72.8777,
      accuracyMeters = 14f,
      timestamp = System.currentTimeMillis(),
      freshness = LocationFreshness.CURRENT,
      approximateAddress = "Bandra-Worli Sea Link Rd, Worli, Mumbai"
    )
  )
  val currentLocation: StateFlow<LocationData> = _currentLocation.asStateFlow()

  // 6. Journey State
  private val _activeJourney = MutableStateFlow<Journey?>(null)
  val activeJourney: StateFlow<Journey?> = _activeJourney.asStateFlow()

  // 7. Route Alternatives
  val availableRoutes = listOf(
    SaferRoute(
      id = "r1",
      name = "Main Arterial via S.V. Road & Police Post",
      distanceKm = 8.4,
      travelTimeMinutes = 24,
      lightingRating = "High (LED Lit Corridor)",
      policeBoothsOnPath = 3,
      publicTransitNodes = 4,
      safetyRationale = "Continuous street lighting, 3 active police chowkis, high public bus frequency.",
      sourceInfo = "Mumbai Traffic Police & BMC Geo-Registry (Verified)",
      isRecommended = true
    ),
    SaferRoute(
      id = "r2",
      name = "Coastal Expressway Corridor",
      distanceKm = 10.2,
      travelTimeMinutes = 22,
      lightingRating = "High (Highway Illuminators)",
      policeBoothsOnPath = 2,
      publicTransitNodes = 1,
      safetyRationale = "CCTV monitored tollway with dedicated emergency patrol vehicles.",
      sourceInfo = "MSRDC Highway Safety Patrol Data",
      isRecommended = false
    ),
    SaferRoute(
      id = "r3",
      name = "Interior Back-Lane Shortcut",
      distanceKm = 6.8,
      travelTimeMinutes = 26,
      lightingRating = "Moderate to Low",
      policeBoothsOnPath = 0,
      publicTransitNodes = 0,
      safetyRationale = "Narrow residential roads with limited visibility and no active police posts.",
      sourceInfo = "Civic Lighting Audit",
      isRecommended = false
    )
  )

  // 8. Verified Support Directory (Official Indian Helplines)
  val verifiedSupportDirectory = listOf(
    SupportContact(
      id = "d1",
      name = "National Emergency Number",
      number = "112",
      category = SupportCategory.EMERGENCY,
      agency = "Ministry of Home Affairs (ERSS)",
      region = "All India",
      officialSource = "https://112.gov.in",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d2",
      name = "Women in Distress Helpline",
      number = "1091",
      category = SupportCategory.WOMEN_HELPLINE,
      agency = "National Commission for Women (NCW)",
      region = "All India",
      officialSource = "https://ncw.nic.in",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d3",
      name = "Sakhi One-Stop Centre Helpline",
      number = "181",
      category = SupportCategory.DOMESTIC_ABUSE,
      agency = "Ministry of Women & Child Development",
      region = "All India",
      officialSource = "https://wcd.nic.in/schemes/one-stop-centre-scheme",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d4",
      name = "Railway Passenger & Women Safety",
      number = "139",
      category = SupportCategory.RAILWAY_SAFETY,
      agency = "Indian Railways Security / RPF",
      region = "All India",
      officialSource = "https://indianrailways.gov.in",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d5",
      name = "Women Power Line (UP)",
      number = "1090",
      category = SupportCategory.WOMEN_HELPLINE,
      agency = "State Police Cyber & Harassment Cell",
      region = "Uttar Pradesh & North",
      officialSource = "https://1090up.in",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d6",
      name = "Mumbai Police Women Safety Cell",
      number = "022-26592700",
      category = SupportCategory.WOMEN_HELPLINE,
      agency = "Mumbai Police Crime Against Women Cell",
      region = "Maharashtra / Mumbai",
      officialSource = "https://mumbaipolice.gov.in",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d7",
      name = "Delhi Police SPUWAC",
      number = "011-23317004",
      category = SupportCategory.WOMEN_HELPLINE,
      agency = "Special Police Unit for Women & Children",
      region = "Delhi NCR",
      officialSource = "https://delhipolice.nic.in",
      verifiedDate = "Oct 2026"
    ),
    SupportContact(
      id = "d8",
      name = "National Legal Services Authority (NALSA)",
      number = "15100",
      category = SupportCategory.LEGAL_AID,
      agency = "Supreme Court of India / NALSA",
      region = "All India",
      officialSource = "https://nalsa.gov.in",
      verifiedDate = "Oct 2026"
    )
  )

  // 9. Grounded AI Safety Knowledge Base
  val groundedKnowledgeSources = listOf(
    GroundedAiSource(
      title = "Zero FIR Registration Mandate",
      publisher = "Ministry of Home Affairs & Supreme Court of India",
      verifiedDate = "Section 154 CrPC / Bharatiya Nagarik Suraksha Sanhita (BNSS)",
      keyTakeaway = "Any police station in India is legally required to accept and register a Zero FIR for cognizable offences against women, irrespective of territorial jurisdiction.",
      officialUrl = "https://mha.gov.in/advisories-women-safety"
    ),
    GroundedAiSource(
      title = "Free Legal Aid for Women",
      publisher = "National Legal Services Authority (NALSA)",
      verifiedDate = "Section 12(c) Legal Services Authorities Act, 1987",
      keyTakeaway = "Women are eligible for free legal aid, legal advice, and court representation regardless of their income level through DLSA/SLSA.",
      officialUrl = "https://nalsa.gov.in"
    ),
    GroundedAiSource(
      title = "Sakhi One Stop Centre (OSC) Protocol",
      publisher = "Ministry of Women and Child Development",
      verifiedDate = "Operational Guidelines (Revised 2024-2026)",
      keyTakeaway = "Provides integrated support: immediate medical aid, temporary shelter, psycho-social counselling, legal aid, and police facilitation under one roof.",
      officialUrl = "https://wcd.nic.in"
    ),
    GroundedAiSource(
      title = "Safe Public Transit Protocols in Indian Metros",
      publisher = "Bureau of Police Research and Development (BPR&D)",
      verifiedDate = "Urban Transit Safety Advisory 2025",
      keyTakeaway = "Always board designated Women's Coaches in Metro/Suburban trains during late hours; verify driver vehicle registration number on aggregator apps before boarding.",
      officialUrl = "https://bprd.nic.in"
    )
  )

  // 10. Private Evidence Vault Items
  private val _evidenceVault = MutableStateFlow(
    listOf(
      EvidenceItem(
        id = "ev_1",
        title = "Audio Memo - Auto Rickshaw plate 4482",
        type = EvidenceType.AUDIO,
        durationSeconds = 42,
        sizeBytes = 328000,
        notes = "Recorded driver taking non-standard detour near flyover.",
        timestamp = System.currentTimeMillis() - 86400000L
      ),
      EvidenceItem(
        id = "ev_2",
        title = "Station Area Safety Note",
        type = EvidenceType.NOTE,
        durationSeconds = 0,
        sizeBytes = 1200,
        notes = "Street lamps unlit along East exit corridor. Boarded pre-paid booth taxi.",
        timestamp = System.currentTimeMillis() - 172800000L
      )
    )
  )
  val evidenceVault: StateFlow<List<EvidenceItem>> = _evidenceVault.asStateFlow()

  // 11. Incident Reports
  private val _incidentReports = MutableStateFlow<List<IncidentReport>>(
    listOf(
      IncidentReport(
        id = "rep_001",
        title = "Draft Incident Summary - Worli Transit",
        incidentDate = "Oct 24, 2026 - 10:15 PM",
        locationText = "Worli Naka, Mumbai (19.0760 N, 72.8777 E)",
        timeline = listOf(
          TimelineEvent("10:14 PM", "SOS triggered via Guardian AI", isRecordedFact = true),
          TimelineEvent("10:14 PM", "Latest GPS coordinate locked (±12m accuracy)", isRecordedFact = true),
          TimelineEvent("10:15 PM", "SMS alert delivered to Pooja Sharma and Rohan Patel", isRecordedFact = true),
          TimelineEvent("10:18 PM", "Audio recording started by user", isRecordedFact = true),
          TimelineEvent("10:22 PM", "User safely boarded metro station escorted by police chowki", isRecordedFact = false)
        ),
        userRecollection = "Felt uncomfortable when approached by two unidentified individuals near the unlit bus stop. Activated SOS immediately and moved towards the lighted metro entrance.",
        aiDraftSummary = "Incident documented at 10:14 PM near Worli. Emergency alerts successfully delivered to 2 primary contacts. Verified GPS and audio records preserved in private vault. Draft prepared for personal record.",
        isDraft = true
      )
    )
  )
  val incidentReports: StateFlow<List<IncidentReport>> = _incidentReports.asStateFlow()

  // 12. Safety Plan
  private val _safetyPlan = MutableStateFlow(SafetyPlan())
  val safetyPlan: StateFlow<SafetyPlan> = _safetyPlan.asStateFlow()

  // 13. Privacy Settings
  private val _privacySettings = MutableStateFlow(PrivacySettings())
  val privacySettings: StateFlow<PrivacySettings> = _privacySettings.asStateFlow()

  // 14. Active Audio Recording State (Evidence Recorder)
  private val _isRecordingAudio = MutableStateFlow(false)
  val isRecordingAudio: StateFlow<Boolean> = _isRecordingAudio.asStateFlow()

  private val _recordingSeconds = MutableStateFlow(0)
  val recordingSeconds: StateFlow<Int> = _recordingSeconds.asStateFlow()

  // Actions

  fun triggerSos(isTest: Boolean = false) {
    val newId = "sos_" + UUID.randomUUID().toString().take(8)
    val loc = _currentLocation.value

    val initialAttempts = _trustedContacts.value.map { contact ->
      DeliveryAttempt(
        contactId = contact.id,
        contactName = contact.name,
        contactPhone = contact.phone,
        channel = if (isTest) "Test Notification" else "SMS & Push",
        status = DeliveryStatus.QUEUED,
        timestamp = System.currentTimeMillis()
      )
    }

    val session = EmergencySession(
      sessionId = newId,
      idempotencyKey = UUID.randomUUID().toString(),
      isTest = isTest,
      status = SessionStatus.ACTIVE,
      startedAt = System.currentTimeMillis(),
      location = loc,
      deliveryAttempts = initialAttempts
    )

    _activeEmergencySession.value = session

    // Real-time asynchronous delivery state progression
    coroutineScope.launch {
      // Step 1: Queued -> Submitted
      delay(400)
      _activeEmergencySession.value = _activeEmergencySession.value?.copy(
        deliveryAttempts = _activeEmergencySession.value?.deliveryAttempts?.map { attempt ->
          attempt.copy(status = DeliveryStatus.SUBMITTED, latencyMs = 380)
        } ?: emptyList()
      )

      // Step 2: Submitted -> Delivered for high-priority contacts
      delay(800)
      _activeEmergencySession.value = _activeEmergencySession.value?.copy(
        deliveryAttempts = _activeEmergencySession.value?.deliveryAttempts?.mapIndexed { index, attempt ->
          if (index < 2) {
            attempt.copy(status = DeliveryStatus.DELIVERED, latencyMs = 740)
          } else {
            attempt.copy(status = DeliveryStatus.SUBMITTED, latencyMs = 620)
          }
        } ?: emptyList()
      )

      // Step 3: Contact acknowledges (opens alert)
      delay(1500)
      _activeEmergencySession.value = _activeEmergencySession.value?.copy(
        deliveryAttempts = _activeEmergencySession.value?.deliveryAttempts?.mapIndexed { index, attempt ->
          if (index == 0) {
            attempt.copy(status = DeliveryStatus.ACKNOWLEDGED, latencyMs = 1200)
          } else {
            attempt.copy(status = DeliveryStatus.DELIVERED, latencyMs = 890)
          }
        } ?: emptyList()
      )
    }
  }

  fun cancelSos(reason: String) {
    val current = _activeEmergencySession.value ?: return
    val updated = current.copy(
      status = SessionStatus.CANCELLED,
      cancellationReason = reason,
      cancelledAt = System.currentTimeMillis(),
      deliveryAttempts = current.deliveryAttempts.map {
        it.copy(
          channel = "Cancellation Notice Broadcast",
          status = DeliveryStatus.DELIVERED
        )
      }
    )
    _emergencyHistory.value = listOf(updated) + _emergencyHistory.value
    _activeEmergencySession.value = null
  }

  fun resolveSos() {
    val current = _activeEmergencySession.value ?: return
    val updated = current.copy(
      status = SessionStatus.RESOLVED,
      cancelledAt = System.currentTimeMillis()
    )
    _emergencyHistory.value = listOf(updated) + _emergencyHistory.value
    _activeEmergencySession.value = null
  }

  fun startJourney(destination: String, routeName: String, arrivalMinutes: Int) {
    val journey = Journey(
      id = "j_" + UUID.randomUUID().toString().take(6),
      destination = destination,
      plannedRouteName = routeName,
      expectedArrivalMinutes = arrivalMinutes,
      startedAt = System.currentTimeMillis(),
      lastCheckInAt = System.currentTimeMillis(),
      isSharingActive = true
    )
    _activeJourney.value = journey
  }

  fun checkInJourney() {
    _activeJourney.value = _activeJourney.value?.copy(
      lastCheckInAt = System.currentTimeMillis()
    )
  }

  fun confirmSafeArrival() {
    _activeJourney.value = _activeJourney.value?.copy(
      isSafeArrivalConfirmed = true,
      isSharingActive = false
    )
    coroutineScope.launch {
      delay(2000)
      _activeJourney.value = null
    }
  }

  fun endJourney() {
    _activeJourney.value = null
  }

  fun startRecordingEvidence() {
    _isRecordingAudio.value = true
    _recordingSeconds.value = 0
    coroutineScope.launch {
      while (_isRecordingAudio.value) {
        delay(1000)
        if (_isRecordingAudio.value) {
          _recordingSeconds.value += 1
        }
      }
    }
  }

  fun stopRecordingEvidence(notes: String = "") {
    if (!_isRecordingAudio.value) return
    val seconds = _recordingSeconds.value
    _isRecordingAudio.value = false

    val timeFormatted = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
    val item = EvidenceItem(
      id = "ev_" + System.currentTimeMillis().toString().takeLast(6),
      title = "Evidence Audio ($timeFormatted)",
      type = EvidenceType.AUDIO,
      durationSeconds = seconds,
      sizeBytes = (seconds * 16000L),
      notes = notes.ifEmpty { "Manual audio recording during safety check." },
      timestamp = System.currentTimeMillis()
    )
    _evidenceVault.value = listOf(item) + _evidenceVault.value
    _recordingSeconds.value = 0
  }

  fun deleteEvidence(id: String) {
    _evidenceVault.value = _evidenceVault.value.filter { it.id != id }
  }

  fun addContact(name: String, phone: String, relationship: String) {
    val newContact = TrustedContact(
      id = "c_" + UUID.randomUUID().toString().take(5),
      name = name,
      phone = phone,
      relationship = relationship,
      priorityOrder = _trustedContacts.value.size + 1,
      isVerified = true
    )
    _trustedContacts.value = _trustedContacts.value + newContact
  }

  fun removeContact(id: String) {
    _trustedContacts.value = _trustedContacts.value.filter { it.id != id }
  }

  fun updatePrivacySettings(settings: PrivacySettings) {
    _privacySettings.value = settings
  }

  fun wipeAllLocalData() {
    _activeEmergencySession.value = null
    _emergencyHistory.value = emptyList()
    _evidenceVault.value = emptyList()
    _activeJourney.value = null
  }

  fun refreshLocation(isStaleSimulation: Boolean = false) {
    if (isStaleSimulation) {
      _currentLocation.value = _currentLocation.value.copy(
        timestamp = System.currentTimeMillis() - (12 * 60 * 1000L),
        freshness = LocationFreshness.STALE
      )
    } else {
      _currentLocation.value = _currentLocation.value.copy(
        timestamp = System.currentTimeMillis(),
        freshness = LocationFreshness.CURRENT,
        accuracyMeters = 8.5f
      )
    }
  }
}
