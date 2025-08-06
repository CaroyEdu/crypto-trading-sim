package com.example.crypto_trading_sim.services;

import com.example.crypto_trading_sim.models.Transaction;
import com.example.crypto_trading_sim.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactionsByAccountPublicId(String accountPublicId) {
        return transactionRepository.findByAccountPublicId(accountPublicId);
    }

    public void createTransaction(String accountPublicId, String type, String cryptoSymbol,
                                  BigDecimal amount, BigDecimal priceAtTransaction,
                                  BigDecimal profitOrLoss) {

        BigDecimal totalValue = priceAtTransaction.multiply(amount);
        String publicId = UUID.randomUUID().toString();
        Timestamp created = new Timestamp(System.currentTimeMillis());

        Transaction tx = new Transaction(
                null, // id (auto)
                publicId,
                accountPublicId,
                type,
                cryptoSymbol,
                amount,
                priceAtTransaction,
                totalValue,
                profitOrLoss,
                created
        );

        transactionRepository.save(tx);
    }
}
