package com.example.demo1.security;


import com.example.demo1.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;
import static com.example.demo1.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;



    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService) {

        this.passwordEncoder = passwordEncoder;
        this.applicationUserService=applicationUserService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasAnyRole(STUDENT.name())
//                .antMatchers(HttpMethod.DELETE, "/managment/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/managment/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/managment/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET, "/managment/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/courses", true)
                .passwordParameter("password")
                .usernameParameter("username")

                .and()
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))// defoult to 2 weeks
                .key("somethingverysecured")
                .rememberMeParameter("remember-me")

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "get"))

                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");

    }


//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails annaSmithUser = User.builder()
//                .username("annasmith")
//                .password(passwordEncoder.encode("password"))
//                //.roles(STUDENT.name()) //role STUDNET
//                .authorities(STUDENT.getGrantedAuthorities())
//                .build();
//
//
//        UserDetails lindaUser = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("password123"))
//                // .roles(ADMIN.name())  //role ADMIN
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tomUser = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password123"))
//                //.roles(ADMINTRAINEE.name())// role admin trainee
//                .authorities(ADMINTRAINEE.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(
//                annaSmithUser,
//                lindaUser,
//                tomUser
//        );



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());


    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);

        return provider;

    }
}
