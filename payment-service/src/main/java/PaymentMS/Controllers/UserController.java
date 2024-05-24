package PaymentMS.Controllers;


import PaymentMS.DTOs.Deposit;
import PaymentMS.DTOs.Withdraw;
import PaymentMS.Models.Transaction;
import PaymentMS.DTOs.PaymentRequest;
import PaymentMS.Models.User;
import PaymentMS.Services.TransactionService;
import PaymentMS.Services.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/paymentApp")
public class UserController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "payments";

    private static final String ROUTING_KEY_CREATE_USER = "user.create";
    private static final String ROUTING_KEY_GET_TRANSACTIONS = "user.transactions";
    private static final String ROUTING_KEY_DEPOSIT = "user.deposit";
    private static final String ROUTING_KEY_WITHDRAW = "user.withdraw";


    @GetMapping("/{userId}/transactions")
    public ResponseEntity<String> getTransactionsByUserId(@PathVariable Long userId) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_GET_TRANSACTIONS, userId);
        return ResponseEntity.accepted().body("Fetching transactions...");
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_CREATE_USER, user);
        return ResponseEntity.accepted().body("User creation in progress...");
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<User> deposit(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        User updatedUser = userService.deposit(userId, amount);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<User> withdraw(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        User updatedUser = userService.withdraw(userId, amount);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserBalance(userId));
    }

}
