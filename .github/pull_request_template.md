## Descripción

Cierre de la etapa 1 de RehaSense: arquitectura, perfiles, navegación y almacenamiento local.

## Cambios principales

- [ ] Navegación y pantallas para Paciente y Fisioterapeuta
- [ ] Validación y almacenamiento local de perfiles
- [ ] Base de datos Room (versión 2) y migración 1→2
- [ ] Pruebas aisladas de navegación y smoke test de MainActivity
- [ ] Documentación y verificación final

## Evidencia de pruebas (no marcar sin ejecutar)

- [ ] `:app:assembleDebug`
- [ ] `:app:testDebugUnitTest`
- [ ] `:app:lintDebug`
- [ ] `:app:connectedDebugAndroidTest` con emulador
- [ ] Esquemas Room `1.json` y `2.json` versionados
- [ ] Pruebas manuales de ambos roles y recuperación

Adjuntar capturas, salida de Gradle y los reportes correspondientes.

## Limitaciones y riesgos

- No existe autenticación ni cifrado integral de SQLite.
- No utilizar datos clínicos reales.
- Funciones de sensores, análisis de movimiento y fisioterapia: fuera de esta etapa.
- Incidencias abiertas: [indicar pruebas o fallos pendientes, o escribir "ninguna" tras verificar].

## Checklist de revisión

- [ ] No hay archivos `.idea` generados accidentalmente
- [ ] No hay secretos o datos personales en el diff
- [ ] Los nombres y las responsabilidades de las clases son claros
- [ ] No hay lógica de datos de Room en Composables
- [ ] Los commits describen cambios comprobables
