package hu.inbuss.vehicleshop.testconfig;

import hu.inbuss.vehicleshop.configuration.WebSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Configuration
@Order(1)
public class NoCsrfSecurityConfig extends WebSecurityConfig {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
    }
}
