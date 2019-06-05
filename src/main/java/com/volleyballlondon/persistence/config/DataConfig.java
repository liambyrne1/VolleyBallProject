package com.volleyballlondon.persistence.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.volleyballlondon.persistence.repository")
@PropertySource("classpath:application.properties")
public class DataConfig {

	private static final String PROPERTY_DRIVER = "dbDriver";
	private static final String PROPERTY_HOSTNAME = "dbHostname";
	private static final String PROPERTY_PORT = "dbPort";
	private static final String PROPERTY_USERNAME = "dbUsername";
	private static final String PROPERTY_PASSWORD = "dbPassword";

	private static final String PROPERTY_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	private static final String PROPERTY_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_FORMAT_SQL = "hibernate.format_sql";

	@Autowired
	Environment environment;

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lfb = new LocalContainerEntityManagerFactoryBean();
		lfb.setDataSource(dataSource());
		lfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		lfb.setPackagesToScan("com.volleyballlondon.persistence.model");
		lfb.setJpaProperties(hibernateProps());
		return lfb;
	}

	@Bean
	DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
        String hostName = environment.getProperty(PROPERTY_HOSTNAME);
        String port = environment.getProperty(PROPERTY_PORT);
        String url = "jdbc:postgresql://" + hostName + ":" + port + "/volleyball";
		ds.setUrl(url);
		ds.setUsername(environment.getProperty(PROPERTY_USERNAME));
		ds.setPassword(environment.getProperty(PROPERTY_PASSWORD));
		ds.setDriverClassName(environment.getProperty(PROPERTY_DRIVER));
		return ds;
	}

	Properties hibernateProps() {
		Properties properties = new Properties();
		properties.setProperty(PROPERTY_DIALECT,
            environment.getProperty(PROPERTY_DIALECT));
		properties.setProperty(PROPERTY_HBM2DDL_AUTO,
            environment.getProperty(PROPERTY_HBM2DDL_AUTO));
		properties.setProperty(PROPERTY_SHOW_SQL,
            environment.getProperty(PROPERTY_SHOW_SQL));
		properties.setProperty(PROPERTY_FORMAT_SQL,
            environment.getProperty(PROPERTY_FORMAT_SQL));
		return properties;
	}

	@Bean
	JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
}
