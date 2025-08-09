package com.example.crypto_trading_sim.controllers;

import com.example.crypto_trading_sim.models.Account;
import com.example.crypto_trading_sim.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{publicId}")
    public ResponseEntity<Account> getAccountByPublicId(@PathVariable String publicId) {
        Account account = accountService.findByPublicId(publicId);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{publicId}/reset")
    public ResponseEntity<Void> resetAccountBalance(@PathVariable String publicId) {
        boolean success = accountService.resetBalance(publicId);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{publicId}/balance")
    public ResponseEntity<String> updateBalance(@PathVariable String publicId, @RequestParam BigDecimal newBalance) {
        boolean updated = accountService.updateBalance(publicId, newBalance);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account not found with publicId: " + publicId);
        }
        return ResponseEntity.ok("Balance updated to " + newBalance);
    }
}
