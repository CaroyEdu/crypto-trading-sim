import { useState, useEffect } from "react";
import { Container, CircularProgress, Stack, Button } from "@mui/material";
import AccountInfo from "../components/AccountInfo";
import BuySellForm from "../components/BuySellForm";
import Portfolio from "../components/Portfolio";
import TickerTable from "../components/TickerTable";
import { getAccount, resetAccount } from "../services/api";

const publicId = "6bb5d398-da1e-4d2d-97ae-dacea4e1c556";

export default function Home() {
  const [account, setAccount] = useState(null);
  const [tickers, setTickers] = useState({});

  const fetchAccount = async () => {
    const res = await getAccount(publicId);
    setAccount(res.data);
  };

  useEffect(() => {
    fetchAccount();
  }, []);

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
    <Stack
      direction="row"
      spacing={2}
      sx={{
        justifyContent: "space-evenly",
        alignItems: "center",
      }}
    >
      <Stack
        direction="column"
        spacing={2}
        sx={{
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Container maxWidth="md" sx={{ mt: 5 }}>
          <AccountInfo account={account} />
          <Button variant="outlined" onClick={handleReset} sx={{ mb: 4 }}>
            Reset Account
          </Button>

          <BuySellForm
            accountPublicId={publicId}
            onTransaction={loadTransactions}
            livePrices={tickers}
          />
          <Portfolio account={account} />
        </Container>
      </Stack>
      <TickerTable onTickersUpdate={setTickers} />
    </Stack>
  );
}
