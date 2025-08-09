import TransactionHistory from "../components/TransactionHistory";

const publicId = "6bb5d398-da1e-4d2d-97ae-dacea4e1c556";

export default function AccountTransactions() {
  return <TransactionHistory accountPublicId={publicId} />;
}
