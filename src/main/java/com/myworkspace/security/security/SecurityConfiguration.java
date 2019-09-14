package com.myworkspace.security.security;

import com.myworkspace.security.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //In-memory authentication -
		/*auth.
                inMemoryAuthentication()

		.withUser("admin").password(passwordEncoder().encode("password"))
		.authorities("ACCESS_TEST1","ACCESS_TEST2","ROLE_ADMIN")

                .and()

        .withUser("priyank").password(passwordEncoder().encode("joshi"))
		.roles("USER")

                .and()

        .withUser("manager").password(passwordEncoder().encode("manager123"))
		.authorities("ACCESS_TEST1","ACCESS_TEST2","ROLE_MANAGER");*/

        //Using the DaoAuthentication Provider and Custom UserPrincipal Service -
        auth.authenticationProvider(autheticationProvider());

    }

    //Key tool command to generate the Certificate -Self Signed
    //keytool.exe -genkey -alias myappkey -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore myappkey.p12 -validity 3650
    @Override
    protected void configure(HttpSecurity http) throws Exception {
		/*http
		.authorizeRequests()
		.antMatchers("/index.html").permitAll()
		.antMatchers("/profile/**").authenticated()
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/management/**").hasAnyRole("ADMIN","MANAGER")
        .antMatchers("/api/public/test1").hasRole("ADMIN")
        .antMatchers("/api/public/users").hasRole("ADMIN")
        .and()
		.httpBasic();*/
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))//add Jwt filters
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/public/users").hasRole("ADMIN");
        //No security applied
        //http.authorizeRequests().antMatchers("*").permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider autheticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userPrincipalDetailsService);
        return authenticationProvider;
    }
}
