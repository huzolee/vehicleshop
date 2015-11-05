package hu.inbuss.vehicleshop.util;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class Views {

    private static final String VEHICLE_LOCATION = "vehicle/";
    private static final String CUSTOMER_LOCATION = "customer/";

    public static final String CUSTOMER_LIST = CUSTOMER_LOCATION + "customer_list";
    public static final String REDIRECT_TO_CUSTOMER_LIST = "redirect:/customer/list";
    public static final String CUSTOMER_CREATOR = CUSTOMER_LOCATION + "customer_creator";
    public static final String CUSTOMER_DETAILED_LIST = CUSTOMER_LOCATION + "detailed_list";

    public static final String VEHICLE_LIST = VEHICLE_LOCATION + "vehicle_list";
    public static final String REDIRECT_TO_VEHICLE_LIST = "redirect:/vehicle/list";
    public static final String VEHICLE_SELLER = VEHICLE_LOCATION + "vehicle_seller";
    public static final String VEHICLE_CREATOR = VEHICLE_LOCATION + "vehicle_creator";

    public static final String HOME = "home";
    public static final String SIGN_IN = "signin/signin";
    public static final String SIGN_UP = "signup/signup";
    public static final String ERROR = "error/error";
}
