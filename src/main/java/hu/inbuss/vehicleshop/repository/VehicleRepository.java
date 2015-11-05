package hu.inbuss.vehicleshop.repository;

import hu.inbuss.vehicleshop.model.Vehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    public List<Vehicle> findByCustomerId(final long customerId);

    public List<Vehicle> findByStatus(final Vehicle.Status status);

    public List<Vehicle> findByVehicleName(final String vehicleName);
}
