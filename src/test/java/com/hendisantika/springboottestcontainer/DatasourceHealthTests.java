package com.hendisantika.springboottestcontainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-testcontainer
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/7/23
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
@Testcontainers
@SpringBootTest(
//        properties = {
//                "management.endpoint.health.show-details=always",
//                "spring.datasource.url=jdbc:tc:postgres:15-alpine:///test"
//        },
        webEnvironment = RANDOM_PORT
)
public class DatasourceHealthTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Database status will be UP and Database name should be PostgreSQL")
    void databaseIsAvailable() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        var response = restTemplate.getForEntity("/actuator/health", String.class);

        assertThat(response.getBody()).isNotNull();

        JsonNode root = new ObjectMapper().readTree(response.getBody());
        JsonNode dbComponentNode = root.get("components").get("db");

        String dbStatus = dbComponentNode.get("status").asText();
        String dbName = dbComponentNode.get("details").get("database").asText();

        assertThat(dbStatus).isEqualTo("UP");
        assertThat(dbName).isEqualTo("postgresql");
    }

}
