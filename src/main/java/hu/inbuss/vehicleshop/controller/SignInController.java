package hu.inbuss.vehicleshop.controller;

import hu.inbuss.vehicleshop.exception.CustomException;
import hu.inbuss.vehicleshop.service.LoginAttemptService;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Views;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Controller
public class SignInController {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @RequestMapping(value = Relatives.SIGN_IN)
    public String signin(final HttpServletRequest request) {
        if (loginAttemptService.isBlocked(request.getRemoteAddr())) {
            throw new CustomException("blocked ip= " + request.getRemoteAddr());
        }

        return Views.SIGN_IN;
    }
}
