# Codecov Coverage Setup Guide

## ✅ Status Current

O workflow do GitHub Actions foi configurado para automaticamente:
1. ✅ Build do projeto com Maven
2. ✅ Executar todos os testes
3. ✅ Gerar relatório JaCoCo em formato XML
4. ✅ Fazer upload automático para Codecov

## 🔍 Como Verificar o Status

### 1. **Verificar os Logs do GitHub Actions**

1. Vá para: https://github.com/Rafael1989/fcs-interview-code-assignment/actions
2. Clique no workflow mais recente "Build and Test with Coverage"
3. Verifique se passou em todos os steps:
   - ✓ Checkout
   - ✓ Set up JDK 21
   - ✓ Build and Test with Maven
   - ✓ Generate JaCoCo Report
   - ✓ List coverage files
   - ✓ Check JaCoCo XML exists
   - ✓ Upload coverage to Codecov
   - ✓ Upload Test Results
   - ✓ Upload Coverage Reports

### 2. **Verificar no Codecov**

1. Vá para: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment
2. Procure pela aba **Commits** 
3. Cada commit deve aparecer com:
   - Data/hora
   - Porcentagem de cobertura
   - Status (✓ ou ×)

### 3. **Se a Cobertura Não Aparecer**

Execute estes passos:

#### a) **Adicionar Token do Codecov (Recomendado)**

1. Vá para: https://codecov.io/gh/Rafael1989/fcs-interview-code-assignment/settings
2. Copie o token fornecido
3. No GitHub, vá para: https://github.com/Rafael1989/fcs-interview-code-assignment/settings/secrets/actions
4. Clique em "New repository secret"
5. Nome: `CODECOV_TOKEN`
6. Valor: (cole o token do Codecov)
7. Clique em "Add secret"

#### b) **Verificar os Logs de Upload**

No GitHub Actions, procure no step "Upload coverage to Codecov" por mensagens como:

```
Reports have been queued for processing
Reports have been processed successfully
Upload successful
```

#### c) **Verificar se o JaCoCo XML está sendo gerado**

No step "Check JaCoCo XML exists", procure por:

```
✓ jacoco.xml found
File size: XXXXX bytes
```

Se não encontrar, significa que o build não está gerando o relatório.

## 📋 Verificação Rápida

```bash
# 1. Build local para gerar JaCoCo
cd java-assignment
mvn clean verify

# 2. Gerar relatório
mvn jacoco:report

# 3. Verificar se foi gerado
ls -la target/site/jacoco/jacoco.xml

# 4. Ver o tamanho
wc -c target/site/jacoco/jacoco.xml
```

## 🔧 Troubleshooting

### Problema: "jacoco.xml not found"

**Solução:**
```bash
# 1. Limpar e rebuildar
mvn clean verify

# 2. Gerar relatório explicitamente
mvn jacoco:report

# 3. Verificar se existe
find target -name "jacoco.xml"
```

### Problema: "Upload failed"

**Checklist:**
- [ ] Token do Codecov foi adicionado ao GitHub Secrets
- [ ] O arquivo `jacoco.xml` foi gerado localmente
- [ ] A conta do Codecov existe
- [ ] O repositório foi conectado ao Codecov

### Problema: "Coverage not updating"

**Causas possíveis:**
1. Token não configurado
2. Arquivo não está sendo gerado
3. Caminho do arquivo está incorreto
4. Codecov está processando (leva alguns minutos)

**Solução:**
1. Aguarde 5-10 minutos
2. Faça um novo push para trigger o workflow novamente
3. Verifique os logs detalhadamente

## 📊 Métricas Esperadas

Com base no projeto atual:

- **Instruction Coverage:** 70%+ (configurado no pom.xml)
- **Branch Coverage:** Variável
- **Line Coverage:** Variável
- **Método Coverage:** 70%+

## 🚀 Próximas Ações

1. ✅ Aguarde o workflow completar (2-5 minutos)
2. ✅ Verifique os logs do GitHub Actions
3. ✅ Acesse o Codecov para ver a cobertura
4. ✅ Se não aparecer, adicione o token e trigger novamente

## 📚 Recursos Úteis

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Codecov Documentation](https://docs.codecov.io/)
- [JaCoCo Maven Plugin](https://www.eclemma.org/jacoco/trunk/doc/maven.html)
- [Codecov GitHub Action](https://github.com/codecov/codecov-action)

---

**Data de Atualização:** 22 de Fevereiro de 2026

**Status:** ✅ Configurado e pronto para uso

