//package PaymentMS.Kafka;
//
//import PaymentMS.DTOs.PaymentRequest;
//import PaymentMS.Services.TransactionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentConsumer {
//    private static final String TOPIC = "payment_requests";
//
//    private final TransactionService transactionService;
//
//    @Autowired
//    public PaymentConsumer(TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }
//
//    @KafkaListener(topics = TOPIC, groupId = "paymentGroup")
//    public void consumeMessage(PaymentRequest paymentRequest){
//        transactionService.processPayment(paymentRequest);
//    }
//}
