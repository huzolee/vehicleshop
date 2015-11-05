package hu.inbuss.vehicleshop.model;

import hu.inbuss.vehicleshop.util.Roles;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Entity
@Table(name = "Seller")
public class Seller implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_seq_gen")
    @SequenceGenerator(name = "seller_seq_gen", sequenceName = "seller_seq", initialValue = 1, allocationSize = 1)
    private long id;
    @Column(unique = true)
    @NotEmpty(message = "{signup.username.required}")
    private String username;
    @NotEmpty(message = "{signup.password.required}")
    private String password;
    private String sellerRole = Roles.SELLER;

    public Seller(final String username, final String password, final String sellerRole) {
        this.username = username;
        this.password = password;
        this.sellerRole = sellerRole;
    }

    public Seller() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSellerRole() {
        return sellerRole;
    }

    public void setSellerRole(final String sellerRole) {
        this.sellerRole = sellerRole;
    }
}
