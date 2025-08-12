# Crypto Trading Simulator

A full-stack web application simulating cryptocurrency trading with real-time price updates, virtual account management, and transaction history.

## Features

- **Display Top 20 Crypto Prices:**  
  Real-time dynamic updates of the top 20 cryptocurrencies by price. Displays name, symbol, and current price in a clean table.

- **Account Balance and Transactions:**

  - Virtual account initialized with $10,000 starting balance.
  - Users can buy/sell cryptocurrencies by specifying amounts.
  - Buying deducts cost (quantity \* price) from balance.
  - Selling adds proceeds (quantity \* price) to balance.
  - Prevents buying beyond available balance.
  - Clear error messages for invalid inputs (e.g., negative amounts).

- **Transaction History:**  
  Log of all buy/sell transactions showing profit/loss status.

- **Reset Button:**  
  Resets virtual account balance to initial value and clears holdings.

## Backend

- **Technology:** Java 21 (OpenJDK 21.0.8), Spring Boot with Maven
- **Database:** PostgreSQL 17.5 (`crypto_sim_db`) with user `root` and password `password`
- **Database Migrations:** Flyway
- **Code Utilities:** Lombok for cleaner code
- **Data Access:** Direct SQL queries only (JPA or ORMs are _not_ used)
- **API:** RESTful services for prices, transactions, and account management
- **Integration Tests:** JUnit, Mockito with Testcontainers

## Frontend

- **Technology:** React 19 + Vite
- **UI Library:** Material UI for components
- **HTTP Client:** Axios
- **Routing:** react-router-dom

---

# Installation

### Prerequisites

- Java 21 SDK
- Node.js and npm
- PostgreSQL 17.5
- Docker (To run PostgreSQL and Testcontainer Integration Tests)

### PostgreSQL Setup

#### Run PostgreSQL via Docker

```bash
docker run --name postgres-crypto -e POSTGRES_DB=crypto_sim_db -e POSTGRES_USER=root -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:17.5
```

## Backend Setup (Java 21 + Maven)

### Prerequisites

- **Java 21** installed  
  Download from the official Oracle website:  
  [https://www.oracle.com/java/technologies/downloads/#jdk21-windows](https://www.oracle.com/java/technologies/downloads/#jdk21-windows)

- **Set the `JAVA_HOME` environment variable**:

  1. Locate your Java installation folder, e.g.:
     ```
     C:\Program Files\Java\jdk-21
     ```
  2. Press **Win + S**, type **environment variables**, and click **Edit the system environment variables**.
  3. Click **Environment Variables…**.
  4. Under **System variables**, click **New…** (if it doesn’t exist) or **Edit** (if it exists):
     - **Variable name:** `JAVA_HOME`
     - **Variable value:** Java installation path (e.g., `C:\Program Files\Java\jdk-21`)
  5. Edit the `Path` variable (under **System variables**) → **New** → add:
     ```
     %JAVA_HOME%\bin
     ```
  6. Click **OK** on all windows.

  **Verify installation:**

  ```cmd
  java -version
  javac -version
  echo %JAVA_HOME%
  ```

- Docker running locally (required for integration tests).

#### 1. Navigate to the backend folder

```bash
cd backend
```

#### 2. Build the project

Using Maven Wrapper (no need to install Maven globally):

**Windows:**

```cmd
mvnw.cmd clean install
```

**macOS/Linux:**

```bash
./mvnw clean install
```

#### 3. Run the backend

Windows:

```cmd
mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
./mvnw spring-boot:run
```

The backend will start at:
http://localhost:8080

#### 4. Configure the Database (if needed)

Database settings are located in:

```css
src/main/resources/application.properties
```

Update these values to match your local or production database configuration.

### Running Integration Tests

Integration tests use Testcontainers with a real PostgreSQL instance.

**Important**: Make sure Docker is running before executing the tests.

#### Navigate to the backend folder:

```bash
cd backend
```

### Run the tests:

**Windows:**

```cmd
mvnw.cmd test
```

**macOS/Linux:**

```bash
./mvnw test
```

Maven will automatically start the PostgreSQL container for the tests and execute all unit and integration tests.

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend runs by default at http://localhost:5173

Proxy or environment variables point API requests to backend

## Usage

Access the app via browser at http://localhost:5173, to view live top 20 crypto prices updated in real-time, also to buy or sell cryptocurrencies using your virtual account balance. Moreover, you can use the reset button to restart with initial balance and empty holdings:
![Screenshot 1](./frontend/crypto-frontend/screenshots/readme1.png)

Lastely, view transaction history with profit/loss details:
![Screenshot 1](./frontend/crypto-frontend/screenshots/readme2.png)

## Project Structure

```bash
crypto-trading-sim/
├── README.md
├── backend/            # Spring Boot Java backend project
│   ├── src/
│   ├── pom.xml
│   └── ...
└── frontend/           # React + Vite frontend project
    ├── src/
    ├── package.json
    └── ...
```

## Notes

- Security is not implemented as this is a simulated environment.
- No ORM used; all database interactions use raw SQL queries for simplicity and control.
- Flyway manages DB migrations; DDL scripts are included in the backend resources.

## License

This project is licensed under the MIT License.
