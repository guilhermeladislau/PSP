# [insira aqui o nome do projeto]

Plataforma integrada de gerenciamento pessoal, multiusuário. O primeiro módulo é **alimentação**: catálogo de alimentos, registro de refeições com cálculo automático de nutrientes e acompanhamento da meta diária de calorias.

> **Status:** em preparação do ambiente. Ainda não há funcionalidades implementadas.

## Estrutura do repositório

```
backend/    API e regras de negócio (Java, Spring Boot)
frontend/   Interface web (React, TypeScript, Vite)
tools/      Ferramentas auxiliares (Python), como importação de tabelas nutricionais
docs/       Documentação e registros de decisões de arquitetura (docs/adr)
```

## Stack

- Backend: Java (LTS) com Spring Boot, monólito modular
- Banco de dados: PostgreSQL, com migrações versionadas
- Frontend: React, TypeScript e Vite
- Ferramentas: Python
- Ambiente local: Docker Compose

As decisões e justificativas estão em [`docs/adr/`](docs/adr/).

## Como rodar

Ainda não disponível. Esta seção será preenchida no primeiro incremento.

## Segurança

Nunca versione segredos (`.env`, chaves, credenciais). Use o arquivo `.env.example` como modelo de configuração.

## Aviso

Este projeto não oferece diagnóstico, prescrição ou orientação médica ou nutricional.
