# Why Codecov Coverage is Not Showing - Troubleshooting Guide

## 📋 Issue: Store Constructor Tests Not Appearing on Codecov

### Root Causes

1. **GitHub Actions Workflow Not Running Yet**
   - The workflow may not have triggered automatically
   - Need to wait for the workflow to complete

2. **Missing Base Commit**
   - Codecov shows "Unable to compare commits because no base commit was found"
   - This is normal for the first upload

3. **Codecov Token Not Configured**
   - Without a token, Codecov may not process the report immediately
   - Public repositories can work without token, but with slower processing

4. **JaCoCo Report Not Generated**
   - If tests don't run, no coverage report is created
   - Build must succeed for report generation

## ✅ Solution Steps

### Step 1: Verify GitHub Actions Ran

1. Go to: https://github.com/Rafael1989/fcs-interview-code-assignment/actions
2. Check if "Build and Test with Coverage" workflow completed
3. Look for green ✅ or red ❌ status

**Expected Steps:**
- ✓ Checkout
- ✓ Set up JDK 21
- ✓ Build and Test with Maven
- ✓ Generate JaCoCo Report
- ✓ Upload coverage to Codecov

### Step 2: Trigger Workflow Manually

If workflow hasn't run:

1. Go to: https://github.com/Rafael1989/fcs-interview-code-assignment/actions
2. Click "Build and Test with Coverage"
3. Click "Run workflow"
4. Select "main" branch
5. Click green "Run workflow" button

### Step 3: Wait for Processing

After workflow completes:
1. Wait 5-10 minutes for Codecov to process
2. Visit: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
3. Coverage should appear in "Commits" tab

### Step 4: (Optional) Add Codecov Token

For faster, more reliable uploads:

1. Go to: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment/settings
2. Copy the token
3. Go to: https://github.com/Rafael1989/fcs-interview-code-assignment/settings/secrets/actions
4. Click "New repository secret"
5. Name: `CODECOV_TOKEN`
6. Value: (paste token)
7. Click "Add secret"

## 📊 About Store Constructor Tests

The `StoreTest.java` file contains:

✅ **testStoreDefaultConstructor()** - Covers: `Store()`
✅ **testStoreConstructorWithName()** - Covers: `Store(String name)`
✅ **testStoreConstructorWithNameAndStock()** - Covers: `Store(String name, int stock)`
✅ **testStoreToString()** - Covers: `toString()` method
✅ **testStoreToStringWithNullName()** - Covers: `toString()` with null
✅ **testStoreConstructorMultipleInstances()** - Covers: Multiple instances
✅ **testStoreFieldAccess()** - Covers: Field access and modification

## 🔍 How to Verify Tests Run Locally

```bash
# Build and test
./mvnw clean test

# Generate coverage
./mvnw jacoco:report

# View report
# Open: target/site/jacoco/index.html
```

## ⏱️ Timeline

- **Now:** Tests are committed to GitHub
- **0-2 min:** Workflow triggers (check Actions tab)
- **2-5 min:** Tests run and JaCoCo report generated
- **5-10 min:** Codecov processes report
- **10+ min:** Coverage appears on Codecov dashboard

## 🎯 Expected Coverage for Store.java

After workflow completes, you should see:

```
Store.java
- Line coverage: 100% (all constructors tested)
- Branch coverage: 100% (all branches covered)
- Method coverage: 100% (all methods tested)
```

## 📝 Quick Checklist

- [ ] GitHub Actions workflow completed (green checkmark)
- [ ] JaCoCo report generated (look in logs)
- [ ] Codecov received the report (check email for notification)
- [ ] Coverage appears in Codecov dashboard (5-10 minutes after upload)
- [ ] Store constructor coverage shows 100%

## 🔗 Useful Links

- **GitHub Actions:** https://github.com/Rafael1989/fcs-interview-code-assignment/actions
- **Codecov:** https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
- **Settings:** https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment/settings

---

**Note:** Store.java tests have been added and committed. Once GitHub Actions runs, coverage will appear on Codecov!

**Date:** February 22, 2026  
**Author:** Rafael Romao Bertoni

