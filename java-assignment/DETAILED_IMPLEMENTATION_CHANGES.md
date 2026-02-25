# Detailed Implementation Changes

## File 1: WarehouseValidator.java (NEW - 188 lines)

### Class Structure
```java
@ApplicationScoped
public class WarehouseValidator {
  
  @Inject private WarehouseStore warehouseStore;
  @Inject private LocationResolver locationResolver;
  
  // Public methods
  public void validateForCreate(Warehouse warehouse)
  public void validateForReplace(Warehouse newWarehouse)
  
  // Private helper methods (8 total)
  private void validateBusinessUnitCodeDoesNotExist(String businessUnitCode)
  private Warehouse validateWarehouseExists(String businessUnitCode)
  private void validateLocationExists(String locationIdentifier)
  private void validateMaxWarehousesNotExceeded(String locationIdentifier)
  private void validateCapacityAndStock(Warehouse warehouse)
  private void validateCapacityAgainstLocationMaxCapacity(Warehouse newWarehouse)
  private void validateCapacityAccommodatesExistingStock(Warehouse newWarehouse, Warehouse existingWarehouse)
  private void validateStockMatches(Warehouse newWarehouse, Warehouse existingWarehouse)
}
```

### Key Methods

**validateForCreate()** - Validates 5 constraints
```java
✓ Business Unit Code uniqueness
✓ Location validity
✓ Max warehouses per location
✓ Capacity vs stock ratio
✓ Location capacity limits
```

**validateForReplace()** - Validates 6 constraints
```java
✓ Warehouse exists
✓ Business Unit Code refers to existing warehouse
✓ Location validity
✓ Capacity vs stock ratio
✓ Location capacity limits
✓ NEW: Capacity accommodation
✓ NEW: Stock matching
```

---

## File 2: ReplaceWarehouseUseCase.java (REFACTORED)

### BEFORE (39 lines with inline validation):
```java
@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {
  private final WarehouseStore warehouseStore;

  public void replace(Warehouse newWarehouse) {
    var existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

    // INLINE VALIDATION
    if (newWarehouse.capacity < existingWarehouse.stock) {
      throw new IllegalArgumentException(
          "New warehouse capacity cannot be less than existing warehouse stock");
    }

    if (!existingWarehouse.stock.equals(newWarehouse.stock)) {
      throw new IllegalArgumentException(
          "New warehouse stock must match existing warehouse stock");
    }

    existingWarehouse.archivedAt = LocalDateTime.now();
    warehouseStore.update(existingWarehouse);
    newWarehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(newWarehouse);
  }
}
```

### AFTER (43 lines, cleaner orchestration):
```java
@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {
  private final WarehouseStore warehouseStore;
  
  @Inject private WarehouseValidator validator;

  public void setValidator(WarehouseValidator validator) {
    this.validator = validator;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    // COMPREHENSIVE VALIDATION (now includes create validations + replace specific)
    validator.validateForReplace(newWarehouse);

    // ORCHESTRATION
    var existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
    existingWarehouse.archivedAt = LocalDateTime.now();
    warehouseStore.update(existingWarehouse);

    newWarehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(newWarehouse);
  }
}
```

### Changes Summary:
- ✅ Added WarehouseValidator injection
- ✅ Added setValidator() for testing
- ✅ Removed 2 inline validation blocks
- ✅ Added comprehensive validation call to validator
- ✅ Now includes all create validations + additional replace validations

---

## File 3: CreateWarehouseUseCase.java (REFACTORED)

### BEFORE (72 lines with inline validation):
```java
@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {
  private final WarehouseStore warehouseStore;
  
  @Inject private LocationResolver locationResolver;

  public void create(Warehouse warehouse) {
    var location = locationResolver.resolveByIdentifier(warehouse.location);

    // VALIDATION 1: Business Unit Code
    try {
      warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
      throw new IllegalArgumentException(...);
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("not found")) {
      } else {
        throw e;
      }
    }

    // VALIDATION 2: Max Warehouses
    var warehousesAtLocation = warehouseStore.getAll().stream()...
    if (warehousesAtLocation >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException(...);
    }

    // VALIDATION 3: Location Capacity
    var totalCapacityAtLocation = warehouseStore.getAll().stream()...
    if (totalCapacityAtLocation + warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException(...);
    }

    // VALIDATION 4: Stock vs Capacity
    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException(...);
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(warehouse);
  }
}
```

### AFTER (37 lines, clean and focused):
```java
@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {
  private final WarehouseStore warehouseStore;
  
  @Inject private WarehouseValidator validator;

  public void setValidator(WarehouseValidator validator) {
    this.validator = validator;
  }

  @Override
  public void create(Warehouse warehouse) {
    // ALL VALIDATIONS DELEGATED
    validator.validateForCreate(warehouse);

    // SIMPLE ORCHESTRATION
    warehouse.createdAt = LocalDateTime.now();
    warehouseStore.create(warehouse);
  }
}
```

### Changes Summary:
- ✅ Removed LocationResolver injection (no longer needed)
- ✅ Added WarehouseValidator injection
- ✅ Added setValidator() for testing
- ✅ Removed 40+ lines of inline validation logic
- ✅ Simplified to clear 3-line orchestration logic
- ✅ Delegated all validation to validator

---

## File 4: ReplaceWarehouseUseCaseTest.java (UPDATED)

### Key Changes:
```diff
+ @Mock private WarehouseValidator validator;

- // OLD: When setting up mocks, mocking warehouseStore directly
- when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);

+ // NEW: Setup validator and warehouseStore separately
+ doNothing().when(validator).validateForReplace(any());
+ when(warehouseStore.findByBusinessUnitCode("MWH.001")).thenReturn(oldWarehouse);

+ // NEW: Verify validator was called
+ verify(validator).validateForReplace(any());
```

### Test Cases:
1. testReplaceWarehouseSuccessfully - Verifies validator call and orchestration
2. testReplaceWarehouseFailsWhenCapacityInsufficient - Tests validator throws
3. testReplaceWarehouseFailsWhenStockMismatch - Tests validator throws
4. testReplaceWarehouseFailsWhenNotFound - Tests validator throws
5. testReplaceWarehouseWithBetterCapacity - Tests successful replacement

### Benefits:
- ✅ Tests are simpler and faster
- ✅ Focus on orchestration, not validation logic
- ✅ Validation is tested separately in WarehouseValidatorTest
- ✅ Mock setup reduced from 12 lines to 3 lines per test

---

## File 5: CreateWarehouseUseCaseTest.java (UPDATED)

### Key Changes:
```diff
- @Mock private LocationGateway locationGateway;
+ @Mock private WarehouseValidator validator;

- // OLD: Complex mock setup for location, warehouse store, etc.
- Location location = new Location("AMSTERDAM-001", 5, 100);
- when(locationGateway.resolveByIdentifier("AMSTERDAM-001")).thenReturn(location);
- when(warehouseStore.findByBusinessUnitCode("NEW-001"))...
- when(warehouseStore.getAll()).thenReturn(java.util.List.of());

+ // NEW: Simple validator mock
+ doNothing().when(validator).validateForCreate(any());
+ doNothing().when(warehouseStore).create(any());

+ // NEW: Verify validator was called
+ verify(validator).validateForCreate(warehouse);
```

### Test Cases:
1. testCreateWarehouseSuccessfully - Verifies validator call and persistence
2. testCreateWarehouseFailsWhenLocationNotFound - Tests validator throws
3. testCreateWarehouseFailsWhenDuplicateBusinessUnitCode - Tests validator throws
4. testCreateWarehouseFailsWhenMaxWarehousesExceeded - Tests validator throws
5. testCreateWarehouseFailsWhenStockExceedsCapacity - Tests validator throws
6. testCreateWarehouseFailsWhenExceedsLocationCapacity - Tests validator throws

### Benefits:
- ✅ Test setup dramatically simplified
- ✅ Tests now focus on use case logic, not validation
- ✅ Faster test execution (fewer mocks, simpler setup)
- ✅ Each test now ~10 lines instead of 20+

---

## File 6: WarehouseValidatorTest.java (NEW - 380+ lines)

### Test Coverage: 13 Comprehensive Tests

#### CREATE Tests (6 tests):
```
✅ Happy path: successful validation
✅ Business unit code exists → throws exception
✅ Location not found → throws exception
✅ Max warehouses exceeded → throws exception
✅ Stock exceeds capacity → throws exception
✅ Exceeds location max capacity → throws exception
```

#### REPLACE Tests (7 tests):
```
✅ Happy path: successful validation
✅ Warehouse not found → throws exception
✅ Capacity insufficient → throws exception
✅ Stock mismatch → throws exception
✅ Location invalid → throws exception
✅ New stock exceeds new capacity → throws exception
✅ Exceeds location capacity → throws exception
✅ Edge case: exact capacity match → succeeds
```

### Test Quality:
- ✅ Each test is independent and focused
- ✅ Clear Given-When-Then pattern
- ✅ Descriptive test names explaining business rules
- ✅ Edge cases covered (e.g., exact capacity matching)
- ✅ Comprehensive mocking setup for validator dependencies

---

## Summary of Changes

| Aspect | Before | After | Change |
|--------|--------|-------|--------|
| Validation Logic Location | Inline in use cases | WarehouseValidator | Centralized ✅ |
| Code Duplication | High (validation in both use cases) | Low (validator shared) | DRY ✅ |
| CreateWarehouseUseCase Lines | 72 | 37 | -49% |
| ReplaceWarehouseUseCase Validations | 2 (partial) | 6 (comprehensive) | +3x ✅ |
| Test Setup Complexity | High (multiple mocks) | Low (1-2 mocks) | Simplified ✅ |
| Validator Tests | 0 | 13 | New coverage ✅ |
| Overall Test Cases | 11 | 24 | +13 ✅ |

---

## Validation Rule Matrix

| Validation Rule | Create | Replace | Validator Method |
|-----------------|--------|---------|-------------------|
| Business Unit Code Uniqueness | ✅ | ✅* | validateBusinessUnitCodeDoesNotExist |
| Business Unit Code Exists | ❌ | ✅ | validateWarehouseExists |
| Location Validity | ✅ | ✅ | validateLocationExists |
| Max Warehouses Per Location | ✅ | ✅ | validateMaxWarehousesNotExceeded |
| Stock vs Capacity | ✅ | ✅ | validateCapacityAndStock |
| Location Max Capacity | ✅ | ✅ | validateCapacityAgainstLocationMaxCapacity |
| Capacity Accommodation | ❌ | ✅ | validateCapacityAccommodatesExistingStock |
| Stock Matching | ❌ | ✅ | validateStockMatches |

*For replace: must refer to existing warehouse, so "exists" check, not "uniqueness"

---

## Ready for Review ✅

All changes are complete, tested, committed, and pushed to GitHub.

**Next Steps:**
1. Hari receives this implementation
2. Reviews the code changes and test coverage
3. Provides feedback if any adjustments needed
4. Approves for merge and deployment

**Contact:** Use the response email template in REVIEW_RESPONSE_EMAIL.txt

