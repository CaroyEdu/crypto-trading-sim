# Backend Documentation - Crypto Trading Simulator

## Overview

The backend is built with **Java 21** using **Spring Boot** and **Maven**, connecting to a **PostgreSQL 17.5** database. **Docker** is needed for Testcontainers (you can run the project without it, but not the tests).  
It handles account management, cryptocurrency portfolio tracking, transaction history, and reset functionality.

**Key tools & libraries:**

- **Spring Boot** (REST API services)
- **Flyway** (Database migration management)
- **Lombok** (Cleaner code via annotations)
- **PostgreSQL** (Persistent storage)
- **No ORM** (All database access is via raw SQL queries)
- **Integration Test** (JUnit, Mockito and Testcontainers)

---

## Database Schema

The backend uses the following tables:

### 1. `account`

Stores user account information and balance.

| Column      | Type               | Notes                            |
| ----------- | ------------------ | -------------------------------- |
| `id`        | SERIAL (PK)        | Auto-increment primary key       |
| `public_id` | VARCHAR(50) UNIQUE | Public UUID identifier           |
| `name`      | VARCHAR(250)       | Account holder name              |
| `balance`   | NUMERIC(20,2)      | Current account balance          |
| `created`   | TIMESTAMP          | Creation timestamp (default NOW) |

---

### 2. `portfolio`

Tracks cryptocurrency holdings per account.

| Column              | Type               | Notes                            |
| ------------------- | ------------------ | -------------------------------- |
| `id`                | SERIAL (PK)        | Auto-increment primary key       |
| `public_id`         | VARCHAR(50) UNIQUE | Public UUID identifier           |
| `account_public_id` | VARCHAR(50)        | FK → `account.public_id`         |
| `crypto_symbol`     | VARCHAR(10)        | Cryptocurrency symbol (e.g. BTC) |
| `amount`            | NUMERIC(20,10)     | Quantity of crypto held          |
| `created`           | TIMESTAMP          | Creation timestamp (default NOW) |

**Unique constraint:** `(crypto_symbol, account_public_id)`

---

### 3. `transaction`

Logs all cryptocurrency trades (buy/sell).

| Column                 | Type               | Notes                                                 |
| ---------------------- | ------------------ | ----------------------------------------------------- |
| `id`                   | SERIAL (PK)        | Auto-increment primary key                            |
| `public_id`            | VARCHAR(50) UNIQUE | Public UUID identifier                                |
| `account_public_id`    | VARCHAR(50)        | FK → `account.public_id`                              |
| `type`                 | VARCHAR(10)        | Must be `'BUY'` or `'SELL'`                           |
| `crypto_symbol`        | VARCHAR(10)        | Cryptocurrency symbol                                 |
| `amount`               | NUMERIC(20,10)     | Amount traded                                         |
| `price_at_transaction` | NUMERIC(20,10)     | Price at the time of transaction                      |
| `total_value`          | NUMERIC(20,2)      | `amount * price_at_transaction`                       |
| `profit_or_loss`       | NUMERIC(20,2)      | Profit/loss for sell transactions (nullable for buys) |
| `created`              | TIMESTAMP          | Creation timestamp (default NOW)                      |

---

### Initial Demo Account

The database is seeded with a demo account:

```sql
INSERT INTO account (name, balance, public_id)
VALUES ('DEMO', 10000.00, gen_random_uuid()::text);
```

## API Endpoints

### **AccountController** (`/api/accounts`)

#### `GET /api/accounts/{publicId}`

Get account details by public ID.

**Response:**

```json
{
  "publicId": "UUID",
  "name": "DEMO",
  "balance": 10000.0,
  "created": "2025-08-11T12:00:00"
}
```

#### `POST /api/accounts/{publicId}/reset`

Reset the account balance to $10,000 and clear holdings/transactions.

**Response:**

- `200 OK` if reset successful
- `404 Not Found` if account does not exist

---

### **PortfolioController** (`/api/portfolio`)

#### `GET /api/portfolio/account/{accountPublicId}`

Get all portfolio holdings for a given account.

**Response:**

```json
[
  {
    "publicId": "UUID",
    "cryptoSymbol": "BTC",
    "amount": 0.5,
    "created": "2025-08-11T12:00:00"
  }
]
```

### **TransactionController** (`/api/transactions`)

#### `GET /api/transactions/account/{accountPublicId}`

Get all transactions for a given account.

**Response:**

```json
[
  {
    "publicId": "UUID",
    "type": "BUY",
    "cryptoSymbol": "ETH",
    "amount": 2.5,
    "priceAtTransaction": 1800.5,
    "totalValue": 4501.25,
    "profitOrLoss": null,
    "created": "2025-08-11T12:00:00"
  }
]
```

#### `POST /api/transactions/create`

Create a new transaction.

**Parameters** (form-data or query params):

- `accountPublicId` (String, **required**)
- `type` (`BUY` or `SELL`, **required**)
- `cryptoSymbol` (String, **required**)
- `amount` (BigDecimal, **required**)
- `priceAtTransaction` (BigDecimal, **required**)
- `profitOrLoss` (BigDecimal, optional)

**Response:**

- `200 OK` on success
