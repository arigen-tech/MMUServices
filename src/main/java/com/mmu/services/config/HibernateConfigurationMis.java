package com.mmu.services.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
public class HibernateConfigurationMis {

	@Autowired
	private Environment environment;

	@Bean()
	public LocalSessionFactoryBean sessionFactory2() {
		LocalSessionFactoryBean sessionFactory2 = new LocalSessionFactoryBean();
		sessionFactory2.setDataSource(dataSource2());
		sessionFactory2.setPackagesToScan(new String[] { "com.mmu.services.entity" });
		sessionFactory2.setHibernateProperties(hibernateProperties());
		return sessionFactory2;
	}
	

	@Bean
	public DataSource dataSource2() {
		System.out.println("Secondary DataSource Called");
		DriverManagerDataSource dataSource2 = new DriverManagerDataSource();
		dataSource2.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource2.setUrl(environment.getRequiredProperty("jdbc.mis.url"));
		dataSource2.setUsername(environment.getRequiredProperty("jdbc.mis.username"));
		dataSource2.setPassword(environment.getRequiredProperty("jdbc.mis.password"));

		return dataSource2;

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
	@Qualifier("transactionManager2")
	public HibernateTransactionManager transactionManager2(@Qualifier("sessionFactory2") SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

}
