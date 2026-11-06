# Wallpaper Engine scene.pkg unpack / repack tool
# Format: uint32 ver(8) + "PKGV0021"(8) + uint32 numFiles
#         then per file: uint32 nameLen + name + uint32 offset + uint32 size
#         data region (uncompressed) follows the file table
param(
    [Parameter(Mandatory=$true)][ValidateSet("unpack","repack")][string]$Action,
    [Parameter(Mandatory=$true)][string]$Pkg,
    [Parameter(Mandatory=$false)][string]$Dir
)

function Read-U32($arr, [ref]$pos) {
    $v = [BitConverter]::ToUInt32($arr, $pos.Value)
    $pos.Value += 4
    return $v
}

if ($Action -eq "unpack") {
    $bytes = [System.IO.File]::ReadAllBytes($Pkg)
    $pos = 0
    $null = Read-U32 $bytes ([ref]$pos)            # ver
    $pos += 8                                       # magic PKGV0021
    $numFiles = Read-U32 $bytes ([ref]$pos)
    $entries = New-Object System.Collections.ArrayList
    for ($i=0; $i -lt $numFiles; $i++) {
        $nameLen = Read-U32 $bytes ([ref]$pos)
        $name = [System.Text.Encoding]::UTF8.GetString($bytes, $pos, $nameLen); $pos += $nameLen
        $off  = Read-U32 $bytes ([ref]$pos)
        $size = Read-U32 $bytes ([ref]$pos)
        $null = $entries.Add([pscustomobject]@{ Name=$name; Offset=$off; Size=$size })
    }
    $dataStart = $pos
    $manifest = New-Object System.Collections.ArrayList
    foreach ($e in $entries) {
        $abs = $dataStart + $e.Offset
        $data = New-Object byte[] $e.Size
        [Array]::Copy($bytes, $abs, $data, 0, $e.Size)
        $dest = Join-Path $Dir $e.Name
        $destDir = Split-Path $dest -Parent
        if ($destDir -and -not (Test-Path $destDir)) { New-Item -ItemType Directory -Force -Path $destDir | Out-Null }
        [System.IO.File]::WriteAllBytes($dest, $data)
        $null = $manifest.Add([pscustomobject]@{ Name=$e.Name; Offset=$e.Offset; Size=$e.Size })
    }
    $manifest | ConvertTo-Json -Depth 3 | Set-Content -Path (Join-Path $Dir "_manifest.json") -Encoding UTF8
    Write-Output "Unpacked $numFiles files to $Dir (dataStart=$dataStart)"
}
elseif ($Action -eq "repack") {
    $manifest = [System.IO.File]::ReadAllText((Join-Path $Dir "_manifest.json"), [System.Text.Encoding]::UTF8) | ConvertFrom-Json
    $files = @()
    $offset = 0
    foreach ($m in $manifest) {
        $p = Join-Path $Dir $m.Name
        if (-not (Test-Path $p)) { throw "Missing file: $($m.Name)" }
        $raw = [System.IO.File]::ReadAllBytes($p)
        $files += [pscustomobject]@{ Name=$m.Name; Offset=$offset; Size=$raw.Length; Data=$raw }
        $offset += $raw.Length
    }
    # build header + table
    $ms = New-Object System.IO.MemoryStream
    $bw = New-Object System.IO.BinaryWriter($ms)
    $bw.Write([UInt32]8)
    $enc = [System.Text.Encoding]::ASCII
    $bw.Write($enc.GetBytes("PKGV0021"))
    $bw.Write([UInt32]$files.Count)
    foreach ($f in $files) {
        $nameBytes = [System.Text.Encoding]::UTF8.GetBytes($f.Name)
        $bw.Write([UInt32]$nameBytes.Length)
        $bw.Write($nameBytes)
        $bw.Write([UInt32]$f.Offset)
        $bw.Write([UInt32]$f.Size)
    }
    foreach ($f in $files) { $bw.Write($f.Data) }
    $bw.Flush()
    [System.IO.File]::WriteAllBytes($Pkg, $ms.ToArray())
    $bw.Dispose(); $ms.Dispose()
    Write-Output "Repacked $($files.Count) files -> $Pkg ($($ms.Length) bytes)"
}
