# RehaSense — Etapa 2 / Parche 02

## Objetivo

Introducir contratos de dominio y adaptadores de datos para Firebase Authentication y
Cloud Firestore, sin modificar Room, RehaSenseGraph, navegación o pantallas existentes.

## Contratos

- `AuthRepository`: flujo de sesión, registro, inicio de sesión, cierre y recuperación.
- `CloudUserRepository`: consulta del documento `users/{uid}` de la sesión actual.
- `FirebaseModule.fromDefaultApp()`: composición aislada de repositorios.

## Esquema **futuro** de `users/{uid}`

- ID de documento: UID de Firebase Authentication.
- `displayName`: `String`.
- `role`: `PATIENT` o `PHYSIOTHERAPIST` (solo representación de perfil).

El adaptador no escribe perfiles ni asigna roles. Las reglas `deny all` del Parche 01
siguen vigentes; por tanto, la consulta remota arrojará `PERMISSION_DENIED` hasta que
se publiquen reglas probadas en un parche posterior. No se deben publicar reglas
abiertas para hacer pasar este parche.

## Estados offline

`CloudUserSnapshot.fromCache` y `hasPendingWrites` indican si se recibió una respuesta
desde caché o si existen escrituras sin confirmar. No confundir caché con confirmación
del servidor; un rol en caché nunca es autorización para consultar datos clínicos.

## Restricciones

- No utilizar la selección visual de perfil como prueba de ser fisioterapeuta.
- No conceder acceso a datos de pacientes desde el cliente.
- No conectar todavía el nuevo módulo a las pantallas ni modificar `RehaSenseGraph`.
- No migrar automáticamente perfiles de Room a usuarios autenticados.
- No incluir `google-services.json` en Git.

## Pruebas

Unitarias: `FirestoreUserMapperTest` con 4 casos sin red.

Compilación: `./gradlew :app:assembleDebug`.

Sin emulador/backend adicional no se prueban credenciales reales ni reglas de seguridad.
En un dispositivo Android, ejecutar la prueba instrumentada del Parche 01.
