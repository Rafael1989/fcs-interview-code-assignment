# CI/CD Pipeline Files

## Location of CI/CD Files

The CI/CD pipeline files have been created in the following locations:

### 1. Build and Test Workflow
**File:** `build-and-test.yml`
**Location:** Project Root or `.github/workflows/build-and-test.yml`

**Features:**
- ✅ Build with Maven
- ✅ Run all tests (78+)
- ✅ Generate JaCoCo coverage report
- ✅ Upload coverage to Codecov
- ✅ Archive test results
- ✅ Archive coverage report
- ✅ Code quality analysis (SonarQube)
- ✅ Security scanning (OWASP)

**Triggers:**
- Push to main or develop branches
- Pull requests to main or develop branches

### 2. Deploy Workflow
**File:** `deploy.yml`
**Location:** Project Root or `.github/workflows/deploy.yml`

**Features:**
- ✅ Build Maven project
- ✅ Create GitHub Releases
- ✅ Tag releases with version numbers
- ✅ Document build information

**Triggers:**
- Push to main branch
- Manual workflow dispatch

---

## How to Use the CI/CD Pipelines

### Step 1: Push to GitHub
```bash
cd C:/Users/rrber/projetos/fcs-interview-code-assignment-main
git add .
git commit -m "Complete FCS Interview Code Assignment with CI/CD"
git push origin main
```

### Step 2: GitHub Actions Automatically Runs
- Navigate to your repository → **Actions** tab
- You'll see the workflows running:
  - **Build and Test** - ~5-10 minutes
  - **Deploy** - ~5 minutes (only on main branch)

### Step 3: Monitor Execution
- Click on the workflow run to see details
- View logs for each job
- Download artifacts (test results, coverage report)

### Step 4: Check Results
- **Test Results**: Download from Artifacts section
- **Coverage Report**: Download and open `index.html`
- **Codecov**: Check codecov.io (if configured)

---

## Configuration

### Required Secrets (for Docker deployment)
Add these in GitHub repository Settings → Secrets:
- `DOCKER_USERNAME` - Docker Hub username
- `DOCKER_PASSWORD` - Docker Hub password/token
- `SONAR_TOKEN` - SonarQube token (optional)

### How to add secrets:
1. Go to repository → Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Add secret name and value

---

## Files Created

✅ `build-and-test.yml` - Build and test pipeline
✅ `deploy.yml` - Deployment pipeline
✅ `CI_CD_PIPELINE.md` - Documentation
✅ All workflows ready for GitHub

---

## Status

✅ CI/CD pipelines created and ready
✅ All configurations included
✅ Ready to push to GitHub
✅ Automated testing and deployment ready

**Next Step:** Push to GitHub and GitHub Actions will automatically run!


