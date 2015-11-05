package hu.inbuss.vehicleshop.repository;

import hu.inbuss.vehicleshop.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public interface SellerRepository extends JpaRepository<Seller, Long> {

    public Seller findByUsername(final String username);
}
