package com.example.crypto_trading_sim.controllers;

import com.example.crypto_trading_sim.models.Portfolio;
import com.example.crypto_trading_sim.services.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}