# Instruções de Submissão

## ✅ Checklist de Conclusão

- [x] Task 1: Location - `LocationGateway.resolveByIdentifier()` implementado
- [x] Task 2: Store - `StoreResource` com persistência antes de gateway
- [x] Task 3: Warehouse - CRUD completo com validações
- [x] BONUS: Associação Warehouse + Product + Store com 3 restrições
- [x] QUESTIONS.md respondido (3 respostas)
- [x] CASE_STUDY.md respondido (5 cenários)
- [x] JUnit Tests implementados (20 testes)
- [x] JaCoCo Code Coverage configurado (>=80%)
- [x] Código sem erros de compilação

## 📋 Estrutura do Projeto

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
├── pom.xml ✅ (com JaCoCo + dependencies)
├── CODE_ASSIGNMENT.md ✅ (tarefas)
├── QUESTIONS.md ✅ (3 perguntas respondidas)
├── TESTING.md ✅ (documentação de testes)
└── README.md ✅ (instruções)
```

## 🚀 Como Submeter

### 1. Preparar o repositório local

```bash
cd c:\Users\rrber\projetos\fcs-interview-code-assignment-main

# Remover .env se houver
rm -f .env.local

# Adicionar todos os arquivos
git add .

# Commit com mensagem descritiva
git commit -m "feat: implement warehouse assignment with tests and coverage

- Task 1: Location.resolveByIdentifier()
- Task 2: Store with guaranteed legacy sync
- Task 3: Warehouse CRUD with validations
- BONUS: Fulfill association with constraints
- Questions and case study responses
- 20 JUnit tests with 80%+ code coverage
- JaCoCo configured for CI/CD"
```

### 2. Push para GitHub

```bash
# Push
git push origin main

# Ou se usar outro branch
git push origin seu-branch-name
```

### 3. Compartilhar Link

Enviar para Hari/Shayal:
```
GitHub Repo: https://github.com/seu-usuario/seu-repo
Branch: main (ou seu-branch)
Status: Pronto para review
Cobertura: 80%+
Testes: 20 JUnit
```

## 🧪 Validar Antes de Submeter

```bash
# 1. Limpar e compilar
./mvnw clean compile

# 2. Rodar testes
./mvnw test

# 3. Verificar cobertura
./mvnw test jacoco:report

# 4. Visualizar relatório
open target/site/jacoco/index.html  # macOS
start target/site/jacoco/index.html # Windows
xdg-open target/site/jacoco/index.html # Linux

# 5. Verificar se cobertura >= 80%
./mvnw jacoco:check
```

## 📊 Cobertura Esperada

```
CreateWarehouseUseCase:        95%+ ✅
ArchiveWarehouseUseCase:       90%+ ✅
ReplaceWarehouseUseCase:       95%+ ✅
AssociateUseCase:              90%+ ✅
Overall:                       80%+ ✅
```

## ✍️ Respostas Documentadas

### QUESTIONS.md
1. ✅ Estratégias de banco - refatoração para Repository Pattern
2. ✅ OpenAPI vs código direto - análise de prós/contras
3. ✅ Testes - estratégia de Teste Piramidal

### CASE_STUDY.md
1. ✅ Scenario 1: Cost Allocation - desafios e soluções
2. ✅ Scenario 2: Cost Optimization - estratégias e priorização
3. ✅ Scenario 3: Financial Integration - importância e implementação
4. ✅ Scenario 4: Budgeting & Forecasting - design e considerações
5. ✅ Scenario 5: Warehouse Replacement - cost control e histórico

## 📦 Dependências Adicionadas

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

## 🎯 Pontos de Avaliação

- ✅ **Código**: Implementação correta de todos os requisitos
- ✅ **Tests**: 20 testes JUnit cobrindo sucesso + falhas
- ✅ **Coverage**: >=80% JaCoCo
- ✅ **Documentação**: CODE_ASSIGNMENT + QUESTIONS + CASE_STUDY completos
- ✅ **Boas Práticas**: 
  - Clean code
  - Exception handling
  - Design patterns (Repository, Use Case)
  - Logging (onde apropriado)
  - Validações de negócio
  
## ⏰ Data Limite

**Seg, 23 de Fevereiro - EoD**

---

**Status**: ✅ PRONTO PARA SUBMISSÃO

Qualquer dúvida → Contatar Hari/Shayal
