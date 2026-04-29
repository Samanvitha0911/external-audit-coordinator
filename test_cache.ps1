$URL = "http://localhost:8080/api/programs/1" # Change this ID to an existing record
$TOKEN = "Bearer YOUR_JWT_TOKEN_HERE"      # Replace with a valid token

Write-Host "===================================="
Write-Host "Testing Redis Cache (Hit vs Miss)"
Write-Host "===================================="

# Miss (First request, will hit database)
Write-Host "`n[1] First Request (Cache Miss)..."
$missTime = Measure-Command {
    try {
        $response = Invoke-RestMethod -Uri $URL -Method GET -Headers @{ Authorization = $TOKEN }
    } catch {
        Write-Host "Error: $_"
    }
}
Write-Host "Time taken (Miss): $($missTime.TotalMilliseconds) ms"

# Hit (Second request, should be served from Redis)
Write-Host "`n[2] Second Request (Cache Hit)..."
$hitTime = Measure-Command {
    try {
        $response = Invoke-RestMethod -Uri $URL -Method GET -Headers @{ Authorization = $TOKEN }
    } catch {
        Write-Host "Error: $_"
    }
}
Write-Host "Time taken (Hit): $($hitTime.TotalMilliseconds) ms"

# Verify improvement
if ($hitTime.TotalMilliseconds -lt $missTime.TotalMilliseconds) {
    Write-Host "`n✅ Cache is working! Hit was $([math]::Round($missTime.TotalMilliseconds - $hitTime.TotalMilliseconds)) ms faster." -ForegroundColor Green
} else {
    Write-Host "`n⚠️ Cache hit was not faster. Ensure Redis is running and the record exists." -ForegroundColor Yellow
}
