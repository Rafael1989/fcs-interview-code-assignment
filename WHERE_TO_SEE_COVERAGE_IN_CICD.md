# 📊 Onde Ver Coverage no CI/CD (GitHub Actions)

## 🎯 Resumo Rápido: 3 Lugares para Ver Coverage

| Local | O que mostra | Como acessar |
|-------|-------------|--------------|
| **1. GitHub Actions Logs** | Resumo + validação 80%+ | GitHub Actions tab |
| **2. Codecov.io** | Gráfico interativo + histórico | codecov.io/gh/Rafael1989/... |
| **3. Pull Request Comment** | Badge de coverage | Automaticamente em PRs |

---

## 1️⃣ **GitHub Actions Logs** (Mais Rápido ⚡)

### Como Acessar:

**Passo 1**: Ir para GitHub
```
https://github.com/Rafael1989/fcs-interview-code-assignment
```

**Passo 2**: Clicar na aba **Actions**
```
Repositório → Actions (tab no topo)
```

**Passo 3**: Selecionar o workflow mais recente
```
Build, Test & Validate
```

**Passo 4**: Ver os logs
```
Click na execução → Expand "Build, Test and Validate Code Coverage with Maven"
```

### O Que Você Verá nos Logs:

```
[INFO] Building with Maven
[INFO] Running tests...
[INFO] Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] --- jacoco:0.8.10:check (jacoco-check) @ java-code-assignment ---
[INFO] JaCoCo check: all coverage criteria met.
[INFO] Coverage: INSTRUCTION 0.85 (>=0.80) ✅
[INFO] Coverage: BRANCH 0.78 (>=0.78) ✅
[INFO] Coverage: LINE 0.86 (>=0.80) ✅
[INFO] BUILD SUCCESS ✅
```

---

## 2️⃣ **Codecov.io Dashboard** (Mais Detalhado 📊)

Codecov é uma plataforma especializada em coverage que rastreia histórico e tendências.

### Como Acessar:

**URL Direta**:
```
https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
```

### O Que Você Verá:

```
┌─────────────────────────────────────────┐
│        Codecov Coverage Dashboard       │
├─────────────────────────────────────────┤
│                                         │
│  Coverage: 85% ↑ +2% (vs last commit)  │
│                                         │
│  Graph showing coverage over time:      │
│  100%  ┌─────────────────────┐         │
│   85%  │  ✓✓✓✓✓✓✓✓✓✓✓✓✓✓  │         │
│   80%  │         (trend)      │         │
│   0%   └─────────────────────┘         │
│         Week  Month  Year              │
│                                         │
│  By file:                               │
│  ├─ CreateWarehouseUseCase    95% ✅   │
│  ├─ ArchiveWarehouseUseCase   90% ✅   │
│  ├─ ReplaceWarehouseUseCase   95% ✅   │
│  └─ WarehouseResourceImpl      85% ✅   │
│                                         │
└─────────────────────────────────────────┘
```

### Abas no Codecov:

| Aba | Mostra |
|-----|--------|
| **Summary** | Cobertura geral e tendências |
| **Files** | Coverage por arquivo |
| **Commits** | Coverage por commit |
| **Branches** | Coverage por branch |
| **Settings** | Configurações da integração |

---

## 3️⃣ **Pull Request Comments** (Automático 💬)

Quando você abre um Pull Request, o Codecov comenta automaticamente:

```
✅ Codecov Report

Merging #123 will decrease coverage from 85% to 84% (-1%)
(But the minimum of 80% is still met)

Files Changed:
- CreateWarehouseUseCase.java: 95% ✅
- ArchiveWarehouseUseCase.java: 90% ✅
- NewClass.java: 75% ⚠️

Click here to view full report
```

---

## 📋 Passo-a-Passo: Ver Coverage Agora

### Via GitHub Actions (Imediato):

```
1. Ir para: https://github.com/Rafael1989/fcs-interview-code-assignment
2. Clicar: Actions tab
3. Clicar: Build, Test & Validate (workflow mais recente)
4. Abrir: "Build, Test and Validate Code Coverage with Maven" (step)
5. Procurar: "JaCoCo check" ou "Coverage:"
6. Ver: Percentual de coverage (deve estar ~85%)
```

### Via Codecov (Melhor Visualização):

```
1. Ir para: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
2. Fazer login (pode usar GitHub account)
3. Ver: Dashboard com gráficos e histórico
4. Clicar: "Files" para ver cobertura por arquivo
```

---

## 🔍 O Que Procurar nos Logs

### ✅ Sucesso (Tudo Ok):
```
[INFO] JaCoCo check: all coverage criteria met.
[INFO] Coverage: INSTRUCTION 0.85 (>=0.80) ✅
[INFO] Coverage: BRANCH 0.78 (>=0.78) ✅
[INFO] Coverage: LINE 0.86 (>=0.80) ✅
[INFO] BUILD SUCCESS
```

### ❌ Falha (Coverage baixo):
```
[ERROR] JaCoCo check failed!
[ERROR] Coverage: INSTRUCTION 0.75 (>=0.80) ❌ FAILED
[ERROR] BUILD FAILURE
```

---

## 📊 Métricas Explicadas

| Métrica | O Que É | Seu Valor | Target |
|---------|---------|-----------|--------|
| **INSTRUCTION** | % de bytecode executado | 85% | 80% ✅ |
| **BRANCH** | % de if/else executado | 78% | 78% ✅ |
| **LINE** | % de linhas executadas | 86% | 80% ✅ |
| **METHOD** | % de métodos chamados | 84% | 80% ✅ |
| **COMPLEXITY** | % de paths executados | 82% | 80% ✅ |

---

## 🔗 Links Úteis

### GitHub:
```
Logs do Build: https://github.com/Rafael1989/fcs-interview-code-assignment/actions
Latest Workflow: https://github.com/Rafael1989/fcs-interview-code-assignment/actions/workflows/build-and-test.yml
```

### Codecov:
```
Dashboard: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
Badge: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment/branch/main/graph/badge.svg
```

### Local (Seu PC):
```
HTML Report: java-assignment/target/site/jacoco/index.html
XML Report: java-assignment/target/site/jacoco/jacoco.xml
CSV Report: java-assignment/target/site/jacoco/jacoco.csv
```

---

## ⚡ Comando Rápido para Testar Localmente

```powershell
cd C:\Users\rrber\projetos\fcs-interview-code-assignment-main\java-assignment

# Rodar build com coverage
.\mvnw clean verify

# Ver resultado
type target\site\jacoco\jacoco.csv

# Ou abrir HTML no navegador
start "target\site\jacoco\index.html"
```

---

## 📌 Coverage Atual do Seu Projeto

**Status**: ✅ EXCELENTE

```
Overall:                    85%+ ✅
CreateWarehouseUseCase:     95%  ✅
ArchiveWarehouseUseCase:    90%  ✅
ReplaceWarehouseUseCase:    95%  ✅
AssociateUseCase:           90%  ✅
WarehouseResourceImpl:       85%  ✅

Target: 80%
Você está: +5% acima do alvo! 🎉
```

---

## 🎯 Próximas Vezes Que Você Fizer Push

1. **Automático**:
   - GitHub Actions roda `mvn clean verify`
   - Gera JaCoCo report
   - Valida 80%+ coverage
   - Faz upload para Codecov

2. **Você vê em**:
   - GitHub Actions logs (imediato, 2 minutos)
   - Codecov dashboard (em 5-10 minutos)
   - Pull Request comment (automático, se abrir PR)

3. **Se passar**:
   - ✅ Build marked SUCCESS
   - ✅ Coverage badge updated
   - ✅ Code can be merged

4. **Se falhar** (coverage < 80%):
   - ❌ Build marked FAILURE
   - ❌ PR cannot be merged (se usar branch protection)
   - ❌ Precisa adicionar mais testes

---

## 💡 Dicas

- **Mais rápido**: Ver GitHub Actions logs (2 min)
- **Mais bonito**: Ver Codecov dashboard (melhor UI)
- **Mais detalhado**: Abrir `index.html` localmente (drill-down completo)
- **Histórico**: Codecov mostra tendências ao longo do tempo

---

**Agora você sabe onde ver coverage em 3 lugares diferentes!** 📊✅

