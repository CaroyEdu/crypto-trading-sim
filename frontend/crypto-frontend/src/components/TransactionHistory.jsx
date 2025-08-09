import { useEffect, useState } from "react";
import { getTransactions } from "../services/api";
import {
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";

export default function TransactionHistory({ accountPublicId }) {
  const [transactions, setTransactions] = useState([]);

  const fetchTransactions = async () => {
    const res = await getTransactions(accountPublicId);
    setTransactions(res.data);
  };

  useEffect(() => {
    fetchTransactions();
  }, []);

  return (
    <Paper sx={{ p: 2, mb: 4 }}>
      <Typography variant="h6" gutterBottom>
        Transaction History
      </Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Date</TableCell>
            <TableCell>Type</TableCell>
            <TableCell>Cripto</TableCell>
            <TableCell align="right">Quantity</TableCell>
            <TableCell align="right">Price</TableCell>
            <TableCell align="right">Total</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {transactions.map((tx) => (
            <TableRow key={tx.publicId}>
              <TableCell>{new Date(tx.created).toLocaleString()}</TableCell>
              <TableCell>{tx.type}</TableCell>
              <TableCell>{tx.cryptoSymbol}</TableCell>
              <TableCell align="right">{tx.amount}</TableCell>
              <TableCell align="right">${tx.priceAtTransaction}</TableCell>
              <TableCell align="right">${tx.totalValue}</TableCell>
              <TableCell
                align="right"
                style={{ color: tx.profitOrLoss > 0 ? "green" : "red" }}
              >
                {tx.profitOrLoss !== null ? `$${tx.profitOrLoss}` : "-"}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
}
