package PaymentMS.Services;

import PaymentMS.Models.User;
import PaymentMS.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "userCache", key = "#user.id")
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (user.getType() == null || !user.getType().matches("(?i)EMPLOYER|FREELANCER")) {
            throw new IllegalArgumentException("Invalid user type. Must be 'EMPLOYER' or 'FREELANCER'.");
        }
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User ID already exists.");
        }

        if (userRepository.existsByNameIgnoreCase(user.getName())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        return userRepository.save(user);
    }


    @Cacheable(value = "userCache", key = "#userId")
    public User deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setBalance(user.getBalance().add(amount));
        return userRepository.save(user);
    }


    @Cacheable(value = "userCache", key = "#userId")
    public User withdraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(amount));
        return userRepository.save(user);
    }

//    public Optional<User> getUserById(Long userId) {
//        return userRepository.findById(userId);
//    }


    @Cacheable(value = "userCache", key = "#userId")
    public BigDecimal getUserBalance(Long userId) {
        System.out.println("Searching for User");
        Optional<User> user = userRepository.findById(userId);
        System.out.println("User Found");
        return user.map(User::getBalance).orElse(BigDecimal.ZERO);
    }
}
