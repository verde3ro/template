package mx.gob.queretaro;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

@Configuration
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
public class MultipartResolverConfiguration {

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

		multipartResolver.setMaxInMemorySize(5242880); // Maximo de memoria 5M
		multipartResolver.setMaxUploadSize(10L * 1024L * 1024L); // 10M 10*1024*1024
		multipartResolver.setMaxUploadSizePerFile(5L * 1024L * 1024L); // 5M 5*1024*1024
		multipartResolver.setDefaultEncoding("UTF-8");
		multipartResolver.setResolveLazily(false);

		return multipartResolver;
	}

	@Bean
	public MultipartFilter multipartFilter() {
		MultipartFilter multipartFilter = new MultipartFilter();
		multipartFilter.setMultipartResolverBeanName("multipartResolver");

		return multipartFilter;
	}

}
