package org.isf.utils.db;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="org.isf")
@ComponentScan(basePackages="org.isf")
public class SpringDataConfiguration {
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf, DataSource dataSource){
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	    transactionManager.setDataSource(dataSource);
	    return transactionManager;
	}
	
	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://localhost:3306/oh");
	    dataSource.setUsername("isf");
	    dataSource.setPassword("isf123");
	    return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
	    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	    em.setDataSource(dataSource);
	    em.setPackagesToScan("org.isf");
	    em.setJpaVendorAdapter(jpaVendorAdapter);
	    em.setJpaProperties(additionalProperties());
	    return em;
	}
	
	Properties additionalProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	    return properties;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
	    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
	    adapter.setShowSql(true);
	    adapter.setGenerateDdl(false);
	    adapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
	    return adapter;
	}
}