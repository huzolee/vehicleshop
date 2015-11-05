package hu.inbuss.vehicleshop.listener;

import hu.inbuss.vehicleshop.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {
        final WebAuthenticationDetails auth = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        loginAttemptService.loginSucceeded(auth.getRemoteAddress());
    }
}
