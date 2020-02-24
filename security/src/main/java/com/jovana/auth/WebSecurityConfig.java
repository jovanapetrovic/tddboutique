package com.jovana.auth;

import com.jovana.entity.authority.AuthoritiesConstants;
import com.jovana.token.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.sql.DataSource;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERS_BY_USERNAME_QUERY = "SELECT p.email, u.password, 1 " +
                                                          "FROM user u " +
                                                          "JOIN profile p ON u.id = p.user_id " +
                                                          "WHERE email=?";
    private static final String AUTHORITIES_BY_USERNAME_QUERY = "SELECT p.email, u2a.authority_name " +
                                                                "FROM profile p " +
                                                                "JOIN user u ON p.user_id = u.id " +
                                                                "JOIN user_authority u2a ON u.id = u2a.user_id " +
                                                                "WHERE email=?";

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        return new TokenAuthenticationFilter();
    }

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
            .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
            .and()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)   // Add custom filter to the Spring security chain.
                .authorizeRequests()
                .antMatchers("/api/testpost").permitAll()
                .antMatchers("/api/testget").permitAll()
                .antMatchers("/api/register/**").permitAll()
                .antMatchers("/api/activate/**").permitAll()
                .antMatchers("/api/suprise/me").permitAll()
                .antMatchers("/api/account/undo/finish").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/subscription/brands/webhook").permitAll()
                .antMatchers("/api/conversation/saveMessage").permitAll()
                .antMatchers("/api/account/reset_password/init").permitAll()
                .antMatchers("/api/account/reset_password/finish").permitAll()
                .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/invite/template").permitAll()
                .antMatchers("/api/**").authenticated()
            .and()
                .formLogin().loginPage("/api/login")    // Pre-set login form with default params
                .usernameParameter("email").passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            .and()
                .logout().logoutUrl("/logout")
                .permitAll();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/scripts/**/*.{js,html}")
                .antMatchers("/bower_components/**")
                .antMatchers("/i18n/**")
                .antMatchers("/assets/**")
                .antMatchers("/test/**")
                .antMatchers("/console/**");
    }

    /**
     * Login form expects to read the following tables from db: a User which has username and password and a Role with role name.
     * Override the default behavior here.
     * Everything else is automated in authenticationSuccessHandler and doFilterInternal.
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder())
                .usersByUsernameQuery(USERS_BY_USERNAME_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
    }

}
