package es.uco.tfg.elderBridge.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.uco.tfg.elderBridge.infrastructure.security.JwtAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private final AuthenticationProvider authProvider;
	private final JwtAuthenticationFilter authenticationFilter;
	
	public SecurityConfiguration(AuthenticationProvider authProvider, JwtAuthenticationFilter authenticationFilter) {
		super();
		this.authProvider = authProvider;
		this.authenticationFilter = authenticationFilter;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http.
				cors(Customizer.withDefaults()).
				csrf(csrf->csrf.disable())
				.authorizeHttpRequests(authRequest ->
		authRequest.requestMatchers("/services/user/auth/**").permitAll()
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.anyRequest().authenticated())
		.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authProvider)
		.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
		.build();
	}
}
