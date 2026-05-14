package com.chubeo.DebtZero;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/debtzero_test",
        "spring.datasource.username=test",
        "spring.datasource.password=test",
        "jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
        "jwt.expiration=86400000",
        "jwt.refresh-expiration=604800000",
        "admin.default-password=admin123"
})
class DebtZeroApplicationTests {
    @Test
    void contextLoads() {
    }
}