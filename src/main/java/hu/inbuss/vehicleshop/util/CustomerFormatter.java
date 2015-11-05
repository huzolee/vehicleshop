package hu.inbuss.vehicleshop.util;

import hu.inbuss.vehicleshop.model.Customer;
import hu.inbuss.vehicleshop.repository.CustomerRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Component
public class CustomerFormatter implements Formatter<Customer> {

    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public String print(final Customer customer, final Locale locale) {
        return String.valueOf(customer.getId());
    }

    @Override
    public Customer parse(final String customerId, final Locale locale) throws ParseException {
        final Customer customer = customerRepo.findById(Long.parseLong(customerId));
        return customer;
    }
}
