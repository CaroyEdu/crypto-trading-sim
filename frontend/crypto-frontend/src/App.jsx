import { useEffect, useState } from 'react';
import { Container, CircularProgress, Button } from '@mui/material';
import AccountInfo from './components/AccountInfo';
import TickerTable from './components/TickerTable';
import BuySellForm from './components/BuySellForm';
import TransactionHistory from './components/TransactionHistory';
import { getAccount, resetAccount } from './services/api';

const publicId = '6bb5d398-da1e-4d2d-97ae-dacea4e1c556'; // Testing purposes

function App() {
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

  if (!account) return (
    <Container maxWidth="md" sx={{ mt: 10 }}>
      <CircularProgress />
    </Container>
  );

  const loadTransactions = async () => {
  await fetchAccount(); // o puedes recargar solo el historial si lo tienes por separado
};


  return (
    <Container maxWidth="md" sx={{ mt: 5 }}>
      <AccountInfo account={account} />
      <Button variant="outlined" onClick={handleReset} sx={{ mb: 4 }}>
        Reset Account
      </Button>
      <TickerTable onTickersUpdate={setTickers} />
      <BuySellForm
        accountPublicId={publicId}
        onTransaction={loadTransactions}
        livePrices={tickers}
      />
      <TransactionHistory accountPublicId={publicId} />
    </Container>
  );
}

export default App;
