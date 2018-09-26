package com.chenshinan.spock.config;

import com.chenshinan.spock.liquibase.LiquibaseExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import spock.mock.DetachedMockFactory;

import javax.annotation.PostConstruct;

/**
 * @author shinan.chen
 * @date 2018/9/24
 */
@TestConfiguration
class IntegrationTestConfiguration {

    private final DetachedMockFactory detachedMockFactory = new DetachedMockFactory();

    @Autowired
    LiquibaseExecutor liquibaseExecutor;
    @Autowired
    Environment environment;

    @Bean
    LiquibaseExecutor getLiquibaseExecutor() {
        return new LiquibaseExecutor();
    }

    @Bean
    public TestRestTemplate restTemplate(ApplicationContext applicationContext) {
        TestRestTemplate rest = new TestRestTemplate();
        LocalHostUriTemplateHandler handler = new LocalHostUriTemplateHandler(
                environment, "http");
        rest.setUriTemplateHandler(handler);
        return rest;
    }

    @Autowired
    TestRestTemplate testRestTemplate;

    final ObjectMapper objectMapper = new ObjectMapper();

//    @Bean
//    KafkaTemplate kafkaTemplate() {
//        return detachedMockFactory.Mock(KafkaTemplate.class);
//    }

    @PostConstruct
    void init() {
        System.out.println("xaxaxa");
        try {
            liquibaseExecutor.execute();
        } catch (Exception e) {
            System.out.println("liquibaseExecutor Error");
        }
    }
}