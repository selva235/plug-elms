//package com.example.controller;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//public class DatabaseConfig {
//
//	@Value("${db.driver}")
//	private String dbDriver;
//
//	@Value("${db.url}")
//	private String dbUrl;
//
//	@Value("${db.username}")
//	private String dbUserName;
//
//	@Value("${db.password}")
//	private String dbPassword;
//
//	@Value("${hibernate.dialect}")
//	private String hibernateDialect;
//	@Value("${hibernate.show_sql}")
//	private String hibernateShowSql;
//	@Value("${entitymanager.packagesToScan}")
//	private String entitymanagerPackagesToScan;
//
//	@Bean
//	@Qualifier("mysqlSource")
//	public DataSource dataSource() throws Exception {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(dbDriver);
//		dataSource.setUrl(dbUrl);
//		dataSource.setUsername(dbUserName);
//		dataSource.setPassword(dbPassword);
//
//		System.setProperty("spring.jpa.properties.hibernate.connection.url", dbUrl);
//		System.setProperty("spring.jpa.properties.hibernate.connection.username", dbUserName);
//		System.setProperty("spring.jpa.properties.hibernate.connection.password", dbPassword);
//		return dataSource;
//	}
//
//	@Bean
//	public LocalSessionFactoryBean sessionFactory() throws Exception {
//		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//		sessionFactoryBean.setDataSource(dataSource());
//		sessionFactoryBean.setPackagesToScan(entitymanagerPackagesToScan);
//
//		Properties hibernateProperties = new Properties();
//		hibernateProperties.put("hibernate.dialect", hibernateDialect);
//		hibernateProperties.put("hibernate.show_sql", hibernateShowSql);
//
//		sessionFactoryBean.setHibernateProperties(hibernateProperties);
//		return sessionFactoryBean;
//	}
//
//	@Bean
//	public HibernateTransactionManager transactionManager() throws Exception {
//		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//		transactionManager.setSessionFactory(sessionFactory().getObject());
//		transactionManager.setAutodetectDataSource(false);
//		return transactionManager;
//	}
//	
//}
