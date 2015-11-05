package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.model.Seller;
import hu.inbuss.vehicleshop.repository.SellerRepository;
import static hu.inbuss.vehicleshop.util.Relatives.SIGN_UP;
import static hu.inbuss.vehicleshop.util.Relatives.SIGN_UP_FORM;
import hu.inbuss.vehicleshop.util.Views;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class SignUpControllerTest extends TestClass {

    @Autowired
    private SellerRepository sellerRepo;

    @Test
    public void testShowSignUpPage() throws Exception {
        final ModelAndView mav = mockMVC.perform(get(SIGN_UP_FORM)).andReturn().getModelAndView();
        final String signUpView = (String) mav.getModelMap().get("view");

        Assert.assertTrue(Views.SIGN_UP.equals(signUpView));
    }

    @Test
    public void testSellerRegistration() throws Exception {
        final String username = "testuser5";
        mockMVC.perform(post(SIGN_UP)
                .param("username", username)
                .param("password", "testuser"));
        final Seller seller = sellerRepo.findByUsername(username);
        Assert.assertNotNull(seller);
    }

    @Test
    public void testAlreadyUsedUserRegistration() throws Exception {
        final String username = "testuser5";

        mockMVC.perform(post(SIGN_UP)
                .param("username", username)
                .param("password", "testuser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));
    }
}
