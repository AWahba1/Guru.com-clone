package PaymentMS.RabbitMQ;

import PaymentMS.DTOs.PaymentRequest;
import PaymentMS.Services.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentProducer {

//    @Autowired
//    private TransactionService transactionService;
    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPayment(PaymentRequest paymentRequest) {
        System.out.println("Sending payment request: " + paymentRequest);
        rabbitTemplate.convertAndSend("payments", "payment.process", paymentRequest);
    }
}
