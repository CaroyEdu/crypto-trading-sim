package com.example.crypto_trading_sim.services;

import com.example.crypto_trading_sim.exceptions.TransactionException;
import com.example.crypto_trading_sim.models.Portfolio;
import com.example.crypto_trading_sim.models.Transaction;
import com.example.crypto_trading_sim.repositories.AccountRepository;
import com.example.crypto_trading_sim.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final PortfolioService portfolioService;

    public List<Transaction> getTransactionsByAccountPublicId(String accountPublicId) {
        return transactionRepository.findByAccountPublicId(accountPublicId);
    }

    public void clearPortfolioForAccount(String accountPublicId) {
        transactionRepository.deleteAllByAccountPublicId(accountPublicId);
    }

    @Transactional
    public void createTransaction(String accountPublicId, String type, String cryptoSymbol, BigDecimal amount, BigDecimal priceAtTransaction, BigDecimal profitOrLoss) {
        BigDecimal totalValue = priceAtTransaction.multiply(amount);

        if ("BUY".equalsIgnoreCase(type)) {
            int updatedRows = accountRepository.subtractBalanceIfSufficient(accountPublicId, totalValue.doubleValue());
            if (updatedRows == 0) {
                throw new TransactionException("Insufficient funds for this transaction");
            }

            Optional<Portfolio> existingPortfolio = portfolioService
                    .getPortfolioByAccountPublicId(accountPublicId)
                    .stream()
                    .filter(p -> p.getCryptoSymbol().equalsIgnoreCase(cryptoSymbol))
                    .findFirst();

            if (existingPortfolio.isPresent()) {
                BigDecimal newAmount = existingPortfolio.get().getAmount().add(amount);
                portfolioService.updateCryptoAmount(existingPortfolio.get().getPublicId(), newAmount);
            } else {
                portfolioService.addCryptoToPortfolio(accountPublicId, cryptoSymbol, amount);
            }

        } else if ("SELL".equalsIgnoreCase(type)) {
            Optional<Portfolio> existingPortfolio = portfolioService
                    .getPortfolioByAccountPublicId(accountPublicId)
                    .stream()
                    .filter(p -> p.getCryptoSymbol().equalsIgnoreCase(cryptoSymbol))
                    .findFirst();

            if (existingPortfolio.isEmpty() || existingPortfolio.get().getAmount().compareTo(amount) < 0) {
                throw new TransactionException("Insufficient crypto holdings for this transaction");
            }

            BigDecimal newAmount = existingPortfolio.get().getAmount().subtract(amount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                portfolioService.updateCryptoAmount(existingPortfolio.get().getPublicId(), newAmount);
            } else {
                portfolioService.deleteCryptoFromPortfolio(existingPortfolio.get().getPublicId());
            }

            accountRepository.addBalance(accountPublicId, totalValue.doubleValue());

        } else {
            throw new TransactionException("Invalid transaction type: " + type);
        }

        Transaction tx = new Transaction(
                null,
                UUID.randomUUID().toString(),
                accountPublicId,
                type,
                cryptoSymbol,
                amount,
                priceAtTransaction,
                totalValue,
                profitOrLoss,
                new Timestamp(System.currentTimeMillis())
        );

        transactionRepository.save(tx);
    }
}
