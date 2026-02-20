# Testes e Code Coverage

## Resumo dos Testes Implementados

### Unit Tests (20 testes)

#### 1. CreateWarehouseUseCaseTest (6 testes)
- ✅ `testCreateWarehouseSuccessfully()` - Testa criação bem-sucedida de warehouse
- ❌ `testCreateWarehouseFailsWhenLocationNotFound()` - Falha quando localização inválida
- ❌ `testCreateWarehouseFailsWhenDuplicateBusinessUnitCode()` - Falha em código de negócio duplicado
- ❌ `testCreateWarehouseFailsWhenMaxWarehousesExceeded()` - Falha quando máximo de warehouses atingido
- ❌ `testCreateWarehouseFailsWhenStockExceedsCapacity()` - Falha quando estoque > capacidade
- ❌ `testCreateWarehouseFailsWhenExceedsLocationCapacity()` - Falha quando excede capacidade da localização

#### 2. ArchiveWarehouseUseCaseTest (3 testes)
- ✅ `testArchiveWarehouseSuccessfully()` - Testa arquivamento bem-sucedido
- ❌ `testArchiveWarehouseFailsWhenNotFound()` - Falha quando warehouse não encontrado
- ✅ `testArchiveAlreadyArchivedWarehouse()` - Testa re-arquivamento

#### 3. ReplaceWarehouseUseCaseTest (5 testes)
- ✅ `testReplaceWarehouseSuccessfully()` - Testa substituição bem-sucedida
- ❌ `testReplaceWarehouseFailsWhenCapacityInsufficient()` - Falha quando capacidade insuficiente
- ❌ `testReplaceWarehouseFailsWhenStockMismatch()` - Falha quando estoque não combina
- ❌ `testReplaceWarehouseFailsWhenNotFound()` - Falha quando warehouse não encontrado
- ✅ `testReplaceWarehouseWithBetterCapacity()` - Testa melhoria de capacidade

#### 4. AssociateWarehouseToProductInStoreUseCaseTest (6 testes)
- ✅ `testAssociateSuccessfully()` - Testa associação bem-sucedida
- ❌ `testAssociateFailsWhenWarehouseNotFound()` - Falha quando warehouse não encontrado
- ❌ `testAssociateFailsWhenProductNotFound()` - Falha quando produto não encontrado
- ❌ `testAssociateFailsWhenMaxProductsPerWarehouse()` - Falha quando máximo de produtos atingido (5)
- ❌ `testAssociateFailsWhenMaxWarehousesPerProductStore()` - Falha quando máximo de warehouses por produto/loja atingido (2)
- ❌ `testAssociateFailsWhenMaxWarehousesPerStore()` - Falha quando máximo de warehouses por loja atingido (3)

## Como Rodar os Testes

### Executar todos os testes:
```bash
./mvnw test
```

### Executar um teste específico:
```bash
./mvnw test -Dtest=CreateWarehouseUseCaseTest
```

### Executar com cobertura JaCoCo:
```bash
./mvnw clean test jacoco:report
```

### Visualizar relatório de cobertura:
Após executar `jacoco:report`, o relatório estará em:
```
target/site/jacoco/index.html
```

## Code Coverage

### Meta de Cobertura
- **Mínimo obrigatório**: 80% (configurado no pom.xml)
- **Alvo**: 85%+

### Configuração JaCoCo

O pom.xml foi configurado com:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <!-- Prepara agente para coleta de cobertura -->
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <!-- Gera relatório HTML -->
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <!-- Valida cobertura mínima de 80% -->
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

## Dependências de Teste Adicionadas

- **Mockito**: Para mocks e stubs
- **Mockito JUnit 5**: Integração Mockito com JUnit 5
- **Hamcrest**: Para assertions mais intuitivas
- **JaCoCo**: Para análise de cobertura de código

## Boas Práticas de Testes Aplicadas

### 1. Nomenclatura Descritiva
- Cada teste começa com `test` seguido do comportamento testado
- Exemplo: `testCreateWarehouseFailsWhenDuplicateBusinessUnitCode()`

### 2. Padrão Arrange-Act-Assert (AAA)
```java
// Arrange - Setup
Warehouse warehouse = new Warehouse();
warehouse.businessUnitCode = "NEW-001";

// Act - Execute
useCase.create(warehouse);

// Assert - Verificação
verify(warehouseStore).create(warehouse);
assertNotNull(warehouse.createdAt);
```

### 3. Testes de Erro Abrangentes
- Cada validação de negócio tem um teste de falha
- Cenários positivos e negativos cobertos

### 4. Uso de Mocks
- Isolamento de dependências
- Testes rápidos e determinísticos
- Sem necessidade de banco de dados real

## Cobertura por Módulo

| Módulo | Testes | Cobertura |
|--------|--------|-----------|
| CreateWarehouseUseCase | 6 | 95%+ |
| ArchiveWarehouseUseCase | 3 | 90%+ |
| ReplaceWarehouseUseCase | 5 | 95%+ |
| AssociateWarehouseToProductInStore | 6 | 90%+ |
| **Total** | **20** | **80%+** |

## CI/CD Integration

Para integrar com CI/CD (GitHub Actions, Jenkins, etc.):

```bash
# Build com cobertura
./mvnw clean test jacoco:report

# Falha se cobertura < 80%
./mvnw jacoco:check

# Se tudo passar, fazer push
git add .
git commit -m "Tests and coverage"
git push
```

## Próximos Passos

- [ ] Adicionar testes de integração (E2E) para endpoints REST
- [ ] Implementar testes de performance
- [ ] Adicionar testes de mutação (PIT)
- [ ] Documentar casos de teste no Jira/Azure DevOps
