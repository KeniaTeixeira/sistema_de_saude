# Sistema de Saúde - ES2

Sistema web para gerenciamento de profissionais de saúde, atendimentos e exames laboratoriais, desenvolvido para a disciplina de Engenharia de Software II (ES2).

## Tecnologias

| Camada          | Tecnologia                  |
| --------------- | --------------------------- |
| Backend         | Java 17 + Spring Boot 3     |
| Frontend        | React 18 + React Router DOM |
| Banco de Dados  | PostgreSQL (Neon Database)  |
| Build Backend   | Maven                       |
| Build Frontend  | Node.js 20 + npm            |
| Versionamento   | Git + GitHub                |
| CI/CD           | GitHub Actions              |
| Containers      | Docker + Docker Compose     |
| Produção        | Render + Neon PostgreSQL    |

## Estrutura do Projeto

```text
sistema_de_saude/
├── backend/                  # API REST (Java/Spring Boot)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
├── frontend/                 # Interface Web (React)
│   ├── package.json
│   ├── Dockerfile
│   └── src/
├── docker-compose.yml
├── .github/workflows/
│   └── ci-cd.yml
└── README.md
```

## Funcionalidades

### Profissionais de Saúde

* Cadastrar profissional
* Consultar profissional
* Atualizar profissional
* Excluir profissional

### Atendimentos

* Cadastrar atendimento
* Consultar atendimento
* Atualizar atendimento
* Excluir atendimento

### Exames Laboratoriais

* Cadastrar exame
* Consultar exame
* Atualizar exame
* Excluir exame

## Modelo de Relacionamento

```text
Profissional de Saúde (1) → (N) Atendimento

Atendimento (1) → (N) Exame Laboratorial
```

## Como Executar (Desenvolvimento)

### Utilizando Docker Compose

```bash
docker compose up --build
```

Serviços disponíveis:

* Frontend: http://localhost:3000
* Backend: http://localhost:8080
* PostgreSQL: localhost:5432

### Executando Manualmente

#### Backend

```bash
cd backend
mvn spring-boot:run
```

Disponível em:

```text
http://localhost:8080
```

#### Frontend

```bash
cd frontend
npm install
npm start
```

Disponível em:

```text
http://localhost:3000
```

## Como Executar Testes

```bash
# Backend (JUnit 5 + MockMvc)
cd backend
mvn test

# Frontend (Jest)
cd frontend
npm test
```

## API

Após iniciar o backend:

```text
http://localhost:8080/swagger-ui.html
```

## Deploy

### Backend

https://sistema-de-saude-backend.onrender.com

### API REST

https://sistema-de-saude-backend.onrender.com/api/profissionais

https://sistema-de-saude-backend.onrender.com/api/atendimentos

https://sistema-de-saude-backend.onrender.com/api/exames

### Frontend

https://sistema-de-saude-front.onrender.com/

## Desenvolvido por

* Kenia Teixeira de Paula
* Ana Clara Iannini

## Disciplina

Engenharia de Software II (ES2)

Pontifícia Universidade Católica de Minas Gerais – PUC Minas
