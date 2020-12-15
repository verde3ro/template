/*
 * COPYRIGHT © 2018. PODER EJECUTIVO DEL ESTADO DE QUERÉTARO. PATENT PENDING. ALL RIGHTS RESERVED.
 * ARCVyC IS REGISTERED TRADEMARKS OF PODER EJECUTIVO DEL ESTADO DE QUERÉTARO.
 *
 * This software is confidential and proprietary information of PODER EJECUTIVO DEL ESTADO DE
 * QUERÉTARO. You shall not disclose such Confidential Information and shall use it only in
 * accordance with the company policy.
 */
package mx.gob.queretaro;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.filter.CsrfHeaderFilter;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRecurso;
import mx.gob.queretaro.repositories.model.TdRecursoRol;
import mx.gob.queretaro.services.service.IParametroService;
import mx.gob.queretaro.services.service.IRecursoRolService;
import mx.gob.queretaro.services.service.IRecursoService;
import mx.gob.queretaro.services.util.Aes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Qualifier("ldapDetailsServiceImpl")
	private final LdapAuthoritiesPopulator ldapAuthoritiesPopulator;
	private final IRecursoRolService recursoRolService;
	private final IRecursoService recursoService;
	private final IParametroService parametrosService;

	@Autowired
	public WebSecurityConfiguration(LdapAuthoritiesPopulator ldapAuthoritiesPopulator, IRecursoRolService recursoRolService,
	                                IRecursoService recursoService, IParametroService parametrosService) {
		this.ldapAuthoritiesPopulator = ldapAuthoritiesPopulator;
		this.recursoRolService = recursoRolService;
		this.recursoService = recursoService;
		this.parametrosService = parametrosService;
	}

	@Value("${application.url-resources}")
	private String urlResources;

	private String ldapUrl;
	private String ldapUsuario;
	private String ldapPassword;
	private String ldapBase;

	@PostConstruct
	public void init() {
		try {
			this.ldapUrl = parametrosService.obtenerPorNombre("LDAP_URL").getValor().trim();
			this.ldapUsuario = "GEQ\\" + parametrosService.obtenerPorNombre("LDAP_USUARIO").getValor().trim();
			this.ldapPassword = Aes.desencriptar(parametrosService.obtenerPorNombre("LDAP_CONTRASENA").getValor().trim(), null);
			this.ldapBase = parametrosService.obtenerPorNombre("LDAP_BASE").getValor().trim();
		} catch (InternalException ex) {
			log.error("Ocurrió un error al inicializar los parámetros.", ex);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		List<TdRecursoRol> recursos = recursoRolService.obtenerRecursos();
		List<TcRecurso> recursosSinSesion = recursoService.obtenerRecursosSinSesion();

		http.formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/inicio").failureUrl("/index?error=true")
				.loginPage("/index").usernameParameter("txtUsuario").passwordParameter("txtPassword").permitAll().and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/index?reload=true").deleteCookies("JSESSIONID").invalidateHttpSession(true)
				.permitAll().and().authorizeRequests()
				.antMatchers("/", "/index", "/css/**", "/js/**", "/img/**", "/isLogin*").permitAll();

		// Autorización de recursos
		this.recusos(http, recursos);

		// Recursos sin autenticacion
		this.recusosSinSesion(http, recursosSinSesion);

		// Autorización a cualquier recurso
		http.authorizeRequests().anyRequest().authenticated();

		// CSRF Filter
		http.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);

		// Access Denied Page
		http.exceptionHandling().accessDeniedPage("/error");

		// Headers
		http.headers().httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000).and()
				.referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN).and()
				.contentSecurityPolicy("default-src * 'self' 'unsafe-inline' 'unsafe-eval' data: gap: content: blob:; " + "script-src 'self' 'unsafe-inline' "
						+ this.urlResources + "; "
						+ "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com "
						+ this.urlResources + "; " + "font-src 'self' https://fonts.gstatic.com "
						+ this.urlResources + " data:; " + "img-src 'self' blob: "
						+ this.urlResources + " data:;")
				.and().defaultsDisabled().contentTypeOptions().and().xssProtection().block(true).and().frameOptions()
				.sameOrigin();

		// Enabled CSRF
		http.csrf().csrfTokenRepository(csrfTokenRepository());
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userSearchFilter("(&(objectClass=user)(sAMAccountName={0}))")
				.contextSource(contextSource()).ldapAuthoritiesPopulator(ldapAuthoritiesPopulator).and()
				.eraseCredentials(false);
	}

	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapUrl + ldapBase);
		contextSource.setUserDn(ldapUsuario);
		contextSource.setPassword(ldapPassword);
		contextSource.afterPropertiesSet();

		return contextSource;

	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");

		return repository;
	}

	private void recusos(HttpSecurity http, List<TdRecursoRol> recursos) throws Exception {
		LinkedList<String> roles = new LinkedList<>();
		String pattern = "";

		if (!recursos.isEmpty()) {
			for (TdRecursoRol recurso : recursos) {
				if (!pattern.trim().equals(recurso.getTcRecurso().getRecurso().trim())) {
					roles = new LinkedList<>();

					if (recurso.getTcRol().getRol().contains("_")) {
						roles.add(recurso.getTcRol().getRol().trim().split("_", 2)[1]);
						pattern = recurso.getTcRecurso().getRecurso().trim();
					}
				} else {
					if (recurso.getTcRol().getRol().contains("_")) {
						roles.add(recurso.getTcRol().getRol().trim().split("_", 2)[1]);
					}
				}

				if (!pattern.trim().isEmpty()) {
					http.authorizeRequests().antMatchers(pattern.trim()).hasAnyRole(roles.toArray(new String[0]));
				}
			}
		}
	}

	private void recusosSinSesion(HttpSecurity http, List<TcRecurso> recursosSinSesion) throws Exception {
		if (!recursosSinSesion.isEmpty()) {
			for (TcRecurso recurso : recursosSinSesion) {
				http.authorizeRequests().antMatchers(recurso.getRecurso().trim()).permitAll();
			}
		}
	}
}
