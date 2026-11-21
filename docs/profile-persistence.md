# Perfil local — parche 0009

## Arquitectura

- `ProfileRepository` es un contrato de dominio, sin referencias a Android.
- `RoomProfileRepository` traduce los modelos del dominio a `UserEntity`, `ProfileEntity` y `PatientEntity` / `PhysiotherapistEntity`.
- `RoomDatabase.runInTransaction` garantiza atomicidad: si falla una fila, las inserciones de la operación se revierten.
- Las operaciones de DAO bloqueantes se ejecutan usando `withContext(Dispatchers.IO)`.
- `RehaSenseGraph` construye una única instancia por proceso con `applicationContext` y registra `MIGRATION_1_2`. No usa `fallbackToDestructiveMigration`.
- `ProfileViewModel` solo muestra el panel después de una escritura completada. Permite reintentar tras un fallo.
- La recuperación es una acción explícita: `Recuperar último perfil de este rol`; nunca revela un perfil automáticamente al entrar a la pantalla.

## Seguridad

Los perfiles contienen nombres en SQLite **sin cifrado integral, PIN ni autenticación**. Esta es una demo educativa con datos ficticios. No introducir nombres reales, información de salud ni registros clínicos hasta implementar un mecanismo de autenticación, cifrado y control de acceso adecuado. La exclusión de respaldos del parche 0007 no equivale al cifrado.

## Pruebas

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --no-configuration-cache
.\gradlew.bat :app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.rehasense.app.data.repository.RoomProfileRepositoryTest"
```

Las pruebas instrumentadas necesitan un emulador conectado. No ejecutar `connectedDebugAndroidTest` completo hasta resolver las fallas conocidas de `RehaSenseNavigationTest` del parche 0006. El parche 0009 no intenta corregirlas ni modifica Navigation Compose.

## Migración y datos

La base de datos se llama `rehasense.db`. El esquema v2 del parche 0008 es el punto de partida; si se encuentra una base v1 se aplica `MIGRATION_1_2`. La creación de perfiles no cambia el esquema ni requiere v3; no debe editarse manualmente `app/schemas/2.json`.

## Limitaciones conocidas

- No hay inicio de sesión ni distinción segura entre personas que comparten un dispositivo.
- La recuperación por rol devuelve el perfil con fecha de creación más reciente, usando identificador como desempate.
- No existen todavía programadores de ejercicios, capturas de pose, conexión BLE ni persistencia de métricas clínicas desde la interfaz.
