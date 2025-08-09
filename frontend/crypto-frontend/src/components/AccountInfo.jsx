import React from "react";
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

function AccountInfo({ account }) {
  const navigate = useNavigate();

  const goToTransactionHistory = () => {
    navigate("/transactions");
  };

  return (
    <div>
      <h2>Hello, {account.name}</h2>
      <p>Balance: ${account.balance}</p>

      <Button
        variant="contained"
        onClick={goToTransactionHistory}
        sx={{ mt: 2 }}
      >
        View Transaction History
      </Button>
    </div>
  );
}

export default AccountInfo;
