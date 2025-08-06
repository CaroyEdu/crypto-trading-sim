package com.example.crypto_trading_sim.repositories;

import com.example.crypto_trading_sim.models.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Account findByPublicId(String publicId) {
        String sql = "SELECT * FROM account WHERE public_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Account.class), publicId);
    }

    public void updateBalanceByPublicId(String publicId, double newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE public_id = ?";
        jdbcTemplate.update(sql, newBalance, publicId);
    }
}
