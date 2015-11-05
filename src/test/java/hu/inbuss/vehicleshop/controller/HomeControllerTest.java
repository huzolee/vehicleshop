package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Seller;
import hu.inbuss.vehicleshop.service.UserService;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Roles;
import hu.inbuss.vehicleshop.util.Util;
import hu.inbuss.vehicleshop.util.Views;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class HomeControllerTest extends TestClass {

    @Autowired
    private UserService userService;

    @Test
    public void testShowHomePage() throws Exception {
        final String username = "testuser1";
        userService.saveSeller(new Seller(username, "testuser", Roles.SELLER));

        final ResultMatcher matcher = new ResultMatcher() {
            @Override
            public void match(final MvcResult mvcResult) throws Exception {
                final SecurityContext securityContext = Util.getSecurityContext(mvcResult);
                final Authentication authToken = securityContext.getAuthentication();
                final MockHttpSession session = new MockHttpSession();
                securityContext.setAuthentication(authToken);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, Util.getSecurityContext(mvcResult));
                final MockHttpServletRequestBuilder rb = MockMvcRequestBuilders.get(Relatives.HOME).session(session);
                final ModelAndView mav = mockMVC.perform(rb).andReturn().getModelAndView();
                final String homeView = (String) mav.getModelMap().get("view");

                Assert.assertTrue(Views.HOME.equals(homeView));
            }
        };
        mockMVC.perform(post("/authenticate")
                .param("username", username)
                .param("password", "testuser"))
                .andExpect(redirectedUrl(Relatives.HOME))
                .andExpect(matcher);
    }
}
