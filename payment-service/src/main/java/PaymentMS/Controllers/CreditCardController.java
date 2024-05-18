package PaymentMS.Controllers;

import PaymentMS.Models.CreditCard;
import PaymentMS.Models.Transaction;
import PaymentMS.Services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/paymentApp/creditCards")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;
    @PostMapping("/{userId}/addCard")
    public ResponseEntity<CreditCard> addCard(@PathVariable Long userId, @RequestBody CreditCard card) {
        CreditCard addedCard = creditCardService.addCard(userId, card);
        return new ResponseEntity<>(addedCard, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/deleteCard/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long userId, @PathVariable Long cardId) {
        creditCardService.deleteCard(userId, cardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/myCards")
    public ResponseEntity<List<CreditCard>> getCards(@PathVariable Long userId) {
        List<CreditCard> cards = creditCardService.getCardsByUserId(userId);
        cards.forEach(card -> card.setCardNumber("**** **** **** " + card.getCardNumber().substring(12)));
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PostMapping("/{userId}/addFunds/{cardId}")
    public ResponseEntity<Transaction> addFundsWithCard(@PathVariable Long userId, @PathVariable Long cardId, @RequestParam BigDecimal amount) {
        Transaction transaction = creditCardService.addFundsWithCard(userId, cardId, amount);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}

