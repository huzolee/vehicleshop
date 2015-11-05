package hu.inbuss.vehicleshop.advice;

import hu.inbuss.vehicleshop.exception.CustomException;
import hu.inbuss.vehicleshop.exception.CustomerEMailException;
import hu.inbuss.vehicleshop.exception.SignUpException;
import hu.inbuss.vehicleshop.util.Relatives;
import hu.inbuss.vehicleshop.util.Views;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ModelAndView customExceptionHandler(final CustomException ex) {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex.getMessage());
        mav.setViewName(Views.ERROR);
        return mav;
    }

    @ExceptionHandler(SignUpException.class)
    public ModelAndView signUpExceptionHandler() {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("error", 1);
        mav.setViewName("redirect:" + Relatives.SIGN_UP_FORM);
        return mav;
    }

    @ExceptionHandler(CustomerEMailException.class)
    public ModelAndView CustomerEMailExceptionHandler() {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("error", 1);
        mav.setViewName("redirect:" + Relatives.CUSTOMER + Relatives.CUSTOMER_CREATOR_FORM);
        return mav;
    }
}
