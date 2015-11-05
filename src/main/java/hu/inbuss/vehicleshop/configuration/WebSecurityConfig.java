package hu.inbuss.vehicleshop.configuration;

import hu.inbuss.vehicleshop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int MAXIMUM_SESSION_COUNT = 1;
    private static final String ENCODING = "UTF-8";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGIN_PAGE_URL = "/signin";
    private static final String FAILURE_URL = "/signin?error=1";
    private static final String REMEMBER_ME_KEY = "remember-me-key";
    private static final String LOGOUT_SUCCESS_URL = "/signin?logout";
    private static final String LOGIN_PROCESSING_URL = "/authenticate";

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userService());
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .eraseCredentials(true)
                .userDetailsService(userService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CharacterEncodingFilter getCharacterEncodingFilter() {
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(ENCODING);
        filter.setForceEncoding(true);
        return filter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/signup_form", "/signup").permitAll()
                //.antMatchers("/vehicle/list").hasIpAddress("127.0.0.1")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_PAGE_URL)
                .permitAll()
                .failureUrl(FAILURE_URL)
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .and()
                .logout()
                .logoutUrl(LOGOUT_URL)
                .permitAll()
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                .and()
                .csrf()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices()).key(REMEMBER_ME_KEY)
                .and()
                .addFilterBefore(getCharacterEncodingFilter(), CsrfFilter.class)
                .sessionManagement().maximumSessions(MAXIMUM_SESSION_COUNT).maxSessionsPreventsLogin(true);
    }
}
