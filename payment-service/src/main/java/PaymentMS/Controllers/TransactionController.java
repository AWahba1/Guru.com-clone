package PaymentMS.Controllers;

import PaymentMS.Models.Transaction;
import PaymentMS.DTOs.PaymentRequest;
import PaymentMS.Services.TransactionService;
import PaymentMS.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/paymentApp")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private UserService userService;

    @PostMapping("/process")
    public ResponseEntity<Transaction> processPayment(@RequestBody PaymentRequest paymentRequest) {
        Transaction transaction = transactionService.processPayment(
                paymentRequest.getSenderId(),
                paymentRequest.getReceiverId(),
                paymentRequest.getAmount()
        );
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
