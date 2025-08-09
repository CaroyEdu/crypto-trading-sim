package com.example.crypto_trading_sim.services;

import com.example.crypto_trading_sim.models.Account;
import com.example.crypto_trading_sim.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountService {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(10000.00);
    private final AccountRepository accountRepository;
    private final PortfolioService portfolioService;
    private final TransactionService transactionService;

    public Account findByPublicId(String publicId) {
        return accountRepository.findByPublicId(publicId);
    }

    public boolean resetAccount(String publicId) {
        accountRepository.updateBalanceByPublicId(publicId, INITIAL_BALANCE);
        portfolioService.clearPortfolioForAccount(publicId);
        transactionService.clearPortfolioForAccount(publicId);
        return true;
    }

    public boolean updateBalance(String publicId, BigDecimal newBalance) {
        Account account = accountRepository.findByPublicId(publicId);
        if (account == null) {
            return false;
        }
        account.setBalance(newBalance);
        accountRepository.updateBalanceByPublicId(publicId, newBalance);
        return true;
    }
}