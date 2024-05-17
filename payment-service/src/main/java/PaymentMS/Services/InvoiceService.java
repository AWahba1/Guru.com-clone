package PaymentMS.Services;

import PaymentMS.Models.Invoice;
import PaymentMS.DTOs.PaymentRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvoiceService {

    public Invoice generateInvoice(PaymentRequest paymentRequest) {
        Invoice invoice = new Invoice();
        invoice.setAmount(paymentRequest.getAmount());
        invoice.setTransactionTime(LocalDateTime.now());
        invoice.setInvoiceId(UUID.randomUUID().toString());
        return invoice;
    }
}