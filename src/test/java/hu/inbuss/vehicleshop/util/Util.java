package hu.inbuss.vehicleshop.util;

import hu.inbuss.vehicleshop.model.Seller;
import hu.inbuss.vehicleshop.service.UserService;
import javax.servlet.http.HttpSession;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class Util {

    public static SecurityContext getSecurityContext(final MvcResult mvcResult) {
        final HttpSession session = mvcResult.getRequest().getSession();
        final SecurityContext securityContext
                = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        return securityContext;
    }

    public static MockHttpSession createSessionForSeller(final String username, final String password,
            final String sellerRole, final UserService userService, final MockMvc mockMVC) throws Exception {
        userService.saveSeller(new Seller(username, password, sellerRole));

        final MockHttpSession session = new MockHttpSession();
        final ResultMatcher matcher = new ResultMatcher() {
            @Override
            public void match(final MvcResult mvcResult) {
                final SecurityContext securityContext = Util.getSecurityContext(mvcResult);
                final Authentication authToken = securityContext.getAuthentication();
                securityContext.setAuthentication(authToken);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, Util.getSecurityContext(mvcResult));
            }
        };

        mockMVC.perform(post("/authenticate").param("username", username).param("password", password))
                .andExpect(redirectedUrl("/"))
                .andExpect(matcher);

        return session;
    }
}
