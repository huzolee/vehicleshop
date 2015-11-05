package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.configuration.JPAConfig;
import hu.inbuss.vehicleshop.testconfig.NoCsrfSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfig.class, NoCsrfSecurityConfig.class,
    hu.inbuss.vehicleshop.configuration.WebAppConfiguration.class})
@WebAppConfiguration
public class TestClass {

    protected MockMvc mockMVC;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FilterChainProxy springSecurityFilter;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilter)
                .build();
    }

    @Test
    public void testClass() {
    }
}
