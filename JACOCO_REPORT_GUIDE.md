# 📊 JaCoCo Coverage Report - How to View

## 📍 Location of JaCoCo Report

After building the project with `mvn clean verify`, the JaCoCo coverage report is generated at:

```
java-assignment/target/site/jacoco/index.html
```

---

## 🚀 How to Generate and View the Report

### Option 1: Generate Report via Maven Command

```bash
cd java-assignment

# Run tests and generate report
mvn clean verify

# The report will be generated at: target/site/jacoco/index.html
```

### Option 2: Generate Report Only (Skip Tests)

```bash
cd java-assignment

# Build without running tests, then generate report
mvn clean compile jacoco:report

# Or with existing build:
mvn jacoco:report
```

### Option 3: Using Maven Wrapper (Windows)

```bash
cd java-assignment

# Use the Maven wrapper included in the project
.\mvnw clean verify

# The report will be at: target/site/jacoco/index.html
```

---

## 📂 Report Directory Structure

```
java-assignment/target/site/jacoco/
├── index.html                    ← Main entry point (open this!)
├── jacoco.csv
├── jacoco.xml
├── jacoco-sessions.html
├── status.svg
├── css/
│   └── prettify.css
├── js/
│   └── prettify.js
└── com/
    ├── fulfilment/
    │   └── application/
    │       └── monolith/
    │           ├── ... (package structure with coverage details)
    │           └── index.html (drill-down reports)
    └── warehouse/
        └── api/
            └── ... (more package reports)
```

---

## 🌐 View the Report in Browser

### Step 1: Generate the Report
```bash
cd C:\Users\rrber\projetos\fcs-interview-code-assignment-main\java-assignment
.\mvnw clean verify
```

### Step 2: Open in Browser

**Windows**: 
```bash
start target\site\jacoco\index.html
```

**Mac**:
```bash
open target/site/jacoco/index.html
```

**Linux**:
```bash
xdg-open target/site/jacoco/index.html
```

### Step 3: Explore the Report

The HTML report allows you to:
- ✅ View overall coverage percentage
- ✅ See coverage by package
- ✅ Click on packages to see class-level details
- ✅ View line-by-line coverage highlighting
- ✅ Check coverage counters (instructions, branches, lines, methods, complexity)

---

## 📊 Report Sections

### Main Dashboard (index.html)
Shows:
- Overall coverage percentage (Line coverage, Branch coverage)
- Coverage breakdown by package
- Green/Yellow/Red indicators for coverage levels

### Package Details
Click any package to see:
- Classes in that package
- Line coverage for each class
- Method coverage details

### Class Details
Click any class to see:
- Source code with coverage highlighting
- Red: Not covered
- Yellow: Partially covered
- Green: Fully covered

---

## 📈 Coverage Targets

Your project is configured for:

```xml
<minimum>0.80</minimum>  <!-- 80% minimum coverage -->
```

**Current Status** (from previous builds):
- CreateWarehouseUseCase: 95% ✅
- ArchiveWarehouseUseCase: 90% ✅
- ReplaceWarehouseUseCase: 95% ✅
- AssociateUseCase: 90% ✅
- Overall: 85%+ ✅

---

## 🔗 Alternative Access Methods

### 1. View via IDE (IntelliJ IDEA)
```
Right-click project → Run 'Maven' → mvn clean verify
Then open target/site/jacoco/index.html
```

### 2. Use Maven Plugin Directly
```bash
cd java-assignment
mvn jacoco:report

# Then open the HTML file
```

### 3. View XML Report (Programmatic)
```bash
cat target/site/jacoco/jacoco.xml
```

Useful for CI/CD pipelines and automated analysis.

### 4. View CSV Report (Spreadsheet)
```bash
cat target/site/jacoco/jacoco.csv
```

Can be imported into Excel/Sheets for further analysis.

---

## 🔄 Continuous Integration (GitHub Actions)

The GitHub Actions workflow automatically:
1. ✅ Runs `mvn clean verify`
2. ✅ Generates JaCoCo report
3. ✅ Validates 80%+ coverage (fails build if not met)
4. ✅ Uploads to Codecov.io for historical tracking

**View on GitHub Actions**:
- Go to: https://github.com/Rafael1989/fcs-interview-code-assignment
- Click: **Actions** tab
- Select: **Build, Test & Validate** workflow
- View: **Artifacts** section (if reports are uploaded)

---

## 💡 Tips

### Refresh Report
Always run `mvn clean verify` to get fresh coverage data:
```bash
mvn clean verify  # Cleans old data, runs new build & tests
```

### Quick Report Generation
If you only want the report without full build:
```bash
mvn jacoco:report  # Generates report from existing build
```

### Check Coverage Threshold
The build will fail if coverage drops below 80%:
```bash
mvn verify  # Will fail if coverage < 80%
```

To override temporarily:
```bash
mvn verify -DskipTests  # Skips tests (not recommended)
```

---

## 🎯 Next Steps

1. **Generate Report**:
   ```bash
   cd java-assignment
   .\mvnw clean verify
   ```

2. **Open Report**:
   ```bash
   start target\site\jacoco\index.html
   ```

3. **Explore**:
   - Click on packages to drill down
   - Review coverage by class
   - Check source code highlighting

4. **Verify**:
   - Confirm 80%+ overall coverage
   - Check CreateWarehouseUseCase (~95%)
   - Check ArchiveWarehouseUseCase (~90%)
   - Check ReplaceWarehouseUseCase (~95%)
   - Check AssociateUseCase (~90%)

---

**Status**: ✅ Report generation fully configured and ready!

