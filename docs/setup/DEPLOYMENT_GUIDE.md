# Guardian AI — Production Deployment & Local Setup Guide

## 1. Prerequisites
- Android Studio Ladybug / Meerkat or Command Line Tools
- JDK 17 / 21
- Node.js 20+ (for website & Cloud Functions)
- Firebase CLI (`npm install -g firebase-tools`)

---

## 2. Environment Variables (.env)
Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```
Ensure secrets are injected securely via CI or the AI Studio Secrets panel:
```ini
# Routing Provider (Google Maps Platform or Mapbox)
MAPS_API_KEY=YOUR_MAPS_API_KEY

# Grounded AI Service (Gemini API for backend companion)
GEMINI_API_KEY=YOUR_GEMINI_API_KEY

# Firebase Configuration (for FCM & Firestore)
FIREBASE_PROJECT_ID=guardian-ai-prod
FIREBASE_CLIENT_ID=YOUR_CLIENT_ID
```

---

## 3. Local Development & Testing

### 3.1 Android Build & Compilation
Run the local compilation check:
```bash
gradle :app:assembleDebug
```

Run local JVM unit and Robolectric tests:
```bash
gradle :app:testDebugUnitTest
```

### 3.2 Firebase Emulators (Firestore & Auth)
To test security rules locally:
```bash
firebase emulators:start --only firestore,auth,storage
```

Deploy security rules to Firebase:
```bash
firebase deploy --only firestore:rules,storage
```

---

## 4. Release Checklist & Deployment Verification
- [x] **SOS Zero Delay**: Verified 2-second hold activates local session with 0ms AI delay.
- [x] **Honest Delivery Receipts**: Verified state transitions (`QUEUED`, `SUBMITTED`, `DELIVERED`, `ACKNOWLEDGED`, `FAILED`).
- [x] **Stale Location Warning**: Coordinates > 5 minutes old are explicitly tagged `STALE`.
- [x] **No Covert Surveillance**: Visible red recording bar when recording audio evidence.
- [x] **Indian Emergency Directory**: Verified numbers for 112, 1091, 181, 1090, 139.
- [x] **Localization**: Verified strings for English, Hindi (हिंदी), and Marathi (मराठी).
- [x] **False Alarm Cancellation**: Verified cancellation broadcast to safety circle.
- [x] **Public Landing Website**: Responsive site available at `public/index.html`.
