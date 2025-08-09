import { useState, useEffect, useRef } from "react";
import {
  Container,
  CircularProgress,
  Stack,
  Box,
  Paper,
  Snackbar,
  Alert,
} from "@mui/material";
import AccountInfo from "../components/AccountInfo";
import BuySellForm from "../components/BuySellForm";
import Portfolio from "../components/Portfolio";
import TickerTable from "../components/TickerTable";
import { getAccount, resetAccount } from "../services/api";

const publicId = "1"; // TESTING PURPOSES

export default function Home() {
  const [account, setAccount] = useState(null);
  const [tickers, setTickers] = useState({});
  const [error, setError] = useState(null);
  const leftColumnRef = useRef(null);
  const [leftHeight, setLeftHeight] = useState(0);

  const fetchAccount = async () => {
    try {
      const res = await getAccount(publicId);
      setAccount(res.data);
      setError(null); // limpiar error si éxito
    } catch (error) {
      setError(
        error.response?.status === 404
          ? "Backend not found (404)"
          : "Backend is not reachable"
      );
    }
  };

  useEffect(() => {
    fetchAccount();
  }, []);

  useEffect(() => {
    if (leftColumnRef.current) {
      setLeftHeight(leftColumnRef.current.clientHeight);
    }
  }, [account]);

  const handleReset = async () => {
    await resetAccount(publicId);
    await fetchAccount();
  };

  const loadTransactions = async () => {
    await fetchAccount();
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 5 }}>
      {!account ? (
        <Box
          sx={{
            height: "80vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <CircularProgress />
        </Box>
      ) : (
        <Stack
          direction="row"
          spacing={2}
          sx={{ alignItems: "flex-start", justifyContent: "center" }}
        >
          <Stack
            ref={leftColumnRef}
            direction="column"
            spacing={2}
            sx={{
              flex: 1.2,
              maxWidth: 520,
              justifyContent: "center",
            }}
          >
            <Paper
              elevation={3}
              sx={{
                p: 2,
                flex: 1,
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
              }}
            >
              <AccountInfo account={account} onReset={handleReset} />
            </Paper>

            <Paper elevation={3} sx={{ p: 2, flex: 1 }}>
              <BuySellForm
                accountPublicId={publicId}
                onTransaction={loadTransactions}
                livePrices={tickers}
              />
            </Paper>

            <Paper
              elevation={3}
              sx={{
                p: 2,
                flex: 1,
                maxHeight: 150,
                overflowY: "auto",
              }}
            >
              <Portfolio account={account} />
            </Paper>
          </Stack>
          <Box
            sx={{
              flex: 1,
              height: leftHeight,
              overflowY: "auto",
              overflowX: "visible",
              boxShadow: 3,
              borderRadius: 1,
              minWidth: 800,
            }}
          >
            <TickerTable onTickersUpdate={setTickers} />
          </Box>
        </Stack>
      )}

      <Snackbar
        open={!!error}
        onClose={() => setError(null)}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          severity="error"
          onClose={() => setError(null)}
          sx={{ width: "100%" }}
        >
          {error}
        </Alert>
      </Snackbar>
    </Container>
  );
}
