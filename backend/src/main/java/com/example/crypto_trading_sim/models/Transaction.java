package com.example.crypto_trading_sim.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long id;
    private String publicId;
    private String accountPublicId;
    private String type;
    private String cryptoSymbol;
    private BigDecimal amount;
    private BigDecimal priceAtTransaction;
    private BigDecimal totalValue;
    private BigDecimal profitOrLoss;
    private Timestamp created;
}
