package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.exception.SignUpException;
import hu.inbuss.vehicleshop.model.Seller;
import hu.inbuss.vehicleshop.repository.SellerRepository;
import hu.inbuss.vehicleshop.service.UserService;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Views;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Controller
public class SignUpController {

    @Autowired
    private UserService userService;
    @Autowired
    private SellerRepository sellerRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);

    @RequestMapping(value = Relatives.SIGN_UP_FORM, method = RequestMethod.GET)
    public String showSignUpForm(final Model model) {
        LOGGER.info("showSignUpForm()");
        model.addAttribute(new Seller());
        return Views.SIGN_UP;
    }

    @RequestMapping(value = Relatives.SIGN_UP, method = RequestMethod.POST)
    public String signUp(@Valid final Seller seller, final Errors errors) {
        LOGGER.info("signUp()");

        if (errors.hasErrors()) {
            LOGGER.info("signUp(): Count of field's errors: {}", errors.getErrorCount());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("signUp(): {}", errors);
            }
            return Views.SIGN_UP;
        }

        if (sellerRepo.findByUsername(seller.getUsername()) != null) {
            LOGGER.info("signUp(): username is already used");
            throw new SignUpException("username is already used");
        }

        userService.saveSeller(seller);

        return Views.SIGN_IN;
    }
}
