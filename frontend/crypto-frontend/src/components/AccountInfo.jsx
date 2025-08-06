import { Typography, Box } from '@mui/material';

export default function AccountInfo({ account }) {
  return (
    <Box mb={4}>
      <Typography variant="h4">Hello, {account.name}</Typography>
      <Typography variant="h6">You currently have {account.balance.toFixed(2)} Coins</Typography>
    </Box>
  );
}
