# CLAUDE.md: instruções do projeto

## Sobre o projeto

Plataforma integrada de gerenciamento pessoal, multiusuário. Primeiro módulo: alimentação. O assessor de IA é um projeto separado, ainda fora do escopo.

Estrutura: `backend/` (Java, Spring Boot), `frontend/` (React, TypeScript, Vite), `tools/` (Python), `docs/` (documentação e ADRs em `docs/adr/`).

Decisões de stack e padrão de qualidade: ver `docs/adr/001-stack.md`. Não proponha trocar tecnologias sem um motivo concreto.

## Como trabalhar comigo

- Faça mudanças pequenas, uma por vez. Não implemente várias funcionalidades de uma só vez.
- Antes de alterar arquivos, explique o que pretende fazer e por quê. Depois, resuma o que mudou e como validar.
- Explique conceitos novos de forma breve. Nenhuma mudança está concluída até eu conseguir explicá-la.
- Se algo for ambíguo, pergunte antes de assumir.
- Quando eu pedir, deixe partes de propósito para eu escrever à mão (modelagem, regras de negócio, testes, autenticação).
- Não invente APIs, opções de ferramentas ou versões. Se não tiver certeza, diga e consulte a documentação oficial.
- Nunca afirme que testes passaram ou que algo funciona sem tê-lo executado. Diferencie resultado observado, hipótese e recomendação.

## Segurança (regras inegociáveis)

- Nunca commitar segredos (senhas, tokens, chaves, `.env`). Use `.env.example` com valores fictícios.
- Não leia, imprima nem exponha o conteúdo de arquivos `.env` ou de credenciais.
- Nunca registrar em logs senhas, tokens ou dados pessoais de saúde.
- Toda consulta a dados de usuário deve ser restrita ao dono do dado. Isso é verificado no backend, nunca só no frontend.
- Validar toda entrada no backend. Usar consultas parametrizadas, nunca concatenar SQL.
- Não adicionar dependência nova sem justificar necessidade, manutenção e riscos.
- Pedir confirmação antes de qualquer comando destrutivo (apagar dados, `rm -r`, `git reset --hard`, `git push --force`, derrubar volumes do Docker).

## Banco de dados e dados

- Toda alteração de esquema via migração versionada. Nunca alterar o banco manualmente.
- Valores nutricionais são guardados por 100 g; porções e unidades são convertidas por equivalência em gramas.
- Registros de refeição guardam uma cópia dos valores nutricionais da época do registro. Corrigir um alimento não altera o histórico.
- Use tipos numéricos exatos para quantidades e nutrientes (não ponto flutuante). Defina e documente a regra de arredondamento.
- Refeições com histórico não são apagadas, e sim arquivadas. O limite de 10 refeições ativas por usuário é garantido no backend.
- Alimentos cadastrados manualmente são privados do usuário por padrão. Registrar a origem do dado: fonte oficial, cadastro do usuário ou editado.

## Qualidade

- Toda mudança de comportamento vem com teste. Cubra casos comuns, de borda, entradas inválidas e cenários de segurança.
- Testes de integração usam PostgreSQL real via Testcontainers, não banco em memória.
- Formatação e análise estática devem passar antes de considerar a tarefa concluída.
- Commits no padrão Conventional Commits (`feat:`, `fix:`, `chore:`, `docs:`, `test:`). Trabalhar em branches curtas e nunca commitar direto na `main`.

## Escopo atual (MVP)

1. Conta com e-mail e senha; cada pessoa vê só seus dados.
2. Catálogo de alimentos com busca, porção padrão e macros (carga inicial pela TACO, após verificar a licença).
3. Registro de alimento em uma refeição, com quantidade em g, kg ou unidade e cálculo automático.
4. Cadastro manual de alimento (privado do usuário).
5. Meta diária de calorias.
6. Tela do dia: consumido vs. restante em velocímetro, mais total de macros.
7. Editar e remover registros do dia.
8. Refeições personalizáveis: 4 sugeridas (café, almoço, jantar, lanches), máximo de 10 ativas.

Fora do MVP: marcas (Open Food Facts), metas de macros, histórico semanal, favoritos, receitas, IA e outros módulos. Não implemente nada fora do escopo sem eu pedir.

## Comandos úteis

### Pré-requisitos do ambiente

- JDK instalado: 27 (o `pom.xml` usa `java.version` 25; compila normalmente).
- Docker ativo e usuário no grupo `docker`. Se aparecer `permission denied ... /var/run/docker.sock`, a sessão foi aberta antes de entrar no grupo: faça logout/login (ou `newgrp docker` no terminal atual). Conferir com `id` (deve listar `docker`) e `docker info`.

### Banco local (raiz do repositório)

```bash
cp .env.example .env   # uma vez; troque DB_PASSWORD (o .env nunca é commitado)
docker compose up -d --wait   # PostgreSQL 18.6, só em 127.0.0.1:5432
docker compose down           # para o banco e mantém os dados (volume postgres-data)
```

A versão da imagem (`postgres:18.6`) aparece em `compose.yaml` e em `TestcontainersConfiguration.java`. Atualize as duas juntas.

### Backend (`backend/`)

Rodar sempre de dentro de `backend/`, usando o wrapper (`./mvnw`), nunca um Maven global.

```bash
cd backend
./mvnw test              # testes; sobe PostgreSQL real via Testcontainers (exige Docker, não usa o .env)
./mvnw verify            # build completo com testes
./mvnw spring-boot:run   # roda local; exige o banco do Compose no ar; lê ../.env
curl localhost:8080/actuator/health   # deve responder {"status":"UP",...}
```

Validado em 2026-10-17: `./mvnw test` passa sem `.env`, e `spring-boot:run` conecta no banco do Compose com health `UP`.

### Frontend (`frontend/`)

Rodar sempre de dentro de `frontend/`.

```bash
cd frontend
npm install       # instala dependências
npm run dev       # roda servidor de desenvolvimento em http://localhost:5173 (com proxy para http://localhost:8080/api)
npm run lint      # análise estática com Oxlint
npm run build     # compilação TypeScript (tsc -b) e build otimizado com Vite
```

### Ainda não existem

- **Ferramentas Python:** ainda não criadas.
- **Formatação e análise estática do backend:** ainda não configuradas no build.
- **CI:** ainda não configurada.

### Avisos conhecidos (não bloqueiam)

- Mockito/ByteBuddy avisam sobre carregamento dinâmico de agent no JDK atual; resolver configurando o Mockito como agent no Surefire.
- `spring.jpa.open-in-view` ligado por padrão; desligar quando a camada web começar.

## Avisos

O sistema não oferece diagnóstico, prescrição ou orientação médica ou nutricional. Estimativas e dados cadastrados pelo usuário devem ser distinguíveis de dados de fonte oficial.
