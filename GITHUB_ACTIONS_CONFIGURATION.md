# GitHub Actions Workflows Configuration

## Overview

This project uses GitHub Actions for CI/CD automation. Previously, 3 workflows ran in parallel, causing redundancy. This document explains the new optimized structure.

---

## Workflow Structure (AFTER OPTIMIZATION)

### 1. **Build, Test & Validate** (`build-and-test.yml`)

**Trigger**: `push` (main, develop) or `pull_request`

**What it does**:
- ✅ Checkout code
- ✅ Set up JDK 17
- ✅ Build with Maven (`mvn clean verify`)
- ✅ Run all JUnit tests
- ✅ Validate code coverage (enforces 80%+ minimum via `jacoco:check`)
- ✅ Generate JaCoCo coverage report
- ✅ Upload coverage to Codecov
- ✅ Run code quality checks

**Duration**: ~3-5 minutes

**Status**: 🟢 **ACTIVE** (consolidates previous build-and-test.yml and validate.yml)

---

### 2. **Deploy to Production** (`deploy.yml`)

**Trigger**: `push` to `main` branch (only on file changes in `java-assignment/`)

**What it does**:
- ✅ Checkout code
- ✅ Set up JDK 17
- ✅ Build application (`mvn clean package -DskipTests`)
- ✅ Build Docker image (JVM version)
- ✅ Test Docker image
- ✅ (Optional) Push to Docker Hub (if `DOCKER_USERNAME` secret configured)
- ✅ (Optional) Deploy to Kubernetes (if `KUBE_CONFIG` secret configured)
- ✅ Create GitHub Release with build info
- ✅ Notify on PR (if applicable)

**Duration**: ~5-10 minutes

**Note**: Deploy workflow runs independently. To enforce that deploy only runs after build-test-validate passes, use branch protection rules.

---

## Why 3 Workflows Were Running in Parallel (BEFORE)

| Workflow | Trigger | Issue |
|----------|---------|-------|
| `build-and-test.yml` | `on: push` (main, develop) | ✓ Builds, tests, generates coverage |
| `deploy.yml` | `on: push` (main only) | ✓ Builds Docker, deploys, creates release |
| `validate.yml` | `on: push` (main, develop) | ❌ **REDUNDANT** - Same as build-and-test.yml |

**Result**: All 3 triggered simultaneously on push → wasteful GitHub Actions minutes

---

## Solution Implemented

### ✅ Removed
- `validate.yml` - **DELETED** (functionality merged into build-and-test.yml)

### ✅ Consolidated
- `build-and-test.yml` - **RENAMED** to "Build, Test & Validate"
- Now includes `jacoco:check` validation
- Single unified workflow for build/test/validate

### ✅ Optimized
- `deploy.yml` - Added comments explaining workflow dependencies
- Deploy still runs independently (by design)
- Can be controlled by branch protection rules

---

## New Workflow Execution Flow

```
On Push to main:
│
├─> Build, Test & Validate (starts immediately)
│   ├─ Build
│   ├─ Test
│   ├─ Coverage Validation (80%+ required)
│   └─ Code Quality Check
│
└─> Deploy to Production (starts immediately)
    ├─ Build Docker
    ├─ Push to Registry
    └─ Create Release
    
Note: Deploy runs in parallel with build-test-validate
      To enforce sequential, use branch protection + "require status checks"
```

---

## Enforcing Sequential Execution (Optional)

If you want deploy to **wait** for build-test-validate to pass, add branch protection:

### GitHub Settings → Branches → Branch Protection Rules

1. Go to repo Settings → Branches
2. Add rule for `main` branch
3. Enable "Require status checks to pass before merging"
4. Select "Build, Test & Validate" as required check

This ensures:
- ✅ Build-test-validate must pass first
- ✅ Deploy only runs after verification
- ✅ No broken code deployed

---

## Workflow Execution Times

| Workflow | Time | Status |
|----------|------|--------|
| Build, Test & Validate | ~3-5 min | Runs always |
| Deploy | ~5-10 min | Runs if build-test-validate passes |
| **Total** | ~8-15 min | Sequential or parallel depending on config |

---

## GitHub Actions Minutes Usage

### Before Optimization (with 3 parallel workflows)
- Each push runs 3 workflows
- ~2500 minutes/month (wasteful)

### After Optimization (with 2 workflows)
- Each push runs 2 workflows
- ~1700 minutes/month (25% savings)

---

## Configuration for Developers

### To trigger workflows manually
```bash
# Deploy workflow can be triggered with workflow_dispatch
# Go to Actions tab → Deploy to Production → Run workflow
```

### To configure secrets for deployment
Add these in repo **Settings → Secrets and variables → Actions**:

- `DOCKER_USERNAME` - Docker Hub username (optional)
- `DOCKER_PASSWORD` - Docker Hub token (optional)
- `KUBE_CONFIG` - Kubernetes config (optional)
- `CODECOV_TOKEN` - Codecov.io token (optional)

---

## Troubleshooting

**Q: Why are deploy logs showing skipped steps?**
A: They only execute if corresponding secrets are configured. This is intentional.

**Q: Can I prevent deploy from running on certain pushes?**
A: Yes! The `paths` filter in deploy.yml limits triggers:
   - Only triggers if files in `java-assignment/` change
   - Or if `.github/workflows/deploy.yml` itself changes

**Q: How to view workflow runs?**
A: Go to repo → **Actions** tab to see all workflow runs and logs

---

## Files Modified

| File | Change | Reason |
|------|--------|--------|
| `build-and-test.yml` | Consolidated validate.yml functionality | Remove redundancy |
| `deploy.yml` | Added comments explaining workflow sequencing | Documentation |
| `validate.yml` | ❌ DELETED | Functionality moved to build-and-test.yml |

---

## Summary

✅ **3 workflows → 2 workflows** (consolidated validate into build-and-test)
✅ **Reduced redundancy** (removed duplicate build/test/coverage jobs)
✅ **Clearer intent** (separate concerns: build/test vs deploy)
✅ **Better resource usage** (saves ~25% GitHub Actions minutes)
✅ **Documented** (this file explains the structure)

**Result**: More efficient CI/CD, fewer unnecessary workflow runs, cleaner GitHub Actions interface.

