$env:JAVA_HOME="C:\Users\rrber\.jdk\jdk-17.0.16(1)"
cd "C:\Users\rrber\projetos\fcs-interview-code-assignment-main\java-assignment"
Write-Host "Starting compilation..."
.\mvnw.cmd clean compile -DskipTests -q
if ($LASTEXITCODE -eq 0) {
    Write-Host "BUILD SUCCESS"
    exit 0
} else {
    Write-Host "BUILD FAILED with exit code: $LASTEXITCODE"
    exit 1
}

