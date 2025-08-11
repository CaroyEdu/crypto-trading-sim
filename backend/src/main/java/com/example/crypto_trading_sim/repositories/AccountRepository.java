package com.example.crypto_trading_sim.repositories;

import com.example.crypto_trading_sim.models.Account;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

@Repository
@AllArgsConstructor
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;

    public Account findByPublicId(String publicId) {
        String sql = "SELECT * FROM account WHERE public_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Account.class), publicId);
    }

    public int subtractBalanceIfSufficient(String publicId, double amount) {
        String sql = "UPDATE account SET balance = balance - ? WHERE public_id = ? AND balance >= ?";
        return jdbcTemplate.update(sql, amount, publicId, amount);
    }

    public void addBalance(String publicId, double amount) {
        String sql = "UPDATE account SET balance = balance + ? WHERE public_id = ?";
        jdbcTemplate.update(sql, amount, publicId);
    }

    public void updateBalanceByPublicId(String publicId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE public_id = ?";
        jdbcTemplate.update(sql, newBalance, publicId);
    }

    public void deleteAll() {
        String sql = "DELETE FROM account";
        jdbcTemplate.update(sql);
    }

    public void save(Account account) {
        String sql = "INSERT INTO account (public_id, name, balance, created) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                account.getPublicId(),
                account.getName(),
                account.getBalance(),
                account.getCreated()
        );
    }
}
