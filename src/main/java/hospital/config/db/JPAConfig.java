package hospital.config.db;

import hospital.exeption.CustomSQLErrorCodeTranslator;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

@PropertySource(value = {
        "classpath:profilesDataSource.properties"
})
@Configuration
@EnableJpaRepositories(basePackages="hospital")
@EnableTransactionManagement
@Slf4j
public class JPAConfig {
    @Autowired
    private Environment env;

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        log.info("Start jdbcTemplate, dataSource = {}", dataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setExceptionTranslator(new CustomSQLErrorCodeTranslator());
        return jdbcTemplate;
    }

    @Bean
    @Profile("qa")
    public DataSource qaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.qa.driver"));
        dataSource.setUrl(env.getProperty("spring.datasource.qa.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.qa.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.qa.password"));
        log.info("Start QADataSource, dataSource = {}", dataSource);
        return dataSource;
    }


    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(env.getProperty("spring.datasource.dev.url"));
        dataSourceBuilder.username(env.getProperty("spring.datasource.dev.username"));
        dataSourceBuilder.password(env.getProperty("spring.datasource.dev.password"));
        log.info("Start DevDataSource, dataSource = {}", dataSourceBuilder);
        return dataSourceBuilder.build();
    }

    @Bean(destroyMethod = "")
    @Profile("prod")
    @Lazy
    public DataSource prodDataSource() throws NamingException, SQLException {
        log.info("Start prodDataSource");

        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/jdbc/postgres");
        log.info("getJndiName = {} " ,bean.getJndiName());
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.setResourceRef(true);
        bean.afterPropertiesSet();
        log.info(" prodDataSource, bean = {}", bean);

        DataSource ds = (DataSource) bean.getObject();
        log.info("End prodDataSource, ds = {}", ds.getConnection());

        return  ds;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("hospital");
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();

        return factory.getObject();
    }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

}