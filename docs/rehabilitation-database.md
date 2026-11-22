# RehaSense — modelo local de rehabilitación (esquema 2)

El parche 0008 amplía la base Room v1 de usuarios a nueve tablas en v2.
Todos los datos del parche son **ficticios**. La UI aún no abre ni persiste la base.

## Relaciones

- `users` → `profiles`: 1:0..1 (índice único sobre `profiles.userId`).
- `profiles` → `patients` o `physiotherapists`: 1:0..1 por tabla (el rol real debe validarlo el caso de uso).
- `patients` + `physiotherapists` → `rehabilitation_programs`: referencias obligatorias.
- `rehabilitation_programs` ↔ `exercises`: N:M mediante `program_exercises`.
- `rehabilitation_programs` → `sessions`: 1:N.
- `sessions` + `exercises` → `biomechanical_results`: referencias obligatorias.

Las restricciones FK e índices protegen integridad. `RESTRICT` evita borrar programas,
sesiones, ejercicios y profesionales con historial dependiente. `CASCADE` está limitado
a datos de perfil sin registros protegidos y la tabla de unión programa-ejercicio.

Los campos de ángulo están expresados en grados. El campo `unit` de resultados es
obligatorio y se almacenará junto al número (`value`). `source` distingue el origen
declarado de medición; todavía no hay mediciones reales de cámara, IMU ni sensores.

## Migración

- `RehaSenseDatabase`: versión 2, `exportSchema = true`.
- `MIGRATION_1_2` conserva `users` y crea las ocho tablas adicionales.
- En el parche 0009, el constructor de una base persistente **debe registrar**
  `.addMigrations(MIGRATION_1_2)` y **no** debe utilizar fallback destructivo.
- Guardar los JSON de esquema **generados por Room** en `app/schemas` después
  de compilar; no inventar ni editar versiones anteriores.

## Riesgos pendientes

- La integridad de que `PatientEntity.profileId` sea realmente rol `PATIENT` y la
  de `PhysiotherapistEntity` se impondrá en el repositorio / casos de uso del parche 0009.
- La relación entre el ejercicio del resultado y el ejercicio incluido en el programa
  de la sesión aún necesita una regla del dominio, no está garantizada por las FK.
- Aún no se implementan controles de acceso, cifrado integral ni consentimiento
  para información clínica. No utilizar con datos de pacientes reales.
