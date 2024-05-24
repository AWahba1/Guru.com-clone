package PaymentMS.RabbitMQ;

import PaymentMS.DTOs.Deposit;
import PaymentMS.DTOs.Withdraw;
import PaymentMS.Models.Transaction;
import PaymentMS.Models.User;
import PaymentMS.Services.TransactionService;
import PaymentMS.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class UserConsumer {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @RabbitListener(queues = "userCreateQueue")
    public void consumeCreateUserMessage(User user) {
        try {
            userService.createUser(user);
            System.out.println("User created successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "userTransactionsQueue")
    public void consumeGetTransactionsMessage(Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        System.out.println("Transactions for user " + userId + ": " + transactions);
    }

//    @RabbitListener(queues = "userDepositQueue")
//    public void consumeDepositMessage(Deposit deposit) {
//        Long userId = deposit.getUserId();
//        BigDecimal amount = deposit.getAmount();
//        try {
//            userService.deposit(userId, amount);
//            System.out.println("Deposit successful for user " + userId);
//        } catch (EntityNotFoundException | IllegalArgumentException e) {
//            System.err.println("Error during deposit: " + e.getMessage());
//        }
//    }
//
//    @RabbitListener(queues = "userWithdrawQueue")
//    public void consumeWithdrawMessage(Withdraw withdraw) {
//        Long userId = withdraw.getUserId();
//        BigDecimal amount = withdraw.getAmount();
//        try {
//            userService.withdraw(userId, amount);
//            System.out.println("Withdrawal successful for user " + userId);
//        } catch (EntityNotFoundException | IllegalArgumentException e) {
//            System.err.println("Error during withdrawal: " + e.getMessage());
//        }
//    }
}



