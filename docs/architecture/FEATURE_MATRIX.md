# Guardian AI — Product Scope & Feature Matrix (23 Areas)

| # | Feature Area | Description | Implementation Status | Safety / Compliance Invariant |
|---|---|---|---|---|
| 1 | **One-action SOS** | 2-second deliberate hold trigger, immediate local session creation, last-known location attachment, cancellation workflow. | **Implemented & Verified** | Zero AI dependency; immediate local session creation. |
| 2 | **Trusted safety circle** | Add/edit contacts, prioritize escalation, configure permissions (location, SOS, journey), verification status. | **Implemented & Verified** | Explicit user consent for each shared resource. |
| 3 | **Live location & Stale warnings** | Real-time coordinates, accuracy in meters, timestamp, stale warning badge (>5m), approximate indicator. | **Implemented & Verified** | Never mislabel stale coordinates as live fixes. |
| 4 | **Low-connectivity mode** | Local SQLite emergency queue, exponential backoff retries, offline delivery status reporting. | **Implemented & Verified** | Explains unconfirmed delivery honestly when offline. |
| 5 | **Emergency checklist** | Immediate contextual steps: 112 dialing, quiet mode, staying in lit areas, notifying circle. | **Implemented & Verified** | Offline accessible; no network required. |
| 6 | **False-alarm recovery** | Safe cancellation with reason, sends correction notice to contacts, preserves auditable session history. | **Implemented & Verified** | Audit log retained for accountability. |
| 7 | **Journey sharing** | Destination, planned arrival, live sharing timer, instant revocation, safe arrival confirmation. | **Implemented & Verified** | Auto-expires sharing at journey completion. |
| 8 | **AI safer routes** | Evaluates route alternatives (distance, transit nodes, lighting, police booths), provides grounded rationale. | **Implemented & Verified** | Does not claim routes are "guaranteed safe"; non-AI fallback. |
| 9 | **Route tracking & Deviations** | Compares planned route with actual trajectory, neutral notification without panic alarms. | **Implemented & Verified** | Route deviation is treated as neutral, not danger proof. |
| 10 | **Journey check-ins** | Periodic safety check-ins (15m, 30m), snooze/extend options, circle notification if missed. | **Implemented & Verified** | Missed check-in does not auto-declare emergency. |
| 11 | **Personal safety plan** | Medical notes, emergency contacts, primary transit mode, custom emergency SMS template. | **Implemented & Verified** | Least-privilege encrypted local storage. |
| 12 | **Grounded AI safety companion**| Factual safety planning, legal rights (Zero FIR, legal aid), grounded citations, verified dates. | **Implemented & Verified** | Server-side credentials; honest uncertainty fallback. |
| 13 | **Verified support directory** | Curated Indian emergency numbers (112, 1091, 181, 1090, 139), city/state filters, verification dates. | **Implemented & Verified** | Official government sources only; verified dates. |
| 14 | **Incident timeline & report** | Chronological event assembler, separates facts from recollection, generates editable draft report. | **Implemented & Verified** | User draft only; never auto-submitted to authorities. |
| 15 | **Multilingual assistance** | Full localization in English, Hindi (हिंदी), and Marathi (मराठी) with dynamic switching. | **Implemented & Verified** | Externalized strings, high contrast, screen reader ready. |
| 16 | **After-incident support** | Grounded aftercare guide, medical assistance, legal aid rights, emotional support directory. | **Implemented & Verified** | Non-victim blaming; no automatic data dissemination. |
| 17 | **Privacy control centre** | Centralized permissions, retention policies, clear data descriptions, one-tap data export and wipe. | **Implemented & Verified** | Server & local authorization enforcement. |
| 18 | **Alert delivery transparency**| Real-time per-contact status: Queued, Submitted, Delivered, Acknowledged, Failed with provider receipts. | **Implemented & Verified** | No unsupported delivery claims. |
| 19 | **Private evidence vault** | Access-controlled vault for incident audio/photos, timestamped checksums, deletion & export. | **Implemented & Verified** | Private local storage; no public bucket exposure. |
| 20 | **Evidence recorder** | Audio/notes recorder with unmistakable high-visibility recording indicator, runtime permissions. | **Implemented & Verified** | Zero covert recording; visible active banner. |
| 21 | **Fake call simulation** | Realistic incoming call UI with custom caller ("Mom", "Office"), ringtone, and safe dismissal. | **Implemented & Verified** | Explicitly labeled simulation; safe exit tool. |
| 22 | **Safer onboarding** | Step-by-step permission primer (Location, Audio, Notifications), test mode preview. | **Implemented & Verified** | Clear explanations before requesting permissions. |
| 23 | **Scheduled test alerts** | Dry-run alert simulator for circle testing, unmistakably labeled as test, 112 disabled. | **Implemented & Verified** | Strict separation of test and production alerts. |
