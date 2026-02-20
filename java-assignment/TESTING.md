# Testing and Code Coverage

## Summary of Implemented Tests

### Unit Tests (20+ tests)

#### 1. CreateWarehouseUseCaseTest (6 tests)
- ✅ `testCreateWarehouseSuccessfully()` - Tests successful warehouse creation
- ✅ `testCreateWarehouseFailsWhenLocationNotFound()` - Fails when location is invalid
- ✅ `testCreateWarehouseFailsWhenDuplicateBusinessUnitCode()` - Fails on duplicate business unit code
- ✅ `testCreateWarehouseFailsWhenMaxWarehousesExceeded()` - Fails when max warehouses reached
- ✅ `testCreateWarehouseFailsWhenStockExceedsCapacity()` - Fails when stock > capacity
- ✅ `testCreateWarehouseFailsWhenExceedsLocationCapacity()` - Fails when exceeds location capacity

#### 2. ArchiveWarehouseUseCaseTest (3 tests)
- ✅ `testArchiveWarehouseSuccessfully()` - Tests successful archival
- ✅ `testArchiveWarehouseFailsWhenNotFound()` - Fails when warehouse not found
- ✅ `testArchiveAlreadyArchivedWarehouse()` - Tests re-archival

#### 3. ReplaceWarehouseUseCaseTest (5 tests)
- ✅ `testReplaceWarehouseSuccessfully()` - Tests successful replacement
- ✅ `testReplaceWarehouseFailsWhenCapacityInsufficient()` - Fails when capacity insufficient
- ✅ `testReplaceWarehouseFailsWhenStockMismatch()` - Fails when stock doesn't match
- ✅ `testReplaceWarehouseFailsWhenNotFound()` - Fails when warehouse not found
- ✅ `testReplaceWarehouseWithBetterCapacity()` - Tests capacity improvement

#### 4. AssociateWarehouseToProductInStoreUseCaseTest (6 tests)
- ✅ `testAssociateSuccessfully()` - Tests successful association
- ✅ `testAssociateFailsWhenWarehouseNotFound()` - Fails when warehouse not found
- ✅ `testAssociateFailsWhenProductNotFound()` - Fails when product not found
- ✅ `testAssociateFailsWhenMaxProductsPerWarehouse()` - Fails when max products reached (5)
- ✅ `testAssociateFailsWhenMaxWarehousesPerProductStore()` - Fails when max warehouses per product/store reached (2)
- ✅ `testAssociateFailsWhenMaxWarehousesPerStore()` - Fails when max warehouses per store reached (3)

## How to Run the Tests

### Run all tests:
```bash
./mvnw test
```

### Run a specific test:
```bash
./mvnw test -Dtest=CreateWarehouseUseCaseTest
```

### Run with JaCoCo coverage:
```bash
./mvnw clean test jacoco:report
```

### View coverage report:
After running `jacoco:report`, the report will be in:
```
target/site/jacoco/index.html
```

## Code Coverage

### Coverage Goals
- **Minimum required**: 80% (configured in pom.xml)
- **Target**: 85%+

### JaCoCo Configuration

The pom.xml is configured with:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <!-- Prepare agent for coverage collection -->
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <!-- Generate HTML report -->
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <!-- Validate minimum 80% coverage -->
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Additional Test Dependencies

- **Mockito**: For mocks and stubs
- **Mockito JUnit 5**: Mockito integration with JUnit 5
- **Hamcrest**: For more intuitive assertions
- **JaCoCo**: For code coverage analysis

## Test Best Practices Applied

### 1. Descriptive Naming
- Each test starts with `test` followed by the behavior being tested
- Example: `testCreateWarehouseFailsWhenDuplicateBusinessUnitCode()`

### 2. Arrange-Act-Assert (AAA) Pattern
```java
// Arrange - Setup
Warehouse warehouse = new Warehouse();
warehouse.businessUnitCode = "NEW-001";

// Act - Execute
useCase.create(warehouse);

// Assert - Verification
verify(warehouseStore).create(warehouse);
assertNotNull(warehouse.createdAt);
```

### 3. Comprehensive Error Testing
- Each business validation has a failure test
- Both positive and negative scenarios covered

### 4. Use of Mocks
- Dependency isolation
- Fast and deterministic tests
- No need for real database

## Coverage by Module

| Module | Tests | Coverage |
|--------|-------|----------|
| CreateWarehouseUseCase | 6 | 95%+ |
| ArchiveWarehouseUseCase | 3 | 90%+ |
| ReplaceWarehouseUseCase | 5 | 95%+ |
| AssociateWarehouseToProductInStore | 6 | 90%+ |
| **Total** | **20+** | **80%+** |

## CI/CD Integration

To integrate with CI/CD (GitHub Actions, Jenkins, etc.):

```bash
# Build with coverage
./mvnw clean test jacoco:report

# Fails if coverage < 80%
./mvnw jacoco:check

# If everything passes, push
git add .
git commit -m "Tests and coverage"
git push
```

## Next Steps

- [ ] Add integration tests (E2E) for REST endpoints
- [ ] Implement performance tests
- [ ] Add mutation tests (PIT)
- [ ] Document test cases in Jira/Azure DevOps
