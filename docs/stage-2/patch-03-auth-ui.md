# RehaSense — Etapa 2 / Parche 03: autenticación funcional

## Objetivo y restricciones

Reutilizar `AuthRepository` y `FirebaseModule` del Parche 02 para ofrecer pantallas Compose
de registro, inicio de sesión, recuperación de contraseña, sesión actual y cierre de sesión.
No se modifica Room, no se crean perfiles en Firestore y no se conceden roles de fisioterapia.

## Orden RED → GREEN → UI → Launcher

1. `0001` define los 7 casos de validación; como `AuthFormValidator` aún no existe,
   la compilación de las pruebas **debe fallar** (RED, fallo de referencia no resuelta).
2. `0002` implementa validaciones y mensajes de error seguros. Ejecutar las pruebas (GREEN).
3. `0003` incorpora estado y ViewModel que dependen únicamente de `AuthRepository`.
4. `0004` incorpora AuthActivity y AuthScreen.
5. `0005` incorpora documentación y script de integración del lanzador.
6. El script `tools/patch03/Enable-AuthLauncher.ps1` realiza una revisión semántica de
   AndroidManifest.xml, identifica el único MAIN/LAUNCHER en MainActivity y lo traslada
   a AuthActivity, sin eliminar MainActivity. Además cambia su `android:exported` a `false` para
   impedir que una aplicación externa acceda a flujos Room sin protección.
   Se ejecuta con `-CheckOnly` antes del cambio.

## Firebase Console

Authentication → Sign-in method → Email/Password: Enabled. Usar cuentas de prueba.

## Seguridad

- Firestore permanece `deny all`; registrar un usuario NO crea `users/{uid}` ni concede roles.
- No almacenar contraseñas en Room, Firestore, logs o SavedState.
- No utilizar usuarios reales/pacientes durante esta integración.
- El antiguo MainActivity se conserva, pero deja de ser LAUNCHER temporalmente.
- No introducir acceso al prototipo Room como si estuviera protegido por Authentication.
- La verificación de correo y perfiles autorizados se tratarán en parches siguientes.

## Pruebas

`./gradlew :app:assembleDebug`

`./gradlew :app:testDebugUnitTest`

`./gradlew :app:testDebugUnitTest --tests "com.rehasense.app.presentation.auth.AuthFormValidatorTest"`

Manual en emulador: registro, cierre, inicio, contraseña equivocada, recuperación
(con correo real de prueba y comprobando bandeja), persistencia tras reinicio y sesión cerrada.
Firebase Console → Authentication → Users debe mostrar la cuenta de prueba.
No verificar lecturas Firestore todavía (están bloqueadas intencionalmente).
