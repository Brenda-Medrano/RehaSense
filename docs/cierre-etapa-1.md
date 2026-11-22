# RehaSense — cierre de etapa 1

## Objetivo y límite del entregable

La etapa 1 prepara una aplicación Android nativa, con dos roles, navegación,
formularios, arquitectura por capas, persistencia local Room y pruebas. **No**
implementa seguimiento de articulaciones, diagnóstico, tratamiento, cuentas
verificadas, cifrado integral ni sincronización con la nube. No usar datos de
pacientes reales.

## Arquitectura

- `presentation/`: pantallas Compose y ViewModel. El formulario valida antes de guardar.
- `domain/`: modelos, reglas y contrato `ProfileRepository`, independientes de Room.
- `data/`: entidades, DAO, base de datos v2 y repositorio transaccional.
- `di/`: construcción manual de dependencias sin añadir otro plugin Gradle.
- `navigation/`: destinos y host de navegación con repositorio sustituible para pruebas.

La aplicación normal sigue usando `RehaSenseGraph` y Room. Las pruebas de
navegación utilizan un repositorio falso aislado **por prueba**; eso no valida
por sí solo el almacenamiento real. `RoomProfileRepositoryTest`, las pruebas
de relaciones y las de migración cubren la persistencia.

## Validación obligatoria en Windows

1. Confirmar rama `feature/stage-1-architecture` y `git status` limpio.
2. Ejecutar `powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Verificar-Etapa1.ps1`.
3. Encender el emulador; comprobar `adb devices`.
4. Ejecutar el mismo script añadiendo `-AndroidTests`.
5. Comprobar que `app/schemas/**/1.json` y `2.json` existen y están registrados en Git.
6. Revisar `app/build/reports/lint-results-debug.html` y el reporte de pruebas.
7. Verificar manualmente: bienvenida; selección de los dos roles; validación de
   nombre vacío; creación de perfil; cambio de rol; recuperación de perfil;
   cierre y reapertura de la aplicación; orientación; modo avión.
8. Probar únicamente con nombres ficticios.

## Investigación de fallos

- Si falla `MainActivityLaunchTest`: la pantalla inicial real no coincide con
  la aplicación que se prueba. Revisar `MainActivity.kt` y el `AndroidManifest.xml`.
- Si falla `RehaSenseNavigationTest`: revisar el flujo Compose, las etiquetas
  de prueba y el reporte; cada prueba ahora usa su propio repositorio aislado.
- Si falla `RoomProfileRepositoryTest`: revisar base de datos, consulta,
  transacción y ejecución sobre `Dispatchers.IO`.
- Si falla `RoomMigration1To2Test`: no eliminar ni sustituir migraciones con
  `fallbackToDestructiveMigration`; corregir el esquema.
- Si el dispositivo no aparece, abrir Device Manager y comprobar `adb devices`.
- No marcar la etapa como aprobada si la compilación o alguna prueba falla.

## Cierre de Pull Request

Base: `main`. Comparar: `feature/stage-1-architecture`.
Adjuntar: capturas de los dos paneles, resultado de compilación y pruebas,
reporte de Lint y evidencia de Room. En el PR declarar las limitaciones
pendientes y el fallo de cualquier prueba que no se haya resuelto.
No hacer merge hasta revisión y comprobaciones satisfactorias.
