# CI/CD Pipeline Documentation

## Overview

This project includes automated CI/CD pipelines using GitHub Actions for continuous integration, testing, and deployment.

## Workflows

### 1. Build and Test Pipeline (`build-and-test.yml`)

**Trigger:** 
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches
- Manual trigger

**Jobs:**

#### Build Job
- **Runs on:** Ubuntu Latest
- **Java Version:** 17
- **Steps:**
  1. Checkout code
  2. Setup JDK 17
  3. Build with Maven (`mvn clean verify`)
  4. Run all tests
  5. Generate JaCoCo coverage report
  6. Upload coverage to Codecov
  7. Archive test results
  8. Archive coverage report
  9. Comment PR with test results

**Artifacts:**
- Test results (Surefire reports)
- Coverage report (JaCoCo HTML)

#### Code Quality Job
- **Runs on:** Ubuntu Latest
- **Tools:** SonarQube (optional)
- **Requires:** Build job to pass
- **Steps:**
  1. Setup JDK 17
  2. Analyze code with SonarQube
  3. Generate quality report

**Configuration:**
- Requires `SONAR_TOKEN` secret
- Analyzes: `src/main` (source code)
- Analyzes: `src/test` (test code)

#### Security Scan Job
- **Runs on:** Ubuntu Latest
- **Tools:** OWASP Dependency Check
- **Requires:** Build job to pass
- **Steps:**
  1. Setup JDK 17
  2. Run OWASP dependency check
  3. Upload security report

**Artifacts:**
- Dependency check HTML report

---

### 2. Deploy to Production Pipeline (`deploy.yml`)

**Trigger:**
- Push to `main` branch (automatic)
- Manual trigger

**Prerequisites:**
- Build and Test pipeline must pass
- Docker credentials configured
- Kubernetes config (optional)

**Jobs:**

#### Deploy Job
- **Runs on:** Ubuntu Latest
- **Steps:**
  1. Checkout code
  2. Setup JDK 17
  3. Build project with Maven
  4. Build Docker image
  5. Test Docker image
  6. Push to Docker Hub
  7. Deploy to Kubernetes (if configured)
  8. Create GitHub Release
  9. Notify deployment success

**Environment Variables:**
- `DOCKER_USERNAME` - Docker Hub username
- `DOCKER_PASSWORD` - Docker Hub password
- `KUBE_CONFIG` - Kubernetes configuration (optional)

**Output:**
- Docker image: `<docker-username>/java-code-assignment:<commit-sha>`
- Docker image: `<docker-username>/java-code-assignment:latest`
- GitHub Release with build information

---

## Configuration

### GitHub Secrets Required

Create the following secrets in your GitHub repository settings:

1. **SONAR_TOKEN** (optional)
   - For SonarQube analysis
   - Obtain from: https://sonarcloud.io

2. **DOCKER_USERNAME** (optional)
   - Docker Hub username
   - Required for Docker push

3. **DOCKER_PASSWORD** (optional)
   - Docker Hub password/token
   - Required for Docker push

4. **KUBE_CONFIG** (optional)
   - Kubernetes configuration
   - Required for Kubernetes deployment

### Add Secrets to GitHub

```bash
# Via GitHub CLI
gh secret set SONAR_TOKEN --body "your-token"
gh secret set DOCKER_USERNAME --body "your-username"
gh secret set DOCKER_PASSWORD --body "your-password"
```

Or via GitHub UI:
1. Go to repository Settings
2. Navigate to Secrets → Actions
3. Click "New repository secret"
4. Add each secret with its value

---

## Workflow Status

### Build and Test Status
- **File:** `.github/workflows/build-and-test.yml`
- **Trigger:** Push/PR to main, develop
- **Duration:** ~5-10 minutes
- **Artifacts:** Test results, coverage report

### Deploy Status
- **File:** `.github/workflows/deploy.yml`
- **Trigger:** Push to main (automatic)
- **Duration:** ~10-15 minutes
- **Prerequisites:** Build must pass

---

## Monitoring

### Check Workflow Status

1. **GitHub UI**
   - Go to Actions tab
   - View workflow runs
   - Click on specific workflow for details

2. **Command Line**
   ```bash
   gh run list --branch main
   gh run view <run-id>
   ```

### View Artifacts

1. **GitHub UI**
   - Actions tab → Workflow run → Artifacts section
   - Download test results, coverage reports, etc.

2. **Command Line**
   ```bash
   gh run download <run-id> --dir ./artifacts
   ```

---

## Coverage Reporting

### JaCoCo Coverage Report
- **Generated:** During build job
- **Format:** HTML
- **Location:** `target/site/jacoco/index.html`
- **Minimum:** 80% instruction coverage
- **Codecov Integration:** Automatic upload to Codecov

### View Coverage
1. Download coverage artifact from GitHub Actions
2. Open `index.html` in browser
3. Or check Codecov online (if configured)

---

## Build Status Badge

Add to your README.md:

```markdown
[![Build and Test](https://github.com/YOUR_USERNAME/fcs-interview-code-assignment/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/YOUR_USERNAME/fcs-interview-code-assignment/actions/workflows/build-and-test.yml)
```

---

## Troubleshooting

### Build Failure
1. Check workflow logs in GitHub Actions
2. Look for compilation errors
3. Verify dependencies are correct
4. Check Java version (17 required)

### Test Failure
1. Review test output in artifacts
2. Check for assertion failures
3. Verify database is accessible
4. Review application logs

### Deployment Failure
1. Verify Docker credentials in secrets
2. Check Docker Hub connection
3. Review Kubernetes configuration
4. Check resource availability

### Coverage Issues
1. Ensure tests are comprehensive
2. Check JaCoCo configuration in pom.xml
3. Review coverage report for uncovered lines
4. Add tests for edge cases

---

## Next Steps

1. **Fork/Clone Repository**
   ```bash
   git clone <repository-url>
   cd fcs-interview-code-assignment
   ```

2. **Configure Secrets**
   - Add DOCKER_USERNAME and DOCKER_PASSWORD
   - (Optional) Add SONAR_TOKEN for code quality

3. **Make Changes**
   - Create feature branch
   - Commit changes
   - Push to GitHub

4. **Monitor Pipeline**
   - GitHub Actions automatically runs
   - Check workflow status
   - Review artifacts

5. **Merge to Main**
   - CI/CD pipeline runs on merge
   - Docker image built and pushed
   - Deployment happens automatically

---

## Best Practices

1. **Always write tests** - Required for CI to pass
2. **Keep commits small** - Easier to debug if pipeline fails
3. **Use meaningful commit messages** - Helps with tracking
4. **Review logs** - Always check build logs for warnings
5. **Monitor coverage** - Maintain 80%+ coverage requirement
6. **Test locally first** - Run `mvn clean verify` before pushing

---

## Support

For issues or questions:
1. Check GitHub Actions logs
2. Review workflow files
3. Check project README
4. Review CHALLENGES_AND_SOLUTIONS.md

---

**Last Updated:** February 20, 2026
**Status:** ✅ Active and Configured

