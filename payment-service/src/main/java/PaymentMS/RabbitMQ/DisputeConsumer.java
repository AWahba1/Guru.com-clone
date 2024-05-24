package PaymentMS.RabbitMQ;

import PaymentMS.DTOs.DisputeRequest;
import PaymentMS.Models.User;
import PaymentMS.Services.DisputeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class DisputeConsumer {
    @Autowired
    private DisputeService disputeService;

    @RabbitListener(queues = "DisputeQueue")
    public void consumeDispute(DisputeRequest request) {
        Long transactionId = request.getTransactionId();
        Long initiatorId = request.getInitiatorId();
        String reason = request.getReason();
        try {
            disputeService.createDispute(transactionId, initiatorId, reason);
            System.out.println("dispute created successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating dispute: " + e.getMessage());
        }
    }
}
