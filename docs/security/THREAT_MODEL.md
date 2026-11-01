# Guardian AI — Security & Privacy Threat Model

## 1. Threat Matrix & Mitigations

### 1.1 Unauthorized Account & Location Access
- **Threat:** Malicious actors attempting to read a user's live coordinates or journey history.
- **Mitigation:** Zero-trust architecture. Firestore security rules enforce `request.auth.uid == resource.data.ownerId`. Trusted contacts can only read live location during active `journey` or `emergency` sessions where `request.auth.uid in resource.data.authorizedContactUids`.

### 1.2 SOS Spoofing & Replay Attacks
- **Threat:** Replaying emergency requests to flood contacts or trigger false alarms.
- **Mitigation:** Every emergency event contains an idempotency key (`eventId`), timestamp within 60s of server clock, and owner digital signature. Duplicate IDs within 24h are rejected.

### 1.3 Secret Surveillance / Covert Evidence Recording
- **Threat:** Malicious apps or unauthorized features recording audio/video covertly.
- **Mitigation:** Invariant: No covert recording. Audio and video capture requires foreground user interaction, requests explicit Android runtime permissions, and displays a prominent, persistent red recording indicator banner with elapsed duration.

### 1.4 Test Alert Leakage to Production
- **Threat:** Test alerts inadvertently dialing emergency services (112) or causing panic among family.
- **Mitigation:** Strict environment separation: `environment: "test"` events use separate channels, prefix all outgoing messages with `[TEST - NO EMERGENCY]`, and are strictly prohibited from calling phone dialer intents for 112/1091.

### 1.5 Stale Location Misrepresentation
- **Threat:** Presenting 3-hour-old GPS fix as current position during a kidnapping or assault.
- **Mitigation:** All location packets include `locationTimestamp`, `accuracyMeters`, and an explicit `freshnessStatus` (`CURRENT`, `STALE`, `APPROXIMATE`, `UNAVAILABLE`). If older than 5 minutes, UI and payload explicitly flag the coordinate as stale.

### 1.6 AI Hallucination in Emergency Scenarios
- **Threat:** AI giving fabricated legal advice or non-existent helpline numbers.
- **Mitigation:** AI is decoupled from the emergency SOS path. AI safety companion uses a strictly curated, retrieval-grounded vector store containing verified Indian legal provisions (Section 154 CrPC, Free Legal Aid Act, Sakhi Scheme) and cites verifiable dates and URLs.
