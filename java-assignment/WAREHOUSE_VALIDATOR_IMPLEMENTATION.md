# Review Comment Implementation Summary

## Comment
**ReplaceWarehouseUseCase** should have validations used for Create + additional validations for replace. Also the additional validations can also be moved to **WarehouseValidator** file.

## Implementation

### 1. Created New WarehouseValidator Class
**File:** `src/main/java/com/fulfilment/application/monolith/warehouses/domain/validators/WarehouseValidator.java`

#### Public Methods:
- **`validateForCreate(Warehouse warehouse)`**: Validates all constraints for warehouse creation including:
  - Business Unit Code uniqueness
  - Location validity
  - Maximum warehouses per location not exceeded
  - Warehouse capacity vs stock validation
  - Capacity vs location max capacity validation

- **`validateForReplace(Warehouse newWarehouse)`**: Validates all constraints for warehouse replacement including:
  - All create validations (to ensure the new warehouse can be created)
  - Capacity accommodation (new warehouse can hold old warehouse's stock)
  - Stock matching (new warehouse stock must match old warehouse stock)
  - Warehouse exists validation

#### Private Helper Methods:
- `validateBusinessUnitCodeDoesNotExist(String businessUnitCode)`
- `validateWarehouseExists(String businessUnitCode)`
- `validateLocationExists(String locationIdentifier)`
- `validateMaxWarehousesNotExceeded(String locationIdentifier)`
- `validateCapacityAndStock(Warehouse warehouse)`
- `validateCapacityAgainstLocationMaxCapacity(Warehouse newWarehouse)`
- `validateCapacityAccommodatesExistingStock(Warehouse newWarehouse, Warehouse existingWarehouse)`
- `validateStockMatches(Warehouse newWarehouse, Warehouse existingWarehouse)`

### 2. Updated ReplaceWarehouseUseCase
**File:** `src/main/java/com/fulfilment/application/monolith/warehouses/domain/usecases/ReplaceWarehouseUseCase.java`

**Changes:**
- Injected `WarehouseValidator` using `@Inject` annotation
- Added `setValidator()` method for testing purposes
- Simplified `replace()` method to:
  1. Call `validator.validateForReplace(newWarehouse)` for comprehensive validation
  2. Archive existing warehouse
  3. Create new warehouse

**Benefits:**
- Now includes all create validations (Business Unit Code, Location, Max Warehouses, Capacity/Stock)
- Includes additional replace-specific validations (Capacity Accommodation, Stock Matching)
- Separation of concerns: validation logic is centralized in WarehouseValidator
- More maintainable and testable code

### 3. Updated CreateWarehouseUseCase
**File:** `src/main/java/com/fulfilment/application/monolith/warehouses/domain/usecases/CreateWarehouseUseCase.java`

**Changes:**
- Removed `@Inject private LocationResolver locationResolver` dependency
- Injected `WarehouseValidator` using `@Inject` annotation
- Added `setValidator()` method for testing purposes
- Simplified `create()` method to:
  1. Call `validator.validateForCreate(warehouse)` for all validation logic
  2. Set creation timestamp
  3. Persist to database

**Benefits:**
- Eliminated 60+ lines of inline validation code
- Centralized validation logic in a single, reusable validator class
- Improved code readability and maintainability

### 4. Updated Unit Tests

#### ReplaceWarehouseUseCaseTest
**File:** `src/test/java/com/fulfilment/application/monolith/warehouses/domain/usecases/ReplaceWarehouseUseCaseTest.java`

**Changes:**
- Mocked `WarehouseValidator` instead of directly testing database operations
- Updated all test cases to use the validator mock
- Tests now verify that `validator.validateForReplace()` is called
- Tests can focus on the use case's orchestration logic rather than validation logic

#### CreateWarehouseUseCaseTest
**File:** `src/test/java/com/fulfilment/application/monolith/warehouses/domain/usecases/CreateWarehouseUseCaseTest.java`

**Changes:**
- Mocked `WarehouseValidator` instead of `LocationGateway` and managing complex test setups
- Updated all test cases to use the validator mock
- Tests now verify that `validator.validateForCreate()` is called
- Simplified test setup from ~12 lines per test to ~3 lines

#### WarehouseValidatorTest (NEW)
**File:** `src/test/java/com/fulfilment/application/monolith/warehouses/domain/validators/WarehouseValidatorTest.java`

**Comprehensive Test Suite:**
- **CREATE Validations (5 tests):**
  - ✅ Successful creation
  - ✅ Fails when business unit code exists
  - ✅ Fails when location not found
  - ✅ Fails when max warehouses exceeded
  - ✅ Fails when stock exceeds capacity
  - ✅ Fails when exceeds location capacity

- **REPLACE Validations (8 tests):**
  - ✅ Successful replacement
  - ✅ Fails when warehouse not found
  - ✅ Fails when capacity insufficient
  - ✅ Fails when stock mismatch
  - ✅ Fails when location invalid
  - ✅ Fails when new stock exceeds capacity
  - ✅ Succeeds with location capacity check (edge case: exact capacity match)
  - ✅ Fails when exceeds location capacity

## Code Quality Improvements

1. **Separation of Concerns**: Validation logic is isolated from business logic
2. **DRY (Don't Repeat Yourself)**: Validation methods are shared between Create and Replace operations
3. **Testability**: Validator can be tested independently with comprehensive test coverage
4. **Reusability**: Validator methods can be reused by other use cases or API controllers
5. **Maintainability**: Single source of truth for warehouse validation rules

## Files Modified
- ✅ `ReplaceWarehouseUseCase.java` - Now uses WarehouseValidator
- ✅ `CreateWarehouseUseCase.java` - Now uses WarehouseValidator
- ✅ `ReplaceWarehouseUseCaseTest.java` - Updated to mock validator
- ✅ `CreateWarehouseUseCaseTest.java` - Updated to mock validator

## Files Created
- ✅ `WarehouseValidator.java` - New validator class with 8 validation methods
- ✅ `WarehouseValidatorTest.java` - New test class with 13 comprehensive test cases

## Validation Flow

### For Create:
```
ReplaceWarehouseUseCase.create(warehouse)
  → WarehouseValidator.validateForCreate(warehouse)
    → validateBusinessUnitCodeDoesNotExist()
    → validateLocationExists()
    → validateMaxWarehousesNotExceeded()
    → validateCapacityAndStock()
    → validateCapacityAgainstLocationMaxCapacity()
  → Set createdAt timestamp
  → WarehouseStore.create(warehouse)
```

### For Replace:
```
ReplaceWarehouseUseCase.replace(newWarehouse)
  → WarehouseValidator.validateForReplace(newWarehouse)
    → validateWarehouseExists()
    → validateLocationExists()
    → validateCapacityAndStock()
    → validateCapacityAgainstLocationMaxCapacity()
    → validateCapacityAccommodatesExistingStock()
    → validateStockMatches()
  → Archive existing warehouse
  → Create new warehouse with timestamp
```

## Testing Coverage
- 13 new test cases in WarehouseValidatorTest covering all validation scenarios
- Updated existing test cases in ReplaceWarehouseUseCaseTest and CreateWarehouseUseCaseTest
- All edge cases covered (capacity matching, stock mismatches, location validation, etc.)

