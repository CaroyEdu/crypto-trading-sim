package com.example.crypto_trading_sim.controllers;

import com.example.crypto_trading_sim.models.Portfolio;
import com.example.crypto_trading_sim.services.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@AllArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/account/{accountPublicId}")
    public List<Portfolio> getPortfolioByAccount(@PathVariable String accountPublicId) {
        return portfolioService.getPortfolioByAccountPublicId(accountPublicId);
    }

    @GetMapping("/{publicId}")
    public Portfolio getPortfolioByPublicId(@PathVariable String publicId) {
        return portfolioService.getPortfolioByPublicId(publicId);
    }

    @PostMapping("/add")
    public void addCryptoToPortfolio(@RequestParam String accountPublicId,
                                     @RequestParam String cryptoSymbol,
                                     @RequestParam BigDecimal amount) {
        portfolioService.addCryptoToPortfolio(accountPublicId, cryptoSymbol, amount);
    }

    @PutMapping("/{publicId}/amount")
    public void updateAmount(@PathVariable String publicId, @RequestParam BigDecimal newAmount) {
        portfolioService.updateCryptoAmount(publicId, newAmount);
    }

    @DeleteMapping("/{publicId}")
    public void deleteCrypto(@PathVariable String publicId) {
        portfolioService.deleteCryptoFromPortfolio(publicId);
    }

    @DeleteMapping("/account/{accountPublicId}/clear")
    public void clearPortfolio(@PathVariable String accountPublicId) {
        portfolioService.clearPortfolioForAccount(accountPublicId);
    }
}