package hu.inbuss.vehicleshop.util;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class Relatives {

    public static final String CUSTOMER_BUY = "/buy";
    public static final String CUSTOMER = "/customer";
    public static final String CUSTOMER_LIST = "/list";
    public static final String CUSTOMER_CREATE = "/create";
    public static final String CUSTOMER_CREATOR_FORM = "/creator_form";
    public static final String CUSTOMER_DELETE = "/delete/{customerId}";
    public static final String CUSTOMER_MODIFY = "/modify/{customerId}";
    public static final String CUSTOMER_DETAILS = "/details/{customerId}";

    public static final String VEHICLE = "/vehicle";
    public static final String VEHICLE_LIST = "/list";
    public static final String VEHICLE_CREATE = "/create";
    public static final String VEHICLE_SELL = "/sell/{vehicleId}";
    public static final String VEHICLE_DELETE = "/delete/{vehicleId}";
    public static final String VEHICLE_MODIFY = "/modify/{vehicleId}";
    public static final String VEHICLE_CREATOR_FORM = "/creator_form";

    public static final String HOME = "/";
    public static final String SIGN_IN = "/signin";
    public static final String SIGN_UP = "/signup";
    public static final String SIGN_UP_FORM = "/signup_form";
    public static final String SIGN_IN_ERROR = "/signin?error=1";

    public static String getRelativeWithParam(final String relative, final String param) {
        final int start = relative.indexOf("{");
        final String partString = relative.replace(relative.subSequence(start, relative.length()), "");
        final String completeRelative = partString + param;

        return completeRelative;
    }
}
