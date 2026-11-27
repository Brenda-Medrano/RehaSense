# RehaSense Parche 03: cambia SOLO el lanzador de Android a AuthActivity.
# Mantiene MainActivity y todos los recursos de Etapa 1.
param([switch]$CheckOnly)
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$root = (Get-Location).Path
$manifestPath = Join-Path $root 'app/src/main/AndroidManifest.xml'
$authPath = Join-Path $root 'app/src/main/java/com/rehasense/app/presentation/auth/AuthActivity.kt'
$contractPath = Join-Path $root 'app/src/main/java/com/rehasense/app/domain/repository/AuthRepository.kt'

foreach ($path in @($manifestPath, $authPath, $contractPath)) {
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) { throw "Falta el archivo esperado: $path" }
}
$branch = (& git branch --show-current 2>$null).Trim()
if ($LASTEXITCODE -ne 0 -or $branch -notlike 'feature/stage-2-p03-*') {
    throw "Rama inesperada '$branch'. Crea y utiliza feature/stage-2-p03-auth-ui antes de ejecutar."
}

$original = [System.IO.File]::ReadAllText($manifestPath, [System.Text.Encoding]::UTF8)
$doc = New-Object System.Xml.XmlDocument
$doc.PreserveWhitespace = $true
$doc.LoadXml($original)
$androidNs = 'http://schemas.android.com/apk/res/android'
$app = $doc.SelectSingleNode('/manifest/application')
if ($null -eq $app) { throw 'No se encontró <application> en AndroidManifest.xml.' }

function AndroidName([System.Xml.XmlElement]$element, [string]$ns) {
    return $element.GetAttribute('name', $ns)
}

$found = @()
foreach ($activity in @($app.SelectNodes('activity'))) {
    $name = AndroidName $activity $androidNs
    if ($name -eq '.presentation.auth.AuthActivity' -or
        $name -eq 'com.rehasense.app.presentation.auth.AuthActivity') {
        throw 'AuthActivity ya está declarada. No repitas la integración del lanzador.'
    }
    foreach ($filter in @($activity.SelectNodes('intent-filter'))) {
        $isMain = $false; $isLauncher = $false
        foreach ($action in @($filter.SelectNodes('action'))) {
            if ((AndroidName $action $androidNs) -eq 'android.intent.action.MAIN') { $isMain = $true }
        }
        foreach ($category in @($filter.SelectNodes('category'))) {
            if ((AndroidName $category $androidNs) -eq 'android.intent.category.LAUNCHER') { $isLauncher = $true }
        }
        if ($isMain -and $isLauncher) {
            $found += ,@($activity, $filter)
        }
    }
}
if ($found.Count -ne 1) {
    throw "Esperaba un único lanzador Android; encontré $($found.Count). No se ha modificado el manifiesto."
}
$oldActivity = $found[0][0]
$oldFilter = $found[0][1]
$oldName = AndroidName $oldActivity $androidNs
if ($oldName -ne '.MainActivity' -and $oldName -ne 'com.rehasense.app.MainActivity') {
    throw "El lanzador anterior no es MainActivity sino '$oldName'. Requiere revisión manual."
}

# MainActivity conserva Room y aún NO comprueba autorizaciones clínicas.
# Evitar que otra aplicación Android la inicie con un intent explícito.
if (@($oldActivity.SelectNodes('intent-filter')).Count -ne 1) {
    throw 'MainActivity tiene filtros adicionales: revisión manual obligatoria. No se modificó el manifiesto.'
}
[void]$oldActivity.RemoveChild($oldFilter)
$oldActivity.SetAttribute('exported', $androidNs, 'false')
$newActivity = $doc.CreateElement('activity')
$newActivity.SetAttribute('name', $androidNs, '.presentation.auth.AuthActivity')
$newActivity.SetAttribute('exported', $androidNs, 'true')
$newActivity.SetAttribute('label', $androidNs, 'RehaSense - Acceso')
[void]$newActivity.AppendChild($oldFilter)
[void]$app.InsertBefore($newActivity, $oldActivity)

$settings = New-Object System.Xml.XmlWriterSettings
$settings.Encoding = New-Object System.Text.UTF8Encoding($false)
$settings.Indent = $false
$settings.CloseOutput = $false
$ms = New-Object System.IO.MemoryStream
$writer = [System.Xml.XmlWriter]::Create($ms, $settings)
try {
    $doc.Save($writer)
    $writer.Flush()
    $updated = [System.Text.Encoding]::UTF8.GetString($ms.ToArray())
} finally {
    $writer.Dispose()
    $ms.Dispose()
}

if ($CheckOnly) {
    Write-Host "VALIDACIÓN OK. Launcher actual: $oldName. Nuevo: .presentation.auth.AuthActivity. No se realizaron cambios."
    exit 0
}
[System.IO.File]::WriteAllText($manifestPath, $updated, (New-Object System.Text.UTF8Encoding($false)))
Write-Host 'Manifiesto actualizado. Verifica: git diff -- app/src/main/AndroidManifest.xml'
