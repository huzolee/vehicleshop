package hu.inbuss.vehicleshop.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Entity
@Table(name = "Vehicle")
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_seq_gen")
    @SequenceGenerator(name = "vehicle_seq_gen", sequenceName = "vehicle_seq", initialValue = 1, allocationSize = 1)
    private long id;
    @NotEmpty(message = "{vehicle.name_error}")
    private String vehicleName;
    @Min(value = 2, message = "{min.val}")
    private int numberOfWheels;
    @ManyToOne
    private Customer customer;
    @NotEmpty(message = "{vehicle.color_error}")
    private String color;
    @NotNull(message = "{vehicle.type_error}")
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status = Status.FOR_SALE;
    @NotNull(message = "{vehicle.price_error}")
    private int price;

    public Vehicle() {
        this.status = Status.FOR_SALE;
    }

    public Vehicle(final String vehicleName, final int numberOfWheels, final String color, final int price, final Type type) {
        this.vehicleName = vehicleName;
        this.numberOfWheels = numberOfWheels;
        this.color = color;
        this.price = price;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(final String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    public void setNumberOfWheels(final int numberOfWheels) {
        this.numberOfWheels = numberOfWheels;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public enum Type {

        BIKE,
        CAR;
    }

    public enum Status {

        SOLD,
        FOR_SALE
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vehicle other = (Vehicle) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "id=" + id + ", vehicleName=" + vehicleName + ", numberOfWheels=" + numberOfWheels + ", customer=" + customer + ", color=" + color + ", price=" + price + ", type=" + type + '}';
    }
}
