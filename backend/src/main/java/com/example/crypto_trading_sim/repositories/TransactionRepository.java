package com.example.crypto_trading_sim.repositories;

import com.example.crypto_trading_sim.models.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Transaction(
                    rs.getLong("id"),
                    rs.getString("public_id"),
                    rs.getString("account_public_id"),
                    rs.getString("type"),
                    rs.getString("crypto_symbol"),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("price_at_transaction"),
                    rs.getBigDecimal("total_value"),
                    rs.getBigDecimal("profit_or_loss"),
                    rs.getTimestamp("created")
            );
        }
    }

    public List<Transaction> findByAccountPublicId(String accountPublicId) {
        String sql = "SELECT * FROM transaction WHERE account_public_id = :accountPublicId ORDER BY created DESC";
        Map<String, Object> params = Map.of("accountPublicId", accountPublicId);
        return jdbcTemplate.query(sql, params, new TransactionRowMapper());
    }

    public void save(Transaction transaction) {
        String sql = "INSERT INTO transaction (public_id, account_public_id, type, crypto_symbol, amount, price_at_transaction, total_value, profit_or_loss, created) " +
                "VALUES (:publicId, :accountPublicId, :type, :cryptoSymbol, :amount, :priceAtTransaction, :totalValue, :profitOrLoss, :created)";
        Map<String, Object> params = Map.of(
                "publicId", transaction.getPublicId(),
                "accountPublicId", transaction.getAccountPublicId(),
                "type", transaction.getType(),
                "cryptoSymbol", transaction.getCryptoSymbol(),
                "amount", transaction.getAmount(),
                "priceAtTransaction", transaction.getPriceAtTransaction(),
                "totalValue", transaction.getTotalValue(),
                "profitOrLoss", transaction.getProfitOrLoss(),
                "created", transaction.getCreated()
        );
        jdbcTemplate.update(sql, params);
    }

    public void deleteAllByAccountPublicId(String accountPublicId) {
        String sql = "DELETE FROM transaction WHERE account_public_id = :accountPublicId";
        jdbcTemplate.update(sql, Map.of("accountPublicId", accountPublicId));
    }

    public void deleteAll() {
        String sql = "DELETE FROM \"transaction\"";
        jdbcTemplate.update(sql, Map.of());
    }

    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transaction";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Transaction tx = new Transaction();
            tx.setId(rs.getLong("id"));
            tx.setPublicId(rs.getString("public_id"));
            tx.setAccountPublicId(rs.getString("account_public_id"));
            tx.setType(rs.getString("type"));
            tx.setCryptoSymbol(rs.getString("crypto_symbol"));
            tx.setAmount(rs.getBigDecimal("amount"));
            tx.setPriceAtTransaction(rs.getBigDecimal("price_at_transaction"));
            tx.setTotalValue(rs.getBigDecimal("total_value"));
            tx.setProfitOrLoss(rs.getBigDecimal("profit_or_loss"));
            tx.setCreated(Timestamp.valueOf(rs.getTimestamp("created").toLocalDateTime()));
            return tx;
        });
    }

}
