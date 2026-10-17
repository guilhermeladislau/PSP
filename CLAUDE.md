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

[Preencher quando existirem: como subir o banco, rodar o backend, o frontend e os testes.]

## Avisos

O sistema não oferece diagnóstico, prescrição ou orientação médica ou nutricional. Estimativas e dados cadastrados pelo usuário devem ser distinguíveis de dados de fonte oficial.
