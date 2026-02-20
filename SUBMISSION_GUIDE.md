
# Submission Instructions


## ✅ Completion Checklist

- [x] Task 1: Location - `LocationGateway.resolveByIdentifier()` implemented
- [x] Task 2: Store - `StoreResource` with persistence before gateway
- [x] Task 3: Warehouse - Full CRUD with validations
- [x] BONUS: Warehouse + Product + Store association with 3 constraints
- [x] QUESTIONS.md answered (3 responses)
- [x] CASE_STUDY.md answered (5 scenarios)
- [x] JUnit Tests implemented (20 tests)
- [x] JaCoCo Code Coverage configured (>=80%)
- [x] Code compiles without errors


## 📋 Project Structure

```
java-assignment/
├── src/main/java/
│   └── com/fulfilment/application/monolith/
│       ├── location/
│       │   └── LocationGateway.java ✅
│       ├── stores/
│       │   ├── Store.java
│       │   ├── StoreResource.java ✅
│       │   └── LegacyStoreManagerGateway.java
│       ├── products/
│       │   ├── Product.java
│       │   ├── ProductResource.java
│       │   └── ProductRepository.java
│       ├── warehouses/
│       │   ├── adapters/
│       │   │   ├── database/
│       │   │   │   ├── DbWarehouse.java
│       │   │   │   └── WarehouseRepository.java ✅
│       │   │   └── restapi/
│       │   │       └── WarehouseResourceImpl.java ✅
│       │   └── domain/
│       │       ├── models/
│       │       │   ├── Warehouse.java
│       │       │   └── Location.java
│       │       ├── usecases/
│       │       │   ├── CreateWarehouseUseCase.java ✅
│       │       │   ├── ArchiveWarehouseUseCase.java ✅
│       │       │   └── ReplaceWarehouseUseCase.java ✅
│       │       └── ports/
│       │           ├── WarehouseStore.java
│       │           ├── CreateWarehouseOperation.java
│       │           ├── ArchiveWarehouseOperation.java
│       │           └── ReplaceWarehouseOperation.java
│       └── fulfillment/
│           ├── adapters/
│           │   ├── database/
│           │   │   ├── DbWarehouseProductStoreAssociation.java ✅
│           │   │   └── WarehouseProductStoreAssociationRepository.java ✅
│           │   └── restapi/
│           │       └── FulfillmentResourceImpl.java ✅
│           └── domain/
│               ├── models/
│               │   └── WarehouseProductStoreAssociation.java ✅
│               └── usecases/
│                   └── AssociateWarehouseToProductInStoreUseCase.java ✅
├── src/test/java/
│   └── com/fulfilment/application/monolith/
│       └── warehouses/domain/usecases/
│           ├── CreateWarehouseUseCaseTest.java ✅ (6 testes)
│           ├── ArchiveWarehouseUseCaseTest.java ✅ (3 testes)
│           ├── ReplaceWarehouseUseCaseTest.java ✅ (5 testes)
│           └── fulfillment/domain/usecases/
│               └── AssociateWarehouseToProductInStoreUseCaseTest.java ✅ (6 testes)
├── pom.xml ✅ (with JaCoCo + dependencies)
├── CODE_ASSIGNMENT.md ✅ (tasks)
├── QUESTIONS.md ✅ (3 questions answered)
├── TESTING.md ✅ (test documentation)
└── README.md ✅ (instructions)
```


## 🚀 How to Submit


### 1. Prepare your local repository

```bash
cd c:\Users\rrber\projetos\fcs-interview-code-assignment-main


# Remove .env if present
rm -f .env.local


# Add all files
git add .


# Commit with descriptive message
git commit -m "feat: implement warehouse assignment with tests and coverage

- Task 1: Location.resolveByIdentifier()
- Task 2: Store with guaranteed legacy sync
- Task 3: Warehouse CRUD with validations
- BONUS: Fulfill association with constraints
- Questions and case study responses
- 20 JUnit tests with 80%+ code coverage
- JaCoCo configured for CI/CD"
```


### 2. Push to GitHub

```bash

# Push
git push origin main

# Or if using another branch
git push origin your-branch-name
```


### 3. Share the Link

Send to Hari/Shayal:
```
GitHub Repo: https://github.com/your-username/your-repo
Branch: main (or your-branch)
Status: Ready for review
Coverage: 80%+
Tests: 20 JUnit
```


## 🧪 Validate Before Submitting

```bash

# 1. Clean and compile
./mvnw clean compile

# 2. Run tests
./mvnw test

# 3. Check coverage
./mvnw test jacoco:report

# 4. View report
open target/site/jacoco/index.html  # macOS
start target/site/jacoco/index.html # Windows
xdg-open target/site/jacoco/index.html # Linux

# 5. Check if coverage >= 80%
./mvnw jacoco:check
```


## 📊 Expected Coverage

```
CreateWarehouseUseCase:        95%+ ✅
ArchiveWarehouseUseCase:       90%+ ✅
ReplaceWarehouseUseCase:       95%+ ✅
AssociateUseCase:              90%+ ✅
Overall:                       80%+ ✅
```


## ✍️ Documented Answers

### QUESTIONS.md
1. ✅ Database strategies - refactoring to Repository Pattern
2. ✅ OpenAPI vs direct code - pros/cons analysis
3. ✅ Testing - Pyramid Test Strategy

### CASE_STUDY.md
1. ✅ Scenario 1: Cost Allocation - challenges and solutions
2. ✅ Scenario 2: Cost Optimization - strategies and prioritization
3. ✅ Scenario 3: Financial Integration - importance and implementation
4. ✅ Scenario 4: Budgeting & Forecasting - design and considerations
5. ✅ Scenario 5: Warehouse Replacement - cost control and history


## 📦 Additional Dependencies

```xml
<!-- Testing -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
</dependency>
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest</artifactId>
</dependency>

<!-- Code Coverage -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
</plugin>
```


## 🎯 Evaluation Points

- ✅ **Code**: Correct implementation of all requirements
- ✅ **Tests**: 20 JUnit tests covering success + failures
- ✅ **Coverage**: >=80% JaCoCo
- ✅ **Documentation**: CODE_ASSIGNMENT + QUESTIONS + CASE_STUDY complete
- ✅ **Best Practices**:
    - Clean code
    - Exception handling
    - Design patterns (Repository, Use Case)
    - Logging (where appropriate)
    - Business validations

## ⏰ Deadline

**Mon, February 23 - EoD**

---

**Status**: ✅ READY FOR SUBMISSION

Any questions → Contact Hari/Shayal
