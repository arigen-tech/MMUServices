package com.mmu.services.config;

import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.mmu.services.config" })
@PropertySource("classpath:application-prod.properties")
//@PropertySource("classpath:application-testing.properties")
@EnableWebMvc
public class HibernateConfiguration {

	@Autowired
	private Environment environment;

	@Bean()
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.mmu.services.entity" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}
	
	

	@Bean
	public DataSource dataSource() {
		System.out.println("Main DataSource Called");

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

		return dataSource;

	}
	
	private Properties hibernateProperties() {
		
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect").trim());
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql").trim());
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql").trim());
		properties.put("hibernate.autoReconnect", environment.getRequiredProperty("hibernate.autoReconnect").trim());
		properties.put("hibernate.c3p0.min_size", environment.getProperty("hibernate.c3p0.min_size").trim());
		properties.put("hibernate.c3p0.max_size", environment.getProperty("hibernate.c3p0.max_size").trim());
		properties.put("hibernate.c3p0.timeout", environment.getProperty("hibernate.c3p0.timeout").trim());
		properties.put("hibernate.c3p0.max_statements",
				environment.getProperty("hibernate.c3p0.max_statements").trim());
		properties.put("hibernate.c3p0.idle_test_period",
				environment.getProperty("hibernate.c3p0.idle_test_period").trim());
		properties.put("hibernate.c3p0.acquire_increment",
				environment.getProperty("hibernate.c3p0.acquire_increment").trim());
		properties.put("hibernate.c3p0.acquireRetryAttempts",
				environment.getProperty("hibernate.c3p0.acquireRetryAttempts").trim());
		properties.put("hibernate.c3p0.acquireRetryDelay",
				environment.getProperty("hibernate.c3p0.acquireRetryDelay").trim());
		properties.put("hibernate.enable_lazy_load_no_trans",
				environment.getProperty("hibernate.enable_lazy_load_no_trans").trim());

		return properties;
	}

	@Bean
	@Autowired
	@Primary
	public HibernateTransactionManager transactionManager(@Qualifier("sessionFactory") SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

}
