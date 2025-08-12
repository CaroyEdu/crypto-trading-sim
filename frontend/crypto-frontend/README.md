# Crypto Trading Simulator - Frontend

This is the frontend application for the Crypto Trading Simulator, a web app that simulates cryptocurrency trading with real-time prices, virtual account management, buying/selling functionality, transaction history, and account reset.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Integration](#api-integration)
- [Pages and Components](#pages-and-components)
- [Setup and Running](#setup-and-running)
- [Screenshots](#screenshots)
- [Notes](#notes)

---

## Overview

This frontend displays the top 20 cryptocurrencies with live price updates fetched from the backend, which connects to Kraken's WebSocket API. Users can manage a virtual trading account, buying and selling cryptos with a starting balance. Transactions are tracked and displayed, and the user can reset their account to the initial state.

---

## Features

- **Real-time Prices:** Dynamic updates of the top 20 cryptocurrencies by price.
- **Virtual Account Management:**
  - Starting balance of $10,000.
  - Buy/sell cryptos with immediate updates to balance and holdings.
  - Input validation and error handling.
- **Transaction History:** View all past buy/sell transactions with profit/loss.
- **Reset Account:** Reset balance and holdings to initial state.
- **Responsive UI:** Clean, user-friendly design built with Material UI.

---

## Tech Stack

- **Framework:** React 19 (via Vite for fast build and development)
- **UI Library:** Material UI (MUI) for consistent components and styling
- **HTTP Client:** Axios for API communication with backend
- **Routing:** react-router-dom for client-side navigation
- **State Management:** React built-in hooks (`useState`, `useEffect`, etc.)

---

## Project Structure

```bash
crypto-frontend/
├── public/
├── screenshots/
│ ├── readme1.png
│ └── readme2.png
├── src/
│ ├── components/
│ │ ├── AccountInfo.jsx # Displays user account balance and info
│ │ ├── BuySellForm.jsx # Form for buying and selling cryptos
│ │ ├── Portfolio.jsx # Displays current crypto holdings
│ │ ├── TickerTable.jsx # Shows live top 20 crypto prices
│ │ └── TransactionHistory.jsx # Lists all past transactions
│ ├── pages/
│ │ ├── Home.jsx # Main page combining price, portfolio, and buy/sell
│ │ └── AccountTransactions.jsx # Page to view full transaction history
│ ├── services/
│ │ └── api.js # Axios-based API calls to backend
│ ├── utils/
│ ├── App.jsx # App root component with routing
│ ├── index.css # Global styles
│ └── main.jsx # Entry point of React app
├── package.json
├── vite.config.js
└── README.md # This documentation
```

---

## API Integration

API calls are centralized in `src/services/api.js` using Axios. The backend URL is set to `http://localhost:8080/api`. Key functions include:

- `getAccount(publicId)`: Fetches user account info.
- `getTransactions(publicId)`: Fetches transaction history.
- `createTransaction(payload)`: Posts a buy or sell transaction.
- `resetAccount(publicId)`: Resets the account balance and portfolio.
- `getPortfolio(accountPublicId)`: Retrieves current crypto holdings.

---

## Pages and Components

### Pages

- **Home:** Combines real-time price ticker (`TickerTable`), user account info (`AccountInfo`), buy/sell form (`BuySellForm`), and portfolio overview (`Portfolio`).
- **AccountTransactions:** Displays a detailed list of all transactions through `TransactionHistory`.

### Components

- **AccountInfo.jsx:** Displays current balance and account details.
- **BuySellForm.jsx:** Allows users to specify crypto symbol and amount to buy or sell, with input validation.
- **Portfolio.jsx:** Shows user's current crypto holdings updated after each transaction.
- **TickerTable.jsx:** Shows the top 20 cryptos with real-time prices updated from backend WebSocket data.
- **TransactionHistory.jsx:** Lists all past transactions with profit or loss status.

---

## Setup and Running

### Prerequisites

- Node.js and npm/yarn installed

### Install dependencies and run development server

```bash
cd frontend/crypto-frontend
npm install
npm run dev
```

The app will be available at http://localhost:5173 by default. It communicates with the backend at http://localhost:8080.

### Notes

- This frontend works in tandem with the Java Spring Boot backend that handles API logic, database storage, and WebSocket price feeds.
- No user authentication is implemented as this is a simulated environment.
- Error handling is implemented to provide user-friendly feedback on invalid inputs.
- The project uses React functional components with hooks for clean, modern React code.
