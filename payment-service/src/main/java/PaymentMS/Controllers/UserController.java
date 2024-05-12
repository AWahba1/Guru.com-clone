package PaymentMS.Controllers;


import PaymentMS.Models.Transaction;
import PaymentMS.DTOs.PaymentRequest;
import PaymentMS.Models.User;
import PaymentMS.Services.TransactionService;
import PaymentMS.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paymentApp")
public class UserController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @PostMapping("/users/addUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/users/{userId}/deposit")
    public ResponseEntity<User> deposit(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        User updatedUser = userService.deposit(userId, amount);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/withdraw")
    public ResponseEntity<User> withdraw(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        User updatedUser = userService.withdraw(userId, amount);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/balance")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Long userId) {
        BigDecimal balance = userService.getUserBalance(userId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

}
