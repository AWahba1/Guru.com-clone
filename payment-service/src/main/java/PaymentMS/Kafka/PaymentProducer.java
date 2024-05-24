//package PaymentMS.Kafka;
//
//import PaymentMS.DTOs.PaymentRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentProducer {
//    private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;
//    private static final String TOPIC = "payment_requests";
//    private final Logger logger = LoggerFactory.getLogger(PaymentProducer.class);
//
//
//    @Autowired
//    public PaymentProducer(KafkaTemplate<String, PaymentRequest> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendPayment(PaymentRequest paymentRequest) {
//        kafkaTemplate.send(TOPIC, paymentRequest);
//    }
//}
//
