# Codecov Coverage Setup Guide

## ✅ Current Status

The GitHub Actions workflow has been configured to automatically:
1. ✅ Build the project with Maven
2. ✅ Execute all tests
3. ✅ Generate JaCoCo report in XML format
4. ✅ Automatically upload to Codecov

## 🔍 How to Verify Status

### 1. **Check GitHub Actions Logs**

1. Go to: https://github.com/Rafael1989/fcs-interview-code-assignment/actions
2. Click on the latest "Build and Test with Coverage" workflow
3. Verify it passed all steps:
   - ✓ Checkout
   - ✓ Set up JDK 21
   - ✓ Build and Test with Maven
   - ✓ Generate JaCoCo Report
   - ✓ List coverage files
   - ✓ Check JaCoCo XML exists
   - ✓ Upload coverage to Codecov
   - ✓ Upload Test Results
   - ✓ Upload Coverage Reports

### 2. **Check on Codecov**

1. Go to: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
2. Look for the **Commits** tab
3. Each commit should appear with:
   - Date/time
   - Coverage percentage
   - Status (✓ or ×)

### 3. **If Coverage Doesn't Appear**

Follow these steps:

#### a) **Add Codecov Token (Recommended)**

1. Go to: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment/settings
2. Copy the provided token
3. On GitHub, go to: https://github.com/Rafael1989/fcs-interview-code-assignment/settings/secrets/actions
4. Click "New repository secret"
5. Name: `CODECOV_TOKEN`
6. Value: (paste the Codecov token)
7. Click "Add secret"

#### b) **Check Upload Logs**

In GitHub Actions, search the "Upload coverage to Codecov" step for messages like:

```
Reports have been queued for processing
Reports have been processed successfully
Upload successful
```

#### c) **Verify JaCoCo XML is Being Generated**

In the "Check JaCoCo XML exists" step, look for:

```
✓ jacoco.xml found
File size: XXXXX bytes
```

If not found, it means the build is not generating the report.

## 📋 Quick Verification

```bash
# 1. Local build to generate JaCoCo
cd java-assignment
mvn clean verify

# 2. Generate report
mvn jacoco:report

# 3. Verify it was generated
ls -la target/site/jacoco/jacoco.xml

# 4. Check file size
wc -c target/site/jacoco/jacoco.xml
```

## 🔧 Troubleshooting

### Problem: "jacoco.xml not found"

**Solution:**
```bash
# 1. Clean and rebuild
mvn clean verify

# 2. Generate report explicitly
mvn jacoco:report

# 3. Verify it exists
find target -name "jacoco.xml"
```

### Problem: "Upload failed"

**Checklist:**
- [ ] Codecov token was added to GitHub Secrets
- [ ] The `jacoco.xml` file was generated locally
- [ ] The Codecov account exists
- [ ] The repository was connected to Codecov

### Problem: "Coverage not updating"

**Possible causes:**
1. Token not configured
2. File is not being generated
3. File path is incorrect
4. Codecov is processing (takes a few minutes)

**Solution:**
1. Wait 5-10 minutes
2. Make a new push to trigger the workflow again
3. Check logs in detail

## 📊 Expected Metrics

Based on the current project:

- **Instruction Coverage:** 70%+ (configured in pom.xml)
- **Branch Coverage:** Variable
- **Line Coverage:** Variable
- **Method Coverage:** 70%+

## 🚀 Next Steps

1. ✅ Wait for workflow to complete (2-5 minutes)
2. ✅ Check GitHub Actions logs
3. ✅ Visit Codecov to view coverage
4. ✅ If not appearing, add token and trigger again

## 📚 Useful Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Codecov Documentation](https://docs.codecov.io/)
- [JaCoCo Maven Plugin](https://www.eclemma.org/jacoco/trunk/doc/maven.html)
- [Codecov GitHub Action](https://github.com/codecov/codecov-action)

---

**Update Date:** February 22, 2026

**Status:** ✅ Configured and ready to use

