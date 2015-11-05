package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Seller;
import hu.inbuss.vehicleshop.service.LoginAttemptService;
import hu.inbuss.vehicleshop.service.UserService;
import hu.inbuss.vehicleshop.util.Relatives;
import static hu.inbuss.vehicleshop.util.Relatives.SIGN_IN;
import static hu.inbuss.vehicleshop.util.Relatives.SIGN_IN_ERROR;
import hu.inbuss.vehicleshop.util.Roles;
import hu.inbuss.vehicleshop.util.Util;
import hu.inbuss.vehicleshop.util.Views;
import javax.servlet.ServletContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class SignInControllerTest extends TestClass {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Test
    public void testShowSignInPage() throws Exception {
        final ModelAndView mav = mockMVC.perform(get(SIGN_IN)).andReturn().getModelAndView();
        final String signInView = (String) mav.getModelMap().get("view");

        Assert.assertTrue(Views.SIGN_IN.equals(signInView));
    }

    @Test
    public void testRedirectToSignInPage() throws Exception {
        final MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.get("/");
        final String host = reqBuilder.buildRequest(servletContext).getLocalName();
        final String protocol = reqBuilder.buildRequest(servletContext).getProtocol();
        mockMVC.perform(reqBuilder)
                .andExpect(redirectedUrl(protocol + "://" + host + SIGN_IN));
    }

    @Test
    public void testSignIn() throws Exception {
        final String username = "signInUser";
        userService.saveSeller(new Seller(username, "testuser", Roles.SELLER));

        final ResultMatcher matcher = new ResultMatcher() {
            @Override
            public void match(final MvcResult mvcResult) throws Exception {
                final SecurityContext securityContext = Util.getSecurityContext(mvcResult);
                Assert.assertEquals(username, securityContext.getAuthentication().getName());
            }
        };

        mockMVC.perform(post("/authenticate")
                .param("username", username)
                .param("password", "testuser"))
                .andExpect(redirectedUrl(Relatives.HOME))
                .andExpect(matcher);
    }

    @Test
    public void testSignInWithBadPassword() throws Exception {
        final String username = "signInUser2";
        userService.saveSeller(new Seller(username, "test2", Roles.SELLER));

        final ResultMatcher matcher = new ResultMatcher() {
            @Override
            public void match(final MvcResult mvcResult) throws Exception {
                final SecurityContext securityContext = Util.getSecurityContext(mvcResult);
                Assert.assertEquals(null, securityContext);
            }
        };

        mockMVC.perform(post("/authenticate")
                .param("username", username)
                .param("password", "test"))
                .andExpect(redirectedUrl(SIGN_IN_ERROR))
                .andExpect(matcher);
    }

    @Test
    public void testUserBan() throws Exception {
        final String username = "signInUser3";
        userService.saveSeller(new Seller(username, "test3", Roles.SELLER));

        final ResultMatcher matcher = new ResultMatcher() {
            @Override
            public void match(final MvcResult mvcResult) throws Exception {
                final SecurityContext securityContext = Util.getSecurityContext(mvcResult);
                Assert.assertTrue((securityContext == null)
                        && (loginAttemptService.isBlocked(mvcResult.getRequest().getRemoteAddr())));
                loginAttemptService.loginSucceeded(mvcResult.getRequest().getRemoteAddr());
            }
        };

        ResultActions ras = null;

        for (int i = 0; i <= LoginAttemptService.MAX_ATTEMPT; i++) {
            ras = mockMVC.perform(post("/authenticate")
                    .param("username", username)
                    .param("password", "test"));
        }

        ras.andExpect(matcher);
    }
}
