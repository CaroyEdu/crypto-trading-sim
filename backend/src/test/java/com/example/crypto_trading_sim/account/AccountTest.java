package com.example.crypto_trading_sim.account;

import com.example.crypto_trading_sim.models.Account;
import com.example.crypto_trading_sim.models.Portfolio;
import com.example.crypto_trading_sim.models.Transaction;
import com.example.crypto_trading_sim.repositories.AccountRepository;
import com.example.crypto_trading_sim.repositories.PortfolioRepository;
import com.example.crypto_trading_sim.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AccountTest {

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("10000.00");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("sql/init.sql");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    private String testAccountId;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = new Account();
        account.setPublicId("acc-123");
        account.setName("Test Account");
        account.setBalance(new BigDecimal("5000.00"));
        account.setCreated(Timestamp.from(Instant.now()));
        accountRepository.save(account);
        this.testAccountId = account.getPublicId();

        Transaction tx = new Transaction();
        tx.setAccountPublicId(testAccountId);
        tx.setType("BUY");
        tx.setCryptoSymbol("BTC");
        tx.setAmount(new BigDecimal("0.1"));
        tx.setPriceAtTransaction(new BigDecimal("30000.00"));
        tx.setTotalValue(new BigDecimal("3000.00"));
        tx.setCreated(Timestamp.from(Instant.now()));
        tx.setPublicId(UUID.randomUUID().toString());
        tx.setProfitOrLoss(BigDecimal.ZERO);
        transactionRepository.save(tx);

        Portfolio portfolio = new Portfolio();
        portfolio.setAccountPublicId(testAccountId);
        portfolio.setCryptoSymbol("BTC");
        portfolio.setAmount(new BigDecimal("0.5"));
        portfolio.setCreated(Timestamp.from(Instant.now()));
        portfolio.setPublicId(UUID.randomUUID().toString());
        portfolioRepository.save(portfolio);
    }

    @Test
    void resetAccount_shouldClearTransactionsAndPortfolioAndResetBalance() throws Exception {
        mockMvc.perform(post("/api/accounts/" + testAccountId + "/reset")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        Account refreshedAccount = accountRepository.findByPublicId(testAccountId);
        assertThat(refreshedAccount.getBalance()).isEqualByComparingTo(INITIAL_BALANCE);

        assertThat(transactionRepository.findByAccountPublicId(testAccountId)).isEmpty();
        assertThat(portfolioRepository.findByAccountPublicId(testAccountId)).isEmpty();
    }
}