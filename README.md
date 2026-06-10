# Sistema de Saúde - ES2

Projeto desenvolvido para a disciplina **Engenharia de Software II (ES2)** com o objetivo de implementar um sistema de gerenciamento de atendimentos na área da saúde utilizando arquitetura web.

## Tecnologias Utilizadas

### Backend

* Java 17
* Spring Boot 3
* Spring Data JPA
* Hibernate
* PostgreSQL (Neon Database)
* Maven
* Swagger/OpenAPI

### Frontend

* React
* Axios
* React Router DOM

### Testes

* JUnit 5
* Spring Boot Test
* MockMvc

---

## Funcionalidades

### Profissionais de Saúde

* Cadastrar profissional
* Consultar profissional
* Atualizar profissional
* Excluir profissional

Campos:

* ID
* Nome
* Endereço
* Email
* Telefone
* Categoria (Médico, Psicólogo, Fisioterapeuta)

### Atendimentos

* Cadastrar atendimento
* Consultar atendimento
* Atualizar atendimento
* Excluir atendimento

Campos:

* ID
* Data
* Horário
* Título
* Link de videoconferência
* Receita/Orientação
* Profissional responsável

### Exames Laboratoriais

* Cadastrar exame
* Consultar exame
* Atualizar exame
* Excluir exame

Campos:

* ID
* Descrição
* Psicologia
* Atendimento relacionado

---

## Modelo de Relacionamento

Profissional de Saúde (1) → (N) Atendimento

Atendimento (1) → (N) Exame Laboratorial

---

## Estrutura do Projeto

```text
sistema_de_saude/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── application.properties
│
├── frontend/
│   ├── src/
│   ├── public/
│   └── package.json
│
└── README.md
```

---

## Configuração do Banco de Dados

O projeto utiliza PostgreSQL hospedado no Neon.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:postgresql://HOST:5432/DATABASE
spring.datasource.username=USUARIO
spring.datasource.password=SENHA
```

---

## Executando o Backend

Entrar na pasta:

```bash
cd backend
```

Instalar dependências e compilar:

```bash
mvn clean install
```

Executar:

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

---

## Documentação Swagger

Após iniciar a aplicação:

```text
http://localhost:8080/swagger-ui.html
```

---

## Testes

Executar todos os testes:

```bash
mvn test
```

---

## Autor

Kenia Teixeira

Disciplina: Engenharia de Software II

PUC Minas
