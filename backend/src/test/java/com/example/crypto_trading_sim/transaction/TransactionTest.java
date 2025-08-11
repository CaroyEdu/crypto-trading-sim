package com.example.crypto_trading_sim.transaction;

import com.example.crypto_trading_sim.models.Account;
import com.example.crypto_trading_sim.models.Transaction;
import com.example.crypto_trading_sim.repositories.AccountRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TransactionTest {

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

        // Ensure Flyway runs in tests
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private String testAccountId;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = new Account();
        account.setPublicId("acc-123");
        account.setName("Test Account");
        account.setBalance(new BigDecimal("10000.00"));
        account.setCreated(Timestamp.from(Instant.now()));
        accountRepository.save(account);

        this.testAccountId = account.getPublicId();
    }

    @Test
    void createTransaction_andVerifyPersistence() throws Exception {
        mockMvc.perform(post("/api/transactions/create")
                        .param("accountPublicId", testAccountId)
                        .param("type", "BUY")
                        .param("cryptoSymbol", "BTC")
                        .param("amount", "0.1")
                        .param("priceAtTransaction", "30000.00")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(1);

        Transaction saved = transactions.getFirst();
        assertThat(saved.getAccountPublicId()).isEqualTo(testAccountId);
        assertThat(saved.getType()).isEqualTo("BUY");
        assertThat(saved.getCryptoSymbol()).isEqualTo("BTC");
        assertThat(saved.getAmount()).isEqualByComparingTo(new BigDecimal("0.1"));
        assertThat(saved.getPriceAtTransaction()).isEqualByComparingTo(new BigDecimal("30000.00"));
        assertThat(saved.getTotalValue()).isEqualByComparingTo(new BigDecimal("3000.00"));
    }
}
