import { useState, useEffect } from "react";
import {
  Box,
  Button,
  MenuItem,
  TextField,
  Typography,
  Snackbar,
  Alert,
  Stack,
  Paper,
} from "@mui/material";
import { createTransaction } from "../services/api";

const PAIRS = [
  "BTC/USD",
  "ETH/USD",
  "XRP/USD",
  "LTC/USD",
  "BCH/USD",
  "ADA/USD",
  "DOT/USD",
  "SOL/USD",
  "LINK/USD",
  "MATIC/USD",
  "DOGE/USD",
  "UNI/USD",
  "AVAX/USD",
  "ATOM/USD",
  "ALGO/USD",
  "XLM/USD",
  "TRX/USD",
  "FIL/USD",
  "AAVE/USD",
  "COMP/USD",
];

export default function BuySellForm({
  accountPublicId,
  onTransaction,
  livePrices,
}) {
  const [type, setType] = useState("BUY");
  const [cryptoSymbol, setCryptoSymbol] = useState("BTC/USD");
  const [amount, setAmount] = useState("");
  const [price, setPrice] = useState(0);

  const [notification, setNotification] = useState({
    open: false,
    message: "",
    severity: "success",
  });

  useEffect(() => {
    if (livePrices && livePrices[cryptoSymbol]) {
      setPrice(livePrices[cryptoSymbol].last);
    }
  }, [cryptoSymbol, livePrices]);

  const handleSubmit = async () => {
    if (!amount || !price) return;

    try {
      await createTransaction({
        accountPublicId,
        type,
        cryptoSymbol,
        amount,
        priceAtTransaction: price,
        profitOrLoss: 0,
      });
      setAmount("");
      setNotification({
        open: true,
        message: `Transaction successful: ${type} ${amount} ${cryptoSymbol}`,
        severity: "success",
      });
      onTransaction();
    } catch (error) {
      const backendMessage =
        error?.response?.data?.message || "An error occurred";
      setNotification({
        open: true,
        message: backendMessage,
        severity: "error",
      });
    }
  };

  const total = amount && price ? parseFloat(amount) * price : 0;

  return (
    <Paper sx={{ p: 3 }} elevation={3}>
      <Typography variant="h6" gutterBottom>
        Simulate Buy/Sell
      </Typography>

      <Stack direction="row" spacing={2} mb={2} alignItems="center">
        <TextField
          select
          label="Type"
          value={type}
          onChange={(e) => setType(e.target.value)}
          sx={{ minWidth: 100, flex: 1 }}
        >
          <MenuItem value="BUY">Buy</MenuItem>
          <MenuItem value="SELL">Sell</MenuItem>
        </TextField>

        <TextField
          select
          label="Crypto"
          value={cryptoSymbol}
          onChange={(e) => setCryptoSymbol(e.target.value)}
          sx={{ minWidth: 120, flex: 2 }}
        >
          {PAIRS.map((p) => (
            <MenuItem key={p} value={p}>
              {p}
            </MenuItem>
          ))}
        </TextField>
      </Stack>

      <Stack direction="row" spacing={2} mb={2} alignItems="center">
        <TextField
          label="Amount"
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          sx={{ flex: 1 }}
        />
      </Stack>

      <Box
        mb={2}
        sx={{
          fontWeight: "bold",
          fontSize: 18,
          color: price ? "primary.main" : "text.secondary",
        }}
      >
        Total: {amount && price ? `$${total.toFixed(2)}` : "-"}
      </Box>

      <Button
        variant="contained"
        onClick={handleSubmit}
        disabled={!amount || !price}
        fullWidth
        size="large"
      >
        Confirm
      </Button>

      <Snackbar
        open={notification.open}
        autoHideDuration={4000}
        onClose={() => setNotification((prev) => ({ ...prev, open: false }))}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={() => setNotification((prev) => ({ ...prev, open: false }))}
          severity={notification.severity}
          sx={{ width: "100%" }}
        >
          {notification.message}
        </Alert>
      </Snackbar>
    </Paper>
  );
}
