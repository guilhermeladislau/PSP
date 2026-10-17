# Frontend — Plataforma Integrada de Gerenciamento Pessoal

Interface web do monorepo, construída com **React 19**, **TypeScript** e **Vite**.

## Executando localmente

Certifique-se de ter o **Node.js** (>= 20) instalado.

```bash
cd frontend
npm install       # instala dependências
npm run dev       # inicia servidor de desenvolvimento em http://localhost:5173
```

### Integração com o Backend

Em desenvolvimento, o Vite está configurado (`vite.config.ts`) para repassar chamadas iniciadas em `/api/*` diretamente para o backend na porta local `8080` (`http://localhost:8080`).

Para testar a busca de alimentos:
1. Inicie o PostgreSQL: `docker compose up -d --wait` (na raiz do repositório)
2. Inicie o backend: `cd backend && ./mvnw spring-boot:run`
3. Inicie o frontend: `cd frontend && npm run dev`
4. Acesse http://localhost:5173

## Scripts disponíveis

- `npm run dev`: Inicia o servidor Vite em modo desenvolvimento com Hot Module Replacement (HMR).
- `npm run build`: Compila TypeScript (`tsc -b`) e gera o pacote estático otimizado em `dist/`.
- `npm run lint`: Executa a análise estática com Oxlint.
- `npm run preview`: Serve a pasta `dist/` localmente para validação pré-deploy.
