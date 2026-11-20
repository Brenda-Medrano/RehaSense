# Room schemas

The Room Gradle plugin exports JSON schemas here during compilation. Commit the generated JSON files after a successful build. Do not create or edit them manually. The version 1 schema is a development baseline, exercised only with an in-memory database in patch 0007. No production database is opened yet. Once a persistent database is introduced, any released schema change must have an explicit tested migration; do not enable destructive fallback.
