package com.example

import com.example.data.model.*
import com.example.data.repository.GuardianRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuardianSafetyUnitTest {

  private lateinit var repository: GuardianRepository

  @Before
  fun setUp() {
    repository = GuardianRepository()
  }

  @Test
  fun `SOS trigger starts local emergency session immediately with active status`() {
    assertNull(repository.activeEmergencySession.value)

    repository.triggerSos(isTest = false)

    val session = repository.activeEmergencySession.value
    assertNotNull(session)
    assertEquals(SessionStatus.ACTIVE, session!!.status)
    assertFalse(session.isTest)
    assertTrue(session.deliveryAttempts.isNotEmpty())
    assertEquals(DeliveryStatus.QUEUED, session.deliveryAttempts.first().status)
  }

  @Test
  fun `Test SOS is strictly separated from production emergency`() {
    repository.triggerSos(isTest = true)

    val session = repository.activeEmergencySession.value
    assertNotNull(session)
    assertTrue(session!!.isTest)
    assertEquals("Test Notification", session.deliveryAttempts.first().channel)
  }

  @Test
  fun `Stale location warning correctly flags coordinates older than 5 minutes`() {
    val freshLocation = LocationData(
      timestamp = System.currentTimeMillis() - 60_000L, // 1 minute old
      freshness = LocationFreshness.CURRENT
    )
    assertFalse(freshLocation.isStale)

    val staleLocation = LocationData(
      timestamp = System.currentTimeMillis() - 360_000L, // 6 minutes old
      freshness = LocationFreshness.CURRENT
    )
    assertTrue(staleLocation.isStale)
  }

  @Test
  fun `False alarm cancellation preserves cancellation reason and auditable history`() {
    repository.triggerSos(isTest = false)
    val testReason = "Accidental tap in pocket."

    repository.cancelSos(testReason)

    assertNull(repository.activeEmergencySession.value)
    val history = repository.emergencyHistory.value
    assertEquals(1, history.size)
    assertEquals(SessionStatus.CANCELLED, history.first().status)
    assertEquals(testReason, history.first().cancellationReason)
    assertNotNull(history.first().cancelledAt)
  }

  @Test
  fun `Trusted circle permits adding and removing contacts safely`() {
    val initialCount = repository.trustedContacts.value.size

    repository.addContact("Priya Nair", "+91 99887 76655", "Friend")
    assertEquals(initialCount + 1, repository.trustedContacts.value.size)

    val added = repository.trustedContacts.value.last()
    assertEquals("Priya Nair", added.name)

    repository.removeContact(added.id)
    assertEquals(initialCount, repository.trustedContacts.value.size)
  }

  @Test
  fun `Verified support directory includes official Indian emergency numbers`() {
    val directory = repository.verifiedSupportDirectory
    val has112 = directory.any { it.number == "112" && it.category == SupportCategory.EMERGENCY }
    val has1091 = directory.any { it.number == "1091" && it.category == SupportCategory.WOMEN_HELPLINE }
    val has181 = directory.any { it.number == "181" && it.category == SupportCategory.DOMESTIC_ABUSE }
    val has139 = directory.any { it.number == "139" && it.category == SupportCategory.RAILWAY_SAFETY }

    assertTrue(has112)
    assertTrue(has1091)
    assertTrue(has181)
    assertTrue(has139)
  }

  @Test
  fun `Wipe all local data cleanly purges history, vault, and active sessions`() {
    repository.triggerSos(isTest = false)
    assertNotNull(repository.activeEmergencySession.value)

    repository.wipeAllLocalData()

    assertNull(repository.activeEmergencySession.value)
    assertTrue(repository.emergencyHistory.value.isEmpty())
    assertTrue(repository.evidenceVault.value.isEmpty())
  }
}
