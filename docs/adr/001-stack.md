# ADR 001: Stack e padrão de qualidade

- **Status:** Aceita
- **Data:** 2026-10-16

## Contexto

Estamos construindo uma plataforma integrada de gerenciamento pessoal. O primeiro módulo é alimentação (catálogo, registro de refeições, metas). O assessor de IA será um projeto separado, integrado depois via API.

Restrições e premissas:

- Multiusuário desde o início, com possibilidade futura de virar produto.
- Os dados incluem informações de saúde e corpo, que exigem cuidado extra. Questões legais (por exemplo, LGPD) precisam de avaliação jurídica antes de abrir para outras pessoas; esta ADR não faz essa avaliação.
- Projeto solo, com apoio de um agente de código (Claude Code) e revisão humana.
- Objetivo de aprendizado em full stack, DevOps e segurança de aplicações, e desejo de um projeto multilinguagem.
- Desenvolvimento em Arch Linux. Hospedagem em servidor próprio no futuro, ainda inexistente.
- Valores nutricionais virão de tabelas públicas (como a TACO), com cadastro manual quando o alimento não existir. As licenças das fontes ainda precisam ser verificadas.

## Opções consideradas

**Backend**

- Java com Spring Boot: tipagem forte, ecossistema maduro para segurança e persistência, boa prática de monólito modular. Mais configuração inicial.
- Node com TypeScript: menos atrito inicial e mesma linguagem do frontend. Menos alinhado ao objetivo de multilinguagem.
- Python com FastAPI: produtivo e simples. Reservado para ferramentas auxiliares e, depois, para o agente de IA.

**Banco de dados**

- PostgreSQL: relacional, forte em integridade e precisão numérica, bem suportado para produção.
- SQLite: simples, mas inadequado para multiusuário em servidor.

**Frontend**

- React com TypeScript e Vite: já conhecido por mim e amplamente adotado.

**Organização do repositório**

- Monorepo (`backend/`, `frontend/`, `tools/`, `docs/`): mais simples para um projeto solo e para o agente enxergar o contexto completo.
- Múltiplos repositórios: mais isolamento, mas custo de coordenação sem benefício hoje.

## Decisão

- Backend em **Java (versão LTS mais recente compatível com o Spring Boot escolhido) com Spring Boot**, como monólito modular (pacotes por área: contas, catálogo, diário).
- **PostgreSQL** com migrações versionadas desde a primeira tabela.
- Frontend em **React, TypeScript e Vite**.
- **Python** para ferramentas auxiliares, começando pela importação e limpeza das tabelas nutricionais.
- **Monorepo** com Git.
- **Docker Compose** para o ambiente local (inicialmente só o PostgreSQL).
- Node não terá papel específico por enquanto; entra se surgir uma necessidade real.
- Kubernetes, microsserviços, filas e alta disponibilidade ficam fora até haver um problema concreto que os justifique.

## Consequências

**Ganhos**

- Base sólida para multiusuário, integridade de dados e segurança.
- Cada linguagem tem um papel definido.
- Oportunidade de aprender práticas profissionais desde o início.

**Custos e riscos**

- Projeto poliglota: mais ferramentas, mais dependências (maior superfície de ataque) e mais configuração de CI e deploy.
- Spring Boot esconde muita configuração automática; é preciso entender o que o agente gera antes de aceitar.
- Pertencer ao grupo `docker` equivale, na prática, a ter acesso root no PC de desenvolvimento. Aceito neste ambiente pessoal; alternativas são Docker rootless ou Podman.
- Licenças das fontes de dados nutricionais pendentes de verificação antes de importar qualquer base.
- Uso de agente de código exige disciplina: nenhuma mudança é concluída sem que eu consiga explicar o que ela faz e como foi validada.

## Padrão de qualidade

Itens que adoto desde o primeiro commit:

- Git disciplinado: `main` protegida, branches curtas, pull requests e mensagens de commit padronizadas.
- Integração contínua: build, testes, formatação e análise estática a cada PR.
- Segurança no pipeline: varredura de segredos, análise de dependências e SAST; varredura de imagens quando houver containers.
- Atualização automática de dependências.
- Testes de integração com PostgreSQL real (Testcontainers), sem banco em memória.
- Migrações de banco versionadas.
- Configuração por ambiente, sem segredos no repositório, e ambiente local reproduzível por um comando.
- ADRs e modelagem de ameaças antes das partes sensíveis (autenticação e dados do usuário).
- Backup com teste de restauração antes de qualquer dado real entrar em produção.

**Adiados, com gatilho de entrada:** Kubernetes (mais de um servidor ou necessidade concreta), microsserviços e filas (módulo que precise escalar ou falhar de forma independente), observabilidade completa (após a implantação), alta disponibilidade (usuários que dependam de uptime).
