# Local data and privacy — stage 1

- The app does not open a persistent Room database in patch 0007. Tests use synthetic data and an in-memory SQLite database.
- Auto Backup is disabled in the manifest. Explicit exclusions cover all Android backup domains in legacy rules and both cloud and Android device-transfer rules. Vendor behavior and actual backup transport require device validation; these settings are not a guarantee against data extraction. No iOS counterpart is configured.
- Room uses SQLite and does not by itself encrypt its database. Do not enter real patient information. Authentication, authorization, encryption and a recovery/export policy need separate design and validation before real use.
- No additional network, camera or Bluetooth permission is introduced. Do not log patient names or clinical measurements.
- Keep exported schema JSON in version control and test migrations before releasing a persistent schema. Do not use destructive migration fallback.
- With automatic backups disabled, losing or uninstalling the app can lose local data once persistence is implemented. A secure user-controlled backup/recovery workflow is out of scope.
