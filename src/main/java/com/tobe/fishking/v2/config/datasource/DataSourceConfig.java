package com.tobe.fishking.v2.config.datasource;

import com.tobe.fishking.v2.repository.BaseRepositoryImpl;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.tobe.fishking.v2.repository"},
        repositoryBaseClass = BaseRepositoryImpl.class,
        entityManagerFactoryRef = "entityFactoryBean",
        transactionManagerRef = "transactionManager"
)
public class DataSourceConfig {
    private final DataSourceProperty dataSourceProperty;

    @Autowired
    DataSourceConfig(DataSourceProperty dataSourceProperty)
    {
        this.dataSourceProperty = dataSourceProperty;
    }

    private DataSource createDataSource(DataSourceProperty dataSourceProperty, String dbUser, String dbPassword)
    {
        HikariDataSource hikariDataSource = (HikariDataSource) DataSourceBuilder.create()
                .driverClassName(dataSourceProperty.getDriverClassName())
                .url(dataSourceProperty.getJdbcUrl())
                .username(dbUser)
                .password(dbPassword)
                .build();

        log.info(String.format("DB Setting // username: %s", dbUser));
        hikariDataSource.setMaximumPoolSize(dataSourceProperty.getMaximumPoolSize());
        return hikariDataSource;
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter)
    {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, new HashMap<>(), null);
    }

    @Bean
    public DataSource initDataSource()
    {
        DataSource dataSource
                = createDataSource(dataSourceProperty, dataSourceProperty.getUsername(), dataSourceProperty.getPassword());

        return dataSource;
    }

    @Primary
    @Bean(name = "entityFactoryBean")
    public LocalContainerEntityManagerFactoryBean getFactoryBean(
            DataSource dataSource,
            EntityManagerFactoryBuilder builder)
    {
        Map<String, Object> jpaProperties = new HashMap<>();

        jpaProperties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        jpaProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());


        return builder.dataSource(dataSource)
                .packages("com.tobe.fishking.v2")
                .properties(jpaProperties)
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    PlatformTransactionManager getTransactionManager(
            LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) throws RuntimeException
    {
        EntityManagerFactory emf = entityManagerFactoryBean.getObject();
        if (emf == null)
            throw new RuntimeException("EntityManagerFactory is null");

        return new JpaTransactionManager(emf);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter()
    {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("com.tobe.fishking.v2.config.datasource.CustomMysqlDialect");
        adapter.setGenerateDdl(false);
        return adapter;
    }
}
