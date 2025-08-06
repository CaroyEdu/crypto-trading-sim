package com.example.crypto_trading_sim.services;

import com.example.crypto_trading_sim.models.Portfolio;
import com.example.crypto_trading_sim.repositories.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public List<Portfolio> getPortfolioByAccountPublicId(String accountPublicId) {
        return portfolioRepository.findByAccountPublicId(accountPublicId);
    }

    public Portfolio getPortfolioByPublicId(String publicId) {
        return portfolioRepository.findByPublicId(publicId);
    }

    public void addCryptoToPortfolio(String accountPublicId, String cryptoSymbol, BigDecimal amount) {
        Portfolio portfolio = new Portfolio();
        portfolio.setPublicId(UUID.randomUUID().toString());
        portfolio.setAccountPublicId(accountPublicId);
        portfolio.setCryptoSymbol(cryptoSymbol);
        portfolio.setAmount(amount);
        portfolio.setCreated(new Timestamp(System.currentTimeMillis()));
        portfolioRepository.save(portfolio);
    }

    public void updateCryptoAmount(String publicId, BigDecimal newAmount) {
        portfolioRepository.updateAmount(publicId, newAmount);
    }

    public void deleteCryptoFromPortfolio(String publicId) {
        portfolioRepository.deleteByPublicId(publicId);
    }

    public void clearPortfolioForAccount(String accountPublicId) {
        portfolioRepository.deleteAllByAccountPublicId(accountPublicId);
    }
}
