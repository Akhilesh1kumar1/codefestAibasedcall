package com.sr.capital.config.db;

import com.sr.capital.config.AppProperties;
import com.sr.capital.config.AttributeEncryptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.boot.spi.MetadataContributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = {"com.sr.capital.repository.primary","com.omunify.kafka.*"}
)
public class PrimaryDatabaseConfig {

    @Autowired
    private AppProperties appProperties;


    @Bean(name = "primaryDataSource")
    //@ConfigurationProperties(prefix = "spring.datasource.primary")
    public HikariDataSource primaryDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(appProperties.getJdbcUrlPrimary());
        config.setUsername(appProperties.getUsernamePrimary());
        config.setPassword(appProperties.getPasswordPrimary());
        config.setMaximumPoolSize(appProperties.getMaxPoolSize());
        config.setMinimumIdle(appProperties.getMinIdle());
        config.setPoolName(appProperties.getPoolName());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return new HikariDataSource(config);
    }

    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            @Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[]{"com.sr.capital.entity.primary","com.omunify.kafka.*"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Register the AttributeEncryptor
        // Apply AttributeEncryptor using MetadataBuilderContributor
        // Register AttributeEncryptor as a TypeContributor
        //em.getJpaPropertyMap().put("hibernate.type_contributors", Collections.singletonList(attributeEncryptor));
       // em.getJpaPropertyMap().put("hibernate.type_contributors",attributeEncryptor);


        return em;
    }

    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
