package hu.inbuss.vehicleshop.exception;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class CustomException extends RuntimeException {

    public CustomException(final String message) {
        super(message);
    }
}
