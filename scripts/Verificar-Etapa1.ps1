# RehaSense / etapa 1. Ejecutar desde PowerShell:
# powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Verificar-Etapa1.ps1
# Añadir -AndroidTests cuando haya un emulador/dispositivo conectado.
param([switch]$AndroidTests)

$ErrorActionPreference = 'Stop'
$root = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
Push-Location $root
try {
    if (-not (Test-Path '.\gradlew.bat')) {
        throw 'No se encontró gradlew.bat en la raíz del proyecto.'
    }

    $amDirectory = (& git rev-parse --git-path rebase-apply).Trim()
    if ($LASTEXITCODE -ne 0) { throw 'No es un repositorio Git.' }
    if (Test-Path $amDirectory) {
        throw 'Existe una sesión git am/rebase pendiente. Ejecuta git status y resuélvela antes.'
    }

    Write-Host '== Estado Git ==' -ForegroundColor Cyan
    & git status -sb
    if ($LASTEXITCODE -ne 0) { throw 'Falló git status.' }

    Write-Host '== Compilación, pruebas JVM y Lint ==' -ForegroundColor Cyan
    & .\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --no-configuration-cache
    if ($LASTEXITCODE -ne 0) { throw 'Falla en compilación, pruebas unitarias o Lint.' }

    if ($AndroidTests) {
        $adbPath = Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'
        if (-not (Test-Path $adbPath)) {
            throw 'No se encontró adb.exe en el Android SDK local.'
        }
        $devices = & $adbPath devices
        if ($LASTEXITCODE -ne 0 -or -not ($devices | Where-Object { $_ -match '\s+device\s*$' })) {
            throw 'No hay emulador o dispositivo Android en estado device. Abre Device Manager.'
        }

        Write-Host '== Pruebas instrumentadas (navegación, Room y Activity) ==' -ForegroundColor Cyan
        & .\gradlew.bat :app:connectedDebugAndroidTest --no-configuration-cache
        if ($LASTEXITCODE -ne 0) {
            Write-Host 'Reporte: app\build\reports\androidTests\connected\debug\index.html'
            throw 'Al menos una prueba instrumentada falló. Consulta el reporte HTML.'
        }
    } else {
        Write-Host 'Pruebas instrumentadas omitidas. Repite con -AndroidTests.' -ForegroundColor Yellow
    }

    Write-Host 'VALIDACIÓN SOLICITADA COMPLETADA' -ForegroundColor Green
} finally {
    Pop-Location
}
