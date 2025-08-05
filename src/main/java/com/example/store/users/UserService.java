package com.example.store.users;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.store.products.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Collections.emptyList());
    }

    // Need to check if we need these methods
    @Transactional
    public void showEntityStates() {
        var user = User.builder().name("Hai").email("hainn2009@gmail.com").password("123456").build();
        if (entityManager.contains(user)) {
            System.out.println("User is managed by EntityManager");
        } else {
            System.out.println("Transient / Detached User");
        }
        userRepository.save(user);
        if (entityManager.contains(user)) {
            System.out.println("User is managed by EntityManager");
        } else {
            System.out.println("Transient / Detached User");
        }
    }

    public void getAddresses() {
        var address = addressRepository.findById(1L).orElseThrow();
        System.out.println(address.getCity());
    }

    @Transactional
    public void showRelatedEntities() {
        var profile = profileRepository.findById(1L).orElseThrow();
        System.out.println(profile.getUser().getEmail());
    }

    public void fetchAddresses() {
        var address = addressRepository.findById(1L).orElseThrow();
        // var user = repository.findById(1L).orElseThrow();
        System.out.println(address.getCity());
    }

    public void persistRelated() {
        var user = User.builder().name("Hai").email("hainn2009@gmail.com").password("123456").build();
        var address = Address.builder().street("Chua Lang").city("HN").state("HN").zip("123").build();
        user.addAddress(address);
        userRepository.save(user);
    }

    // TODO: to remove
    @Transactional
    public void deletedRelated() {
        var user = userRepository.findById(2L).orElseThrow();
        var address = user.getAddresses().get(0);
        user.removeAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void manageProducts() {
        // var user = userRepository.findById(1L).orElseThrow();
        // var products = productRepository.findAll();
        // products.forEach(user::addFavouriteProduct);
        // userRepository.save(user);
        productRepository.deleteById(2L);
    }

    @Transactional
    public void updateProductPrices() {
        // productRepository.updatePriceByCategory(new BigDecimal(200), (byte) 1);
    }

    public void fetchProducts() {
        // var products = productRepository.findProducts(BigDecimal.valueOf(10),
        // BigDecimal.valueOf(500));
        // products.forEach(System.out::println);
    }

    @Transactional
    public void fetchUser() {
        var user = userRepository.findByEmail("hainn2009@gmail.com").orElseThrow();
        System.err.println(user);
    }

    public void fetchUsers() {
        var users = userRepository.findAllWithAddresses();
        users.forEach(user -> {
            System.out.println(user);
            user.getAddresses().forEach(System.out::println);
            ;
        });
    }
}
