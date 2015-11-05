package hu.inbuss.vehicleshop.repository;

import hu.inbuss.vehicleshop.model.Customer;
import hu.inbuss.vehicleshop.model.Customer.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findById(final long id);

    public Customer findByName(final String name);

    public Customer findByEmail(final String email);

    public List<Customer> findByStatus(final Status status);
}
