package mx.gob.queretaro;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {
				"mx.gob.queretaro.repositories.repository"
		}
)
public class JpaConfiguration {

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages("mx.gob.queretaro.repositories.model")
				.persistenceUnit("template")
				.build();
	}

	@Primary
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory customEntityManagerFactory) {
		return new JpaTransactionManager(customEntityManagerFactory);
	}

}
