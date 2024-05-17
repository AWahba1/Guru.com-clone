package PaymentMS.Controllers;

import PaymentMS.DTOs.DisputeRequest;
import PaymentMS.Models.Dispute;
import PaymentMS.Services.DisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentApp/disputes")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    @PostMapping("/{transactionId}/initiate")
    public ResponseEntity<Dispute> initiateDispute(@PathVariable Long transactionId, @RequestBody DisputeRequest request) {
        Dispute dispute = disputeService.createDispute(transactionId, request.getInitiatorId(), request.getReason());
        return new ResponseEntity<>(dispute, HttpStatus.CREATED);
    }

    @PutMapping("/{disputeId}/updateStatus")
    public ResponseEntity<Dispute> updateDisputeStatus(@PathVariable Long disputeId, @RequestParam String status) {
        Dispute dispute = disputeService.updateDisputeStatus(disputeId, status);
        return new ResponseEntity<>(dispute, HttpStatus.OK);
    }

}

