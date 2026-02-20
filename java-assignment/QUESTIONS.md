# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
Yes, I would refactor to achieve consistency. Currently we have:
- Store extends PanacheEntity (direct entity persistence)
- Product uses ProductRepository with PanacheRepository
- Warehouse uses custom WarehouseStore interface + PanacheRepository

The inconsistency creates cognitive overhead. I would standardize to use the Repository Pattern uniformly:

1. Create repositories for all entities (StoreRepository, ProductRepository follow this)
2. Implement custom interfaces for business rules (StoreStore, ProductStore interfaces)
3. This decouples entities from persistence logic, improves testability, and makes the codebase more maintainable

The reason: Repository Pattern provides a consistent abstraction layer. When we mix direct entity persistence (PanacheEntity) with repositories, we lose this consistency. If requirements change (e.g., migrate to a different ORM), having a uniform interface makes refactoring easier.

Additionally, custom interfaces (like WarehouseStore) make business requirements explicit and allow for multiple implementations (in-memory for testing, database for production).
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
OPENAPI-FIRST APPROACH (Warehouse):
Pros:
- Single source of truth: specification IS the documentation
- Contract-first design: API shape is explicit and agreed upon before implementation
- Automatic code generation reduces manual errors and boilerplate
- Easy to generate client SDKs, ensuring client-server consistency
- Versioning and changes are tracked in the spec file

Cons:
- Setup overhead: requires learning OpenAPI tooling
- Slower initial development: need to design spec first
- Generated code can be rigid if customization is needed

CODE-FIRST APPROACH (Product, Store):
Pros:
- Faster initial development: implement first, spec later
- More flexible: easier to iterate quickly during experimentation
- Less tooling complexity: no code generation to manage

Cons:
- Documentation can become out of sync with code
- No contract-first validation: implementation details leak into API design
- Client integration is harder without a spec
- Harder to track breaking changes

MY CHOICE: OpenAPI-first for this codebase

Why: The API is already stable and serves business requirements. Warehouse implementation shows this codebase values contracts and consistency. Moving Product and Store to OpenAPI-first would:
1. Ensure consistency across all endpoints
2. Make API documentation authoritative (not needing to reverse-engineer from code)
3. Reduce bugs from specification-implementation mismatches
4. Improve onboarding for new developers

The extra setup cost is justified for a production system with multiple consumers.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
TESTING PYRAMID APPROACH (Optimal for this codebase):

1. UNIT TESTS (70% - Foundation)
   Focus: Business logic in use cases
   Examples: CreateWarehouseUseCase validations, ArchiveWarehouseUseCase logic, AssociateWarehouseToProductInStoreUseCase constraints
   Why: Fast, isolated, catch most bugs early
   Tools: JUnit 5 locally, no database needed

2. INTEGRATION TESTS (20% - Critical paths)
   Focus: Repository operations + database interactions
   Examples: WarehouseRepository.create(), findByBusinessUnitCode(), update()
   Why: Verify ORM works correctly, catch transaction issues
   Tools: @QuarkusTest with test container for PostgreSQL

3. END-TO-END TESTS (10% - Key workflows)
   Focus: Full API requests through REST endpoints
   Examples: Create warehouse → Archive warehouse → Replace warehouse workflow
   Why: Verify complete user journeys, integration between layers
   Tools: @QuarkusIntegrationTest with RestAssured

PRIORITIZATION STRATEGY:

PHASE 1 (MVP): 
- Unit tests for all Warehouse use cases (70% of effort)
- Integration tests for critical repositories (20%)
- 1-2 E2E tests for happy paths (10%)

PHASE 2 (Production-ready):
- Add edge case unit tests (null validations, boundary conditions)
- Add failure scenario integration tests (duplicate codes, max capacity)
- Add comprehensive E2E tests covering error scenarios

ENSURING LONG-TERM COVERAGE:

1. Mutation Testing: Use PIT to verify test quality (not just coverage %)
2. Code Review: Require test coverage for all new code (min 80%)
3. CI/CD Gates: Fail builds if coverage drops
4. Documentation: Write test names that explain business rules
   Bad: testCreate()
   Good: testCreateWarehouseFailsWhenDuplicateBusinessUnitCode()
5. Regular Reviews: Quarterly cleanup of obsolete tests

RECOMMENDED TEST ORDER FOR THIS PROJECT:
1. CreateWarehouseUseCase tests (validates 5 constraints)
2. WarehouseRepository tests (CRUD operations)
3. FulfillmentResourceImpl tests (association constraints)
4. E2E workflow test (happy path: create → list → get)
```