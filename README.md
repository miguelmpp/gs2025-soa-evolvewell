
# EvolveWell API – GS 2025 – SOA & WebServices

API REST para monitorar **bem-estar e hábitos de vida de colaboradores** em um cenário de **trabalho remoto, híbrido e presencial**, alinhada ao tema da Global Solution **“O Futuro do Trabalho”**.

O sistema EvolveWell permite cadastrar colaboradores, registrar hábitos de sono, pausas e exercícios físicos, e gerar um **índice de bem-estar com plano semanal sugerido**, consumindo uma **API externa** para complementar as recomendações.

---

## 👥 Integrantes do Grupo

- Matheus Farias – RM 554254
- Miguel Parrado – RM 554007

---

## 🎯 Objetivo do Projeto

Desenvolver um **WebService RESTful**, utilizando princípios de **SOA & WebServices**, que:

1. Centralize os dados de colaboradores em diferentes modelos de trabalho (remoto, híbrido, presencial);
2. Registre **hábitos relacionados à saúde e bem-estar** (sono, exercícios, pausas, nível de estresse);
3. Calcule um **índice de bem-estar** e classifique o **risco de burnout** (Baixo, Médio, Alto);
4. Gere um **plano semanal sugerido** com base nos dados cadastrados;
5. Consuma uma **API REST externa** para sugerir uma atividade leve/relaxante para o colaborador;
6. Utilize **MySQL + Flyway** para versionamento de banco, com camadas bem separadas e tratamento de erros.

---

## 🛠️ Stack Tecnológica

- **Linguagem:** Java 21  
- **Framework:** Spring Boot 3.5.x  
- **Módulos Spring:**
  - Spring Web (APIs REST)
  - Spring Data JPA (persistência)
  - Spring Validation (Bean Validation)
- **Banco de Dados:** MySQL 8  
- **Migrações de BD:** Flyway (`db.migration`)  
- **Build:** Maven  
- **Ferramentas de apoio:**
  - IntelliJ IDEA
  - DBeaver (administração do MySQL)
  - Postman (testes de API)
  - Git + GitHub (versionamento)

---

## 🧱 Arquitetura e Organização de Pacotes

O projeto segue uma **organização por domínio**, seguindo o padrão utilizado em aula (Auto Escola 3ESPA), adaptado para o contexto de bem-estar:

- `br.com.fiap.evolvewell`
  - `controller`
    - `HealthCheckController`
    - `ColaboradorController`
  - `colaborador`
    - `Colaborador` (Entity)
    - `ColaboradorRepository` (JPA Repository)
    - `ModoTrabalho` (Enum: `REMOTO`, `HIBRIDO`, `PRESENCIAL`)
    - `DadosCadastroColaborador` (DTO entrada – POST)
    - `DadosAtualizacaoColaborador` (DTO entrada – PUT)
    - `DadosListagemColaborador` (DTO saída – GET lista)
    - `PlanoBemEstarResponse` (DTO saída – plano de bem-estar)
  - `habitos`
    - `HabitosVida` (Value Object/Embeddable)
    - `DadosHabitosVida` (DTO)
  - `service`
    - `PlanoBemEstarService` (regra de negócio do índice de bem-estar)
  - `integracao`
    - `AtividadeExternaClient` (consumo da API REST externa)
  - `infra.erros`
    - `TratadorDeErros` (`@RestControllerAdvice`)
    - `ErroValidacao`
    - `ErroGeral`

Camadas:

- **Controller:** expõe os endpoints REST e faz a orquestração básica.
- **Service:** concentra a lógica de negócio (cálculo de índice, classificação de risco, texto de recomendação).
- **Domínio (Entity/VO/Enum):** representa colaborador, hábitos de vida e modo de trabalho.
- **Persistência:** Spring Data JPA + MySQL + Flyway.
- **Infra:** tratamento global de erros e integração com API externa.

---

## 🗄️ Banco de Dados e Migrações (Flyway)

Banco configurado: `evolvewell_db`

Migrações em `src/main/resources/db.migration`:

- `V1__create-table-colaboradores.sql`

Tabela principal:

```sql
create table colaboradores (
    id bigint not null auto_increment,
    ativo tinyint(1),
    nome varchar(100) not null,
    email varchar(150) not null unique,
    telefone varchar(20) not null,
    cargo varchar(100) not null,
    departamento varchar(100) not null,
    modo_trabalho varchar(20) not null,
    horas_sono int,
    minutos_exercicio_semana int,
    pausas_turno int,
    nivel_estresse_auto int,
    primary key (id)
);
````

A coluna `ativo` é utilizada para **exclusão lógica** (soft delete).

---

## 🔗 Integração com API REST Externa

Para cumprir o requisito de **consumo de API externa**, o projeto utiliza a **Bored API**:

* URL base: `https://www.boredapi.com/api/activity`

A classe `AtividadeExternaClient` faz uma requisição HTTP simples (`RestTemplate`) para buscar uma sugestão de atividade relaxante, que é incorporada no plano de bem-estar gerado para o colaborador.

Em caso de falha ou indisponibilidade da API externa, o sistema aplica um **fallback**:

```java
return "Reserve 15 minutos para uma pausa sem telas, apenas respirar e alongar.";
```

---

## 📊 Índice de Bem-Estar e Plano Semanal

A lógica está centralizada em `PlanoBemEstarService`.

Entradas consideradas (habitos):

* `horasSono`
* `minutosExercicioSemana`
* `pausasTurno`
* `nivelEstresseAuto` (escala de 0 a 10)

Regras (resumo):

* Cada fator contribui com uma pontuação parcial de 0 a 3, considerando faixas “saudáveis”.
* O score total é normalizado para uma escala **0 a 10** (uma casa decimal).
* Classificação de risco:

    * `>= 7.5` → **Baixo**
    * `>= 5.0` e `< 7.5` → **Médio**
    * `< 5.0` → **Alto**
* O texto de recomendação leva em conta:

    * Índice de bem-estar (manter, ajustar, priorizar recuperação)
    * `modoTrabalho` (`REMOTO`, `HIBRIDO`, `PRESENCIAL`)

Resposta do endpoint de plano de bem-estar:

```json
{
  "colaboradorId": 3,
  "nome": "Ana Pereira",
  "modoTrabalho": "HIBRIDO",
  "indiceBemEstar": 9.2,
  "nivelRisco": "Baixo",
  "recomendacaoGeral": "Manter a rotina atual, garantindo pausas e preservando a qualidade do sono. No modelo híbrido, aproveite os dias presenciais para interação e os remotos para foco.",
  "sugestaoAtividadeExterna": "Reserve 15 minutos para uma pausa sem telas, apenas respirar e alongar."
}
```

---

## 📡 Endpoints Principais

### 1. Health Check

* **GET** `/health-check`
* **Resposta:**

  ```text
  EvolveWell API - OK!
  ```

---

### 2. Cadastrar colaborador

* **POST** `/colaboradores`
* **Body (JSON)**:

```json
{
  "nome": "Ana Pereira",
  "email": "ana.pereira2@empresa.com",
  "telefone": "11987654321",
  "cargo": "Analista de Dados",
  "departamento": "Tecnologia",
  "modoTrabalho": "HIBRIDO",
  "habitos": {
    "horasSono": 7,
    "minutosExercicioSemana": 150,
    "pausasTurno": 3,
    "nivelEstresseAuto": 5
  }
}
```

* **Validações:**

    * `nome`, `email`, `telefone`, `cargo`, `departamento` obrigatórios
    * `email` deve ser válido
    * `telefone` deve corresponder ao padrão `\d{8,15}`
    * `modoTrabalho` não pode ser nulo
    * `habitos` não pode ser nulo

---

### 3. Listar colaboradores ativos (paginação)

* **GET** `/colaboradores`
* Suporta parâmetros de paginação do Spring:

    * `page`, `size`, `sort`
* **Resposta (exemplo):**

```json
{
  "content": [
    {
      "id": 3,
      "nome": "Ana Pereira",
      "email": "ana.pereira2@empresa.com",
      "departamento": "Tecnologia",
      "modoTrabalho": "HIBRIDO"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

---

### 4. Atualizar dados do colaborador

* **PUT** `/colaboradores`
* **Body (JSON):**

```json
{
  "id": 3,
  "telefone": "11999998888",
  "cargo": "Analista Sênior de Dados",
  "departamento": "Inovação",
  "modoTrabalho": "REMOTO",
  "habitos": {
    "horasSono": 6,
    "minutosExercicioSemana": 60,
    "pausasTurno": 2,
    "nivelEstresseAuto": 7
  }
}
```

Campos nulos são ignorados (update parcial).

---

### 5. Exclusão lógica de colaborador

* **DELETE** `/colaboradores/{id}`
* Marca o campo `ativo = false`.
* O colaborador não aparece mais em `GET /colaboradores`, mas permanece no banco.

---

### 6. Plano de bem-estar do colaborador (API externa)

* **GET** `/colaboradores/{id}/plano-bem-estar`
* Gera o índice, a classificação de risco, a recomendação e consulta a API externa para sugestão de atividade.

Em caso de `id` inexistente:

```json
{
  "mensagem": "Colaborador não encontrado"
}
```

---

## ⚠️ Validações e Tratamento de Erros

A validação é feita com **Bean Validation** (`jakarta.validation`) e tratada de forma centralizada pela classe `TratadorDeErros` (`@RestControllerAdvice`).

### Erros de validação (400)

Exemplo de resposta ao enviar JSON inválido no `POST /colaboradores`:

```json
[
  {
    "campo": "email",
    "mensagem": "deve ser um endereço de e-mail bem formado"
  },
  {
    "campo": "cargo",
    "mensagem": "não deve estar em branco"
  },
  {
    "campo": "departamento",
    "mensagem": "não deve estar em branco"
  },
  {
    "campo": "nome",
    "mensagem": "não deve estar em branco"
  },
  {
    "campo": "habitos",
    "mensagem": "não deve ser nulo"
  },
  {
    "campo": "modoTrabalho",
    "mensagem": "não deve ser nulo"
  },
  {
    "campo": "telefone",
    "mensagem": "deve corresponder a \"\\d{8,15}\""
  }
]
```

### Erros de negócio (404)

```json
{
  "mensagem": "Colaborador não encontrado"
}
```

### Erros inesperados (500)

```json
{
  "mensagem": "Ocorreu um erro inesperado. Se persistir, contate o suporte."
}
```

---

## ▶️ Como Rodar o Projeto Localmente

### Pré-requisitos

* JDK 21 instalado
* Maven instalado (ou usar o wrapper `mvnw`)
* MySQL 8 rodando localmente
* Banco criado: `evolvewell_db`
* Usuário e senha configurados no `application.properties`, por exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/evolvewell_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Passos

1. Clonar o repositório:

   ```bash
   git clone https://github.com/miguelmpp/gs2025-soa-evolvewell.git
   cd gs2025-soa-evolvewell
   ```

2. Subir o MySQL e garantir que o banco `evolvewell_db` existe.

3. Rodar o projeto:

   ```bash
   ./mvnw spring-boot:run
   ```

   ou pela IDE (rodando a classe `EvolvewellApplication`).

4. Testar o health check:

   ```text
   GET http://localhost:8080/health-check
   ```

---

## 🧪 Testes de Carga (Planejamento)

Para atender ao critério de **“Testes de carga com ferramentas adequadas (5%)”**, sugerimos o uso de:

* **JMeter** ou **k6**.

### Cenário sugerido

1. Configurar um teste de carga para o endpoint:

    * `GET /colaboradores`
    * `GET /colaboradores/{id}/plano-bem-estar`

2. Massa de teste:

    * 50 a 100 usuários virtuais simultâneos.
    * Ramp-up de 10 a 30 segundos.

3. Métricas observadas:

    * Tempo médio de resposta
    * Throughput (requisições/segundo)
    * Percentis (p95, p99)
    * Taxa de erro (HTTP 5xx)

Os resultados podem ser descritos na entrega textual (não é obrigatório subir scripts de teste no repositório, caso a disciplina não exija).

---

## 📌 Considerações Finais

O projeto **EvolveWell** demonstra:

* Aplicação prática de **SOA & WebServices** com APIs REST;
* Integração entre **Spring Boot, JPA, MySQL e Flyway**;
* Separação clara de camadas, DTOs e VOs;
* Uso de **API externa** para enriquecer a recomendação ao colaborador;
* Tratamento cuidadoso de **validação e erros**;
* Aderência ao tema **“O Futuro do Trabalho”**, focando no bem-estar em modelos remoto, híbrido e presencial.

Este repositório é a base da entrega técnica da Global Solution de SOA & WebServices – 2025.


