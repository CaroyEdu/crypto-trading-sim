import { useState, useEffect } from "react";
import {
  Paper,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Typography,
  Container,
  CircularProgress,
} from "@mui/material";
import { getPortfolio } from "../services/api";

function Portfolio({ account }) {
  const [portfolio, setPortfolio] = useState(null);

  const fetchAccount = async () => {
    if (!account?.publicId) return;
    const res = await getPortfolio(account.publicId);
    setPortfolio(res.data);
  };

  useEffect(() => {
    fetchAccount();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [account]);

  if (!portfolio)
    return (
      <Container maxWidth="md" sx={{ mt: 10 }}>
        <CircularProgress />
      </Container>
    );

  return (
    <Paper elevation={3} sx={{ p: 2, maxHeight: 300, overflowY: "auto" }}>
      <Typography variant="h6" gutterBottom>
        Portfolio
      </Typography>
      <Table size="small" stickyHeader>
        <TableHead>
          <TableRow>
            <TableCell>Crypto</TableCell>
            <TableCell align="right">Amount</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {portfolio.length === 0 ? (
            <TableRow>
              <TableCell colSpan={2} align="center">
                No cryptos in portfolio
              </TableCell>
            </TableRow>
          ) : (
            portfolio.map(({ publicId, cryptoSymbol, amount }) => (
              <TableRow key={publicId}>
                <TableCell>{cryptoSymbol}</TableCell>
                <TableCell align="right">{amount.toString()}</TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </Paper>
  );
}

export default Portfolio;
