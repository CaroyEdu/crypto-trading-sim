import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import AccountTransactions from "./pages/AccountTransactions";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/transactions" element={<AccountTransactions />} />
    </Routes>
  );
}

export default App;
