package PaymentMS.RabbitMQ;

import PaymentMS.DTOs.PaymentRequest;
import PaymentMS.Services.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    @Autowired
    private TransactionService transactionService;

//    public PaymentConsumer(TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }

    @RabbitListener(queues = "paymentQueue")
    private void consumeMessage(PaymentRequest paymentRequest){
        transactionService.processPayment(paymentRequest);
        System.out.println("Payment processed successfully.");
    }
}
