package com.jeegox.glio.config.spring;

import java.util.Properties;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.hsqldb.jdbc.JDBCDataSource;

/**
 *
 * @author j2esus
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.jeegox.glio.*")
public class ApplicationContextConfigTest {

    //jdni
    @Bean
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[]{"com.jeegox.glio.entities"});
        sessionFactory.setHibernateProperties(additionalProperties());
        return sessionFactory;
    }

    @Bean(destroyMethod = "")
    public DataSource dataSource() throws NamingException {
        
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:glio;shutdown=true;sql.syntax_mys=true;sql.ignore_case=true");
        dataSource.setUser("SA");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        hibernateProperties.setProperty("hibernate.connection.shutdown", "true");
        return hibernateProperties;
    }
}
