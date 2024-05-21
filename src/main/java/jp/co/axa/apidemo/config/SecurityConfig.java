package jp.co.axa.apidemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Set the security configuration to controller the access authority of 
 * particular resource.
 * 
 * @author natsubunrai
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(password());
	}
	
	/**
	 * define password encoder
	 * @return PasswordEncoder
	 */
	@Bean
	PasswordEncoder password() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * set the configuration
	 * 
	 * @param http 
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.logout().logoutUrl("/api/v1/logout").logoutSuccessUrl("/login.html").permitAll();
		http.exceptionHandling().accessDeniedPage("/unauth.html"); //redirect to unauth.html page when login by people without adequate authority  
		http.authorizeRequests()
		.antMatchers("/h2-console/**","/css/**").permitAll()
		.antMatchers("/swagger-ui.html/**").hasAnyRole("HR,Director") //only the people whose department is HR or "Director" can access swagger-ui.html page 
		.anyRequest().authenticated()
		.and().formLogin()
			  .loginPage("/login.html")
		      .loginProcessingUrl("/user/login")
			  .defaultSuccessUrl("/swagger-ui.html").permitAll()
	    .and().headers().frameOptions().disable(); //disable X-Frame-Options
		http.csrf().disable();
		
	}
}
