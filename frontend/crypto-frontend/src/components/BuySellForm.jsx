import { useState, useEffect } from 'react';
import {
  Box,
  Button,
  MenuItem,
  TextField,
  Typography
} from '@mui/material';
import { createTransaction } from '../services/api';

const PAIRS = ['XBT/USD', 'ETH/USD', 'ADA/USD', 'DOT/USD', 'SOL/USD'];

export default function BuySellForm({ accountPublicId, onTransaction, livePrices }) {
  const [type, setType] = useState('BUY');
  const [cryptoSymbol, setCryptoSymbol] = useState('XBT/USD');
  const [amount, setAmount] = useState('');
  const [price, setPrice] = useState(0);

  // Actualiza el precio cada vez que cambie el símbolo o los datos en tiempo real
  useEffect(() => {
    if (livePrices && livePrices[cryptoSymbol]) {
      setPrice(livePrices[cryptoSymbol].last);
    }
  }, [cryptoSymbol, livePrices]);

  const handleSubmit = async () => {
    if (!amount || !price) return;

    await createTransaction({
      accountPublicId,
      type,
      cryptoSymbol,
      amount,
      priceAtTransaction: price,
      profitOrLoss: 0
    });

    setAmount('');
    onTransaction();
  };

  const total = amount && price ? parseFloat(amount) * price : 0;

  return (
    <Box mb={4}>
      <Typography variant="h6" gutterBottom>
        Simulate Buy/Sell
      </Typography>

      <TextField
        select
        value={type}
        onChange={e => setType(e.target.value)}
        sx={{ mr: 2 }}
      >
        <MenuItem value="BUY">Buy</MenuItem>
        <MenuItem value="SELL">Sell</MenuItem>
      </TextField>

      <TextField
        select
        value={cryptoSymbol}
        onChange={e => setCryptoSymbol(e.target.value)}
        sx={{ mr: 2 }}
      >
        {PAIRS.map(p => (
          <MenuItem key={p} value={p}>{p}</MenuItem>
        ))}
      </TextField>

      <TextField
        label="Cantidad"
        type="number"
        value={amount}
        onChange={e => setAmount(e.target.value)}
        sx={{ mr: 2 }}
      />

      <TextField
        label="Precio unitario"
        value={price ? `$${price.toFixed(2)}` : "-"}
        InputProps={{ readOnly: true }}
        sx={{ mr: 2 }}
      />

      <Button
        variant="contained"
        onClick={handleSubmit}
        disabled={!amount || !price}
      >
        Confirm
      </Button>

      <Box mt={2}>
        <Typography variant="body1">
          Total: {amount && price ? `$${total.toFixed(2)}` : "-"}
        </Typography>
      </Box>
    </Box>
  );
}
