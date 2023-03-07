package academy.devdojo.springboot2.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import academy.devdojo.springboot2.service.AnimeUserService;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
//@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	private final AnimeUserService animeUserService;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		    .csrf().disable()
			//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
			.authorizeHttpRequests()
			.antMatchers("/animes/admin/**").hasRole("ADMIN")
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.and()
			.httpBasic();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		log.info("password encoder: {}", passwordEncoder.encode("user"));
		
		auth.inMemoryAuthentication()
			.withUser("admin")
			.password(passwordEncoder.encode("admin"))
			.roles("USER","ADMIN")
			.and()
			.withUser("user")
			.password(passwordEncoder.encode("user"))
			.roles("USER");
		auth.userDetailsService(animeUserService).passwordEncoder(passwordEncoder);
	}
}
