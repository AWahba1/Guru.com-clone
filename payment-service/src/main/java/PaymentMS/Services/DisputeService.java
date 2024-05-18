package PaymentMS.Services;

import PaymentMS.Models.Dispute;
import PaymentMS.Models.Transaction;
import PaymentMS.Models.User;
import PaymentMS.Repositories.DisputeRepository;
import PaymentMS.Repositories.TransactionRepository;
import PaymentMS.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DisputeService {

    @Autowired
    private DisputeRepository disputeRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public Dispute createDispute(Long transactionId, Long initiatorId, String reason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Dispute dispute = new Dispute();
        dispute.setTransaction(transaction);
        dispute.setInitiator(initiator);
        dispute.setReason(reason);
        dispute.setTimestamp(LocalDateTime.now());
        dispute.setStatus("OPEN");

        return disputeRepository.save(dispute);
    }

    public Dispute updateDisputeStatus(Long disputeId, String newStatus) {
        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new EntityNotFoundException("Dispute not found"));
        dispute.setStatus(newStatus);
        return disputeRepository.save(dispute);
    }

}
