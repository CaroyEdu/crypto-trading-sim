package com.example.crypto_trading_sim.repositories;

import com.example.crypto_trading_sim.models.Portfolio;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@AllArgsConstructor
public class PortfolioRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final class PortfolioRowMapper implements RowMapper<Portfolio> {
        @Override
        public Portfolio mapRow(ResultSet rs, int rowNum) throws SQLException {
            Portfolio portfolio = new Portfolio();
            portfolio.setId(rs.getLong("id"));
            portfolio.setPublicId(rs.getString("public_id"));
            portfolio.setAccountPublicId(rs.getString("account_public_id"));
            portfolio.setCryptoSymbol(rs.getString("crypto_symbol"));
            portfolio.setAmount(rs.getBigDecimal("amount"));
            portfolio.setCreated(rs.getTimestamp("created"));
            return portfolio;
        }
    }

    public List<Portfolio> findByAccountPublicId(String accountPublicId) {
        String sql = "SELECT * FROM portfolio WHERE account_public_id = :accountPublicId";
        Map<String, Object> params = Map.of("accountPublicId", accountPublicId);
        return jdbcTemplate.query(sql, params, new PortfolioRowMapper());
    }

    public Portfolio findByPublicId(String publicId) {
        String sql = "SELECT * FROM portfolio WHERE public_id = :publicId";
        Map<String, Object> params = Map.of("publicId", publicId);
        return jdbcTemplate.queryForObject(sql, params, new PortfolioRowMapper());
    }

    public void save(Portfolio portfolio) {
        String sql = "INSERT INTO portfolio (public_id, account_public_id, crypto_symbol, amount, created) " +
                "VALUES (:publicId, :accountPublicId, :cryptoSymbol, :amount, :created)";
        Map<String, Object> params = Map.of(
                "publicId", portfolio.getPublicId(),
                "accountPublicId", portfolio.getAccountPublicId(),
                "cryptoSymbol", portfolio.getCryptoSymbol(),
                "amount", portfolio.getAmount(),
                "created", portfolio.getCreated()
        );
        jdbcTemplate.update(sql, params);
    }

    public void updateAmount(String publicId, java.math.BigDecimal newAmount) {
        String sql = "UPDATE portfolio SET amount = :amount WHERE public_id = :publicId";
        Map<String, Object> params = Map.of(
                "amount", newAmount,
                "publicId", publicId
        );
        jdbcTemplate.update(sql, params);
    }

    public void deleteByPublicId(String publicId) {
        String sql = "DELETE FROM portfolio WHERE public_id = :publicId";
        jdbcTemplate.update(sql, Map.of("publicId", publicId));
    }

    public void deleteAllByAccountPublicId(String accountPublicId) {
        String sql = "DELETE FROM portfolio WHERE account_public_id = :accountPublicId";
        jdbcTemplate.update(sql, Map.of("accountPublicId", accountPublicId));
    }
}