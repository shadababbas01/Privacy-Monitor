# Architecture Documentation: Privacy Monitor

## Overview

Privacy Monitor is built using standard Clean Architecture principles split into three primary layers: **Presentation**, **Domain**, and **Data**.

```
                           +------------------------+
                           |   Presentation Layer   |
                           | ViewModels & Compose UI|
                           +-----------+------------+
                                       |
                                       v
                           +------------------------+
                           |      Domain Layer      |
                           |  Use Cases & RiskEngine|
                           +-----------+------------+
                                       |
                                       v
                           +------------------------+
                           |       Data Layer       |
                           |  Room, System Wrappers |
                           +------------------------+
```

### 1. Presentation Layer (`com.privacymonitor.android.presentation`)
- **Jetpack Compose UI**: Single activity (`MainActivity`) with Navigation Compose graph.
- **ViewModels**: StateFlow-driven ViewModels managed by Hilt.
- **Design System**: Centralized themes, typography, colors, and reusable UI components.

### 2. Domain Layer (`com.privacymonitor.android.domain`)
- **RiskEngine**: Pure Kotlin deterministic engine evaluating 100-point baseline risk scoring.
- **Use Cases**: Encapsulate single responsibilities (`ScanAppsUseCase`, `CalculateScoreUseCase`, `CheckUpiSafetyUseCase`, `GeneratePdfUseCase`).
- **Models**: Immutable domain data objects (`InstalledApp`, `RiskAssessment`, `PermissionInfo`, `RiskReason`).

### 3. Data Layer (`com.privacymonitor.android.data`)
- **Room Persistence**: `PrivacyDatabase` with `AppDao`, `EventDao`, `ScoreDao`, and `ReportDao`.
- **DataStore**: Preference storage for language, theme, retention policy, and opt-in settings.
- **System Wrappers**: Interoperability with `PackageManager`, `SensorManager`, `NetworkStatsManager`, and `TrafficStats`.

---

## Data Flow

1. User or WorkManager triggers a privacy scan.
2. `AndroidPackageManager` queries installed packages and active permissions from the OS.
3. `RiskEngine` evaluates rules against each package and computes points deductions and per-app scores.
4. `CalculateScoreUseCase` aggregates overall device privacy score using a weighted formula.
5. `AppRepositoryImpl` transactionally saves snapshots to Room DB.
6. Flow emits updated `RiskAssessment` to `HomeViewModel` which updates Compose UI.
