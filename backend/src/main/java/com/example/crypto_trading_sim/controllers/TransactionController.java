package com.example.crypto_trading_sim.controllers;

import com.example.crypto_trading_sim.models.Transaction;
import com.example.crypto_trading_sim.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/account/{accountPublicId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable String accountPublicId) {
        return transactionService.getTransactionsByAccountPublicId(accountPublicId);
    }

    @PostMapping("/create")
    public void createTransaction(
            @RequestParam String accountPublicId,
            @RequestParam String type,
            @RequestParam String cryptoSymbol,
            @RequestParam BigDecimal amount,
            @RequestParam BigDecimal priceAtTransaction,
            @RequestParam(required = false) BigDecimal profitOrLoss
    ) {
        transactionService.createTransaction(
                accountPublicId,
                type,
                cryptoSymbol,
                amount,
                priceAtTransaction,
                null
        );
    }
}
