# Privacy Monitor (`com.privacymonitor.android`)

**Samjho kaunsi app aapki privacy use kar rahi hai.**

Privacy Monitor is a production-ready, local-first Android application designed to inspect installed apps, analyze permission access, detect risk patterns, monitor live sensor states, track network data usage, provide Hinglish AI privacy advice, perform India-specific UPI safety checks, generate weekly PDF audit reports, and display a home screen widget.

---

## Key Features

1. **AI Privacy Score Engine**: Deterministic 0–100 score with explainable point deductions (Background Location, Accessibility, Overlay, SMS, Sideloaded installer, Mic, Camera).
2. **Permission Analysis**: Deep breakdown of granted vs denied permissions and special access privileges across all installed applications.
3. **Live Sensors Status**: Honest hardware status monitoring for GPS, Camera, Microphone, Bluetooth, Gyroscope, and Network Interface based on official Android APIs.
4. **India-Specific Privacy & UPI Safety Check**: Targeted checks for UPI and financial applications (Paytm, PhonePe, Google Pay) verifying overlay risks and accessibility service exposure.
5. **AI Privacy Advisor**: Local-first Hinglish NLP advisor explaining security concepts in simple language without cloud data leakage. Optional Gemini API opt-in support.
6. **PDF Audit Reports**: Export clean, native PDF reports summarizing weekly privacy trends.
7. **Home Screen AppWidget**: Instant visibility into device privacy score right from the home screen.
8. **Dark & Light Themes**: Original UI design featuring Deep Navy dark mode and clean off-white light mode with custom typography.

---

## Tech Stack & Architecture

- **Language**: Kotlin 1.9.23
- **UI**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Dagger Hilt
- **Persistence**: Room Database + DataStore Preferences
- **Asynchronous**: Kotlin Coroutines + Flow
- **Background Jobs**: WorkManager
- **Navigation**: Jetpack Navigation Compose
- **PDF Generation**: Android `PdfDocument`

---

## Build & Test Commands

```bash
# Run unit tests
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```
