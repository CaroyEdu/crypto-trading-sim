import { useLocation, useNavigate, Navigate } from "react-router-dom";
import { Box, Button, Typography, Paper } from "@mui/material";
import TransactionHistory from "../components/TransactionHistory";

export default function AccountTransactions() {
  const location = useLocation();
  const navigate = useNavigate();
  const accountPublicId = location.state?.accountPublicId;
  const accountName = location.state?.accountName || "Account";

  if (!accountPublicId) {
    return <Navigate to="/" replace />;
  }

  return (
    <Box
      sx={{
        minHeight: "80vh",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "flex-start",
        mt: 5,
        px: 2,
      }}
    >
      <Button
        variant="outlined"
        onClick={() => navigate(-1)}
        sx={{ alignSelf: "flex-start", mb: 2 }}
      >
        ← Back
      </Button>

      <Typography variant="h5" gutterBottom>
        Transactions for {accountName}
      </Typography>

      <Paper
        elevation={3}
        sx={{
          width: "100%",
          maxWidth: 900,
          p: 2,
          boxSizing: "border-box",
        }}
      >
        <TransactionHistory accountPublicId={accountPublicId} />
      </Paper>
    </Box>
  );
}
