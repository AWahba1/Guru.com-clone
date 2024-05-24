package PaymentMS.Services;

import PaymentMS.Models.CreditCard;
import PaymentMS.Models.Transaction;
import PaymentMS.Models.User;
import PaymentMS.Repositories.CreditCardRepository;
import PaymentMS.Repositories.TransactionRepository;
import PaymentMS.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.math.BigDecimal;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //@Cacheable(value = "cardCache", key = "#userId")
    public CreditCard addCard(Long userId, CreditCard card) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        card.setUser(user);
        return creditCardRepository.save(card);
    }

    //@CacheEvict(value = "cardCache", key = "#userId")
    public void deleteCard(Long userId, Long cardId) {
        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Credit card not found"));

        if (!card.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized access to credit card");
        }

        creditCardRepository.delete(card);
    }


    //@Cacheable(value = "cardCache", key = "#userId")
    public List<CreditCard> getCardsByUserId(Long userId) {
        return creditCardRepository.findByUserId(userId);
    }


    public Transaction addFundsWithCard(Long userId, Long cardId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Credit card not found"));

        if (!card.getUser().equals(user)) {
            throw new SecurityException("Unauthorized access to credit card");
        }

        // In a real scenario, We would integrate here with a payment gateway
        System.out.println("Processing payment of " + amount + " with card " + card.getCardNumber());

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setReceiverId(user.getId());
        transaction.setAmount(amount);
        transaction.setTimestamp(java.time.LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
}

