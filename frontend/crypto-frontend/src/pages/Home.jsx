import { useState, useEffect, useRef } from "react";
import { Container, CircularProgress, Stack, Box, Paper } from "@mui/material";
import AccountInfo from "../components/AccountInfo";
import BuySellForm from "../components/BuySellForm";
import Portfolio from "../components/Portfolio";
import TickerTable from "../components/TickerTable";
import { getAccount, resetAccount } from "../services/api";

const publicId = "6bb5d398-da1e-4d2d-97ae-dacea4e1c556";

export default function Home() {
  const [account, setAccount] = useState(null);
  const [tickers, setTickers] = useState({});
  const leftColumnRef = useRef(null);
  const [leftHeight, setLeftHeight] = useState(0);

  const fetchAccount = async () => {
    const res = await getAccount(publicId);
    setAccount(res.data);
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

  if (!account)
    return (
      <Container maxWidth="md" sx={{ mt: 10 }}>
        <CircularProgress />
      </Container>
    );

  return (
    <Container maxWidth="lg" sx={{ mt: 5 }}>
      <Stack
        direction="row"
        spacing={2}
        sx={{ alignItems: "flex-start", justifyContent: "center" }}
      >
        {/* Columna izquierda */}
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
    </Container>
  );
}
