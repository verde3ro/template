package mx.gob.queretaro;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {
				"mx.gob.queretaro.repositories.repository"
		}
)
public class JpaConfiguration {

	@Bean(name ="dataSourceProperties")
	@Primary
	@Profile("dev")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "dataSource")
	@Primary
	@Profile("dev")
	public DataSource dataSource() {
		return dataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean(name ="dataSourceProperties")
	@Primary
	@Profile("prod")
	@ConfigurationProperties(prefix = "spring.datasource.jndi")
	public DataSourceProperties dataSourcePropertiesJndi() {
		return new DataSourceProperties();
	}

	@Bean(name = "dataSource")
	@Primary
	@Profile("prod")
	public DataSource dataSourceJndi() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

		return dataSourceLookup.getDataSource(dataSourcePropertiesJndi().getJndiName());
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages("mx.gob.queretaro.repositories.model")
				.persistenceUnit("template")
				.build();
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
