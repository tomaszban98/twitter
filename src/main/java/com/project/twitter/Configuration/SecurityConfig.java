package com.project.twitter.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;


    @Override
    protected void configure(HttpSecurity http)throws Exception{
 /*       http.authorizeRequests()
                .antMatchers("/index","/users","/","/addPost","/user/**")
                .hasAnyAuthority("ROLE_USER")
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login-process")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/index")
                .and()
                .logout()
                .logoutSuccessUrl("/login");

*/
        http.authorizeRequests()
                .antMatchers("/index","/users","/","/addPost","/users/**","comment/**")
                .hasAnyRole("USER","ADMIN")
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login-process")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/index")
                .and()
                .logout()
                .logoutSuccessUrl("/login");



    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("test"))
                .roles("USER");

       /* auth.jdbcAuthentication()
                .usersByUsernameQuery("Select u.login, u.password, 1 From user u where u.login=?")
                .authoritiesByUsernameQuery("select u.login, 'ROLE_USER', 0 from user u where u.login=?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);*/
        auth.jdbcAuthentication()
                .usersByUsernameQuery("Select u.login, u.password, u.id From user u where u.login=?")
                .authoritiesByUsernameQuery("select u.login, u.role,u.id from user u where u.login=?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);

    }




}