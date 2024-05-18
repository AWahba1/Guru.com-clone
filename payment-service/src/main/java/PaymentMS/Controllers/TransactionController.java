package PaymentMS.Controllers;

import PaymentMS.Models.Transaction;
import PaymentMS.DTOs.PaymentRequest;
import PaymentMS.RabbitMQ.PaymentProducer;
import PaymentMS.Services.TransactionService;
import PaymentMS.Services.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

@RestController
@RequestMapping("/paymentApp")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private UserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PaymentProducer paymentProducer;


    @PostMapping("/process")
    public ResponseEntity<Transaction> processPayment(@RequestBody PaymentRequest paymentRequest) {
        Transaction transaction = transactionService.processPayment(paymentRequest);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

//    @PostMapping("/process")
//    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
//        paymentProducer.sendPayment(paymentRequest);
//        return ResponseEntity.accepted().body("Payment processing initiated.");
//    }


    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
