package hu.inbuss.vehicleshop.service;

import hu.inbuss.vehicleshop.model.Seller;
import hu.inbuss.vehicleshop.repository.SellerRepository;
import hu.inbuss.vehicleshop.util.Roles;
import java.util.Collections;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
public class UserService implements UserDetailsService {

    @Autowired
    private SellerRepository sellerRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final Seller seller = sellerRepo.findByUsername(username);
        if (seller == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return createUser(seller);
    }

    private UserDetails createUser(final Seller seller) {
        return new User(seller.getUsername(), seller.getPassword(), Collections.singleton(new SimpleGrantedAuthority(seller.getSellerRole())));
    }

    public Seller saveSeller(final Seller seller) {
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        return sellerRepo.save(seller);
    }

    @PostConstruct
    protected void initialize() {
        saveSeller(new Seller("test", "test", Roles.SELLER));
    }
}
