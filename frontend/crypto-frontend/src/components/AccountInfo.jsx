import { Button, Box, Typography, Stack, Paper } from "@mui/material";
import { useNavigate } from "react-router-dom";

function AccountInfo({ account, onReset }) {
  const navigate = useNavigate();

  const goToTransactionHistory = () => {
    navigate("/transactions", {
      state: { accountPublicId: account.publicId, accountName: account.name },
    });
  };

  const handleResetClick = () => {
    if (
      window.confirm(
        "Are you sure you want to reset the balance of the account?"
      )
    ) {
      onReset();
    }
  };

  return (
    <Paper elevation={3}>
      <Box
        sx={{
          width: "100%",
          px: 3,
          py: 2,
          boxSizing: "border-box",
        }}
      >
        <Typography variant="h5" gutterBottom>
          Hello, {account.name}
        </Typography>

        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            mb: 3,
            backgroundColor: "#f5f5f5",
            borderRadius: 1,
            p: 1,
            fontSize: 24,
          }}
        >
          <span role="img" aria-label="wallet" style={{ marginRight: 8 }}>
            💰
          </span>
          <Typography variant="h6" fontWeight="bold">
            Balance: ${account.balance.toFixed(2)}
          </Typography>
        </Box>

        <Stack spacing={2}>
          <Button
            variant="contained"
            onClick={goToTransactionHistory}
            fullWidth
            size="large"
          >
            Transaction History
          </Button>

          <Button
            variant="contained"
            color="error"
            onClick={handleResetClick}
            fullWidth
            size="large"
          >
            Reset Balance
          </Button>
        </Stack>
      </Box>
    </Paper>
  );
}

export default AccountInfo;
