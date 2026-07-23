package com.bank.recommendationService.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "defaultDataSourceProperties")
   @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource(
            @Qualifier("defaultDataSourceProperties")
            DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "knowledgeDataSourceProperties")
    @ConfigurationProperties(prefix = "knowledge.datasource")
    public DataSourceProperties knowledgeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "knowledgeDataSource")
    public DataSource knowledgeDataSource(
            @Qualifier("knowledgeDataSourceProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "knowledgeJdbcTemplate")
    public JdbcTemplate knowledgeJdbcTemplate(
            @Qualifier("knowledgeDataSource")
            DataSource knowledgeDataSource
    ) {
        return new JdbcTemplate(knowledgeDataSource);
    }
}
