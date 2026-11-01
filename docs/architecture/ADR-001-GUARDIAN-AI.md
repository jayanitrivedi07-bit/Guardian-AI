# Architecture Decision Record (ADR-001): Guardian AI Architecture & Backend Selection

## Status: Accepted
**Date:** 2026-10-25
**Scope:** Privacy-First Women's Personal Safety Platform (Android Native App, Landing Website, Cloud Backend)

---

## 1. Context & Problem Statement
Personal safety platforms handle life-critical workflows (SOS, live emergency coordinates, delivery receipts) and highly sensitive private information (location history, incident notes, evidence recordings). 
Women users in India require:
1. Deterministic, zero-AI-delay SOS triggering with local-first session creation.
2. Honest delivery transparency across patchy network coverage (Queued, Submitted, Delivered, Acknowledged).
3. Stale-location transparency: never passing old coordinates as live fixes.
4. Robust privacy-preserving zero-trust cloud backend with field-level access control.
5. Multilingual accessibility in English, Hindi, and Marathi.
6. Responsive public landing website for transparent safety education, responsible AI disclosure, and verification status.

---

## 2. Decision: Technology Stack Selection

### 2.1 Native Android Client
- **Language:** Kotlin 2.2+ with modern Coroutines and StateFlow.
- **UI Framework:** Jetpack Compose with Material Design 3 (M3) dynamic color tokens.
- **Architecture:** Unidirectional Data Flow (MVVM / Clean Architecture), repository pattern, local persistence via Room & DataStore.
- **Location Services:** Google Play Services FusedLocationProviderClient with fallback to Android LocationManager and clear stale/approximate tagging.
- **Emergency Resilience:** Offline SQLite event queue with exponential backoff retry.
- **Multilingual Support:** Resource bundles (`values/`, `values-hi/`, `values-mr/`) and in-app runtime locale switching.

### 2.2 Backend & Cloud Services: Firebase vs. Supabase Evaluation
| Criteria | Firebase (Selected) | Supabase | Justification |
| :--- | :--- | :--- | :--- |
| **Push Delivery (FCM)** | Native, 1st-party Android integration | Requires external FCM gateway | Direct FCM device tokens, background data messages |
| **Security Rules** | Granular declarative Firestore & Storage rules | PostgreSQL RLS | Firestore rules integrate directly with Android SDK and emulator testing |
| **App Check & Device Attestation** | Integrated reCAPTCHA / Play Integrity | Manual setup | Guards against bot SOS spam and fake telemetry |
| **Offline Cache** | Native Firestore offline persistence engine | Limited client caching | Offline emergency queue operates seamlessly |
| **Verdict** | **Selected** | Alternative | Firebase provides optimal latency, zero-maintenance scaling, and certified mobile integration |

### 2.3 Grounded AI Architecture
- **Boundary:** AI is strictly isolated from SOS activation, contact notification, and emergency dispatch.
- **Grounding Layer:** Curated vector knowledge base containing verified Indian legal rights (Zero FIR, Section 154 CrPC / BNSS, Free Legal Aid), National Commission for Women (NCW) guidelines, One-Stop Centre (Sakhi) protocols, and transit safety procedures.
- **Transparency:** All AI responses cite source documents, verified dates, and clear uncertainty qualifiers.

---

## 3. Core Safety Invariants (Non-Negotiables)
1. **SOS Zero-Dependency:** SOS triggers immediately upon 2-second user hold, capturing last-known coordinates without waiting for GPS lock.
2. **Honest Delivery State:** An alert is never marked "Delivered" until an end-to-end receipt is confirmed.
3. **No Secret Recording:** Audio/video evidence recording requires user initiation and displays a persistent high-visibility warning banner.
4. **Draft Reports Only:** Incident reports are generated as client drafts for user review; never automatically transmitted to law enforcement.
5. **Safe False-Alarm Cancellation:** Accidental triggers can be cancelled with a dedicated notice broadcast to contacts, preserving the audit trail.
