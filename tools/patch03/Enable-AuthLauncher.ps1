# RehaSense - Stage 2 / Patch 03
# Windows PowerShell 5.1 / PowerShell 7
# Does not depend on the output of git branch --show-current.
# Usage:
# powershell -NoProfile -ExecutionPolicy Bypass -File '.\tools\patch03\Enable-AuthLauncher.ps1' -CheckOnly
# powershell -NoProfile -ExecutionPolicy Bypass -File '.\tools\patch03\Enable-AuthLauncher.ps1'
param([switch]$CheckOnly)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# Resolve from the script location, not from the terminal's current directory.
$root = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..\..'))
$manifestPath = Join-Path $root 'app\src\main\AndroidManifest.xml'
$authPath = Join-Path $root 'app\src\main\java\com\rehasense\app\presentation\auth\AuthActivity.kt'
$contractPath = Join-Path $root 'app\src\main\java\com\rehasense\app\domain\repository\AuthRepository.kt'

foreach ($path in @($manifestPath, $authPath, $contractPath)) {
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) {
        throw "Required project file is missing: $path"
    }
}

$original = [System.IO.File]::ReadAllText($manifestPath, [System.Text.Encoding]::UTF8)
$doc = New-Object System.Xml.XmlDocument
$doc.PreserveWhitespace = $true
try {
    $doc.LoadXml($original)
} catch {
    throw "AndroidManifest.xml is not valid XML: $($_.Exception.Message)"
}

$androidNs = 'http://schemas.android.com/apk/res/android'
$app = $doc.SelectSingleNode('/manifest/application')
if ($null -eq $app) {
    throw 'AndroidManifest.xml must contain a manifest/application element.'
}

# Do not migrate twice and do not overwrite another launcher.
$launchers = New-Object System.Collections.ArrayList
foreach ($activity in $app.SelectNodes('activity')) {
    $activityName = $activity.GetAttribute('name', $androidNs)
    if ($activityName -eq '.presentation.auth.AuthActivity' -or
        $activityName -eq 'com.rehasense.app.presentation.auth.AuthActivity') {
        throw 'AuthActivity is already in AndroidManifest.xml. Nothing was changed.'
    }

    foreach ($filter in $activity.SelectNodes('intent-filter')) {
        $hasMain = $false
        $hasLauncher = $false
        foreach ($action in $filter.SelectNodes('action')) {
            if ($action.GetAttribute('name', $androidNs) -eq 'android.intent.action.MAIN') {
                $hasMain = $true
            }
        }
        foreach ($category in $filter.SelectNodes('category')) {
            if ($category.GetAttribute('name', $androidNs) -eq 'android.intent.category.LAUNCHER') {
                $hasLauncher = $true
            }
        }
        if ($hasMain -and $hasLauncher) {
            [void]$launchers.Add([pscustomobject]@{ Activity = $activity; Filter = $filter })
        }
    }
}

if ($launchers.Count -ne 1) {
    throw "Expected one existing launcher. Found $($launchers.Count). Nothing was changed."
}
$oldActivity = $launchers[0].Activity
$oldFilter = $launchers[0].Filter
$oldName = $oldActivity.GetAttribute('name', $androidNs)
if ($oldName -ne '.MainActivity' -and $oldName -ne 'com.rehasense.app.MainActivity') {
    throw "The launcher is $oldName, not MainActivity. Nothing was changed."
}
if ($oldActivity.SelectNodes('intent-filter').Count -ne 1) {
    throw 'MainActivity has additional intent filters. Nothing was changed.'
}

function Set-AndroidAttribute {
    param(
        [System.Xml.XmlElement]$Element,
        [string]$Name,
        [string]$Value,
        [string]$NamespaceUri
    )
    $attribute = $Element.OwnerDocument.CreateAttribute('android', $Name, $NamespaceUri)
    $attribute.Value = $Value
    [void]$Element.Attributes.SetNamedItem($attribute)
}

# Work on the in-memory XML tree only until every check passes.
[void]$oldActivity.RemoveChild($oldFilter)
Set-AndroidAttribute -Element $oldActivity -Name 'exported' -Value 'false' -NamespaceUri $androidNs

$newActivity = $doc.CreateElement('activity')
Set-AndroidAttribute -Element $newActivity -Name 'name' -Value '.presentation.auth.AuthActivity' -NamespaceUri $androidNs
Set-AndroidAttribute -Element $newActivity -Name 'exported' -Value 'true' -NamespaceUri $androidNs
Set-AndroidAttribute -Element $newActivity -Name 'label' -Value 'RehaSense - Acceso' -NamespaceUri $androidNs
[void]$newActivity.AppendChild($oldFilter)
[void]$app.InsertBefore($newActivity, $oldActivity)
[void]$app.InsertBefore($doc.CreateWhitespace("`n        "), $oldActivity)

$updated = $doc.OuterXml
$checkDoc = New-Object System.Xml.XmlDocument
$checkDoc.LoadXml($updated)
$checkApp = $checkDoc.SelectSingleNode('/manifest/application')
$checkNew = $null
$checkOld = $null
foreach ($activity in $checkApp.SelectNodes('activity')) {
    $name = $activity.GetAttribute('name', $androidNs)
    if ($name -eq '.presentation.auth.AuthActivity') { $checkNew = $activity }
    if ($name -eq '.MainActivity' -or $name -eq 'com.rehasense.app.MainActivity') { $checkOld = $activity }
}
if ($null -eq $checkNew -or $null -eq $checkOld -or
    $checkNew.GetAttribute('exported', $androidNs) -ne 'true' -or
    $checkOld.GetAttribute('exported', $androidNs) -ne 'false') {
    throw 'Updated manifest failed internal checks. Nothing was changed.'
}

Write-Host "Project: $root"
if ($CheckOnly) {
    Write-Host 'CHECK OK: MainActivity launcher can be moved to AuthActivity.'
    Write-Host 'No files were changed.'
    exit 0
}

$utf8NoBom = [System.Text.UTF8Encoding]::new($false)
[System.IO.File]::WriteAllText($manifestPath, $updated, $utf8NoBom)
Write-Host 'OK: AndroidManifest.xml updated.'
Write-Host 'Check with: git diff --check'
Write-Host 'Check with: git diff -- app/src/main/AndroidManifest.xml'
