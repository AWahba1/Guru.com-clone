package PaymentMS.Repositories;

import PaymentMS.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //List<Transaction> findByUserId(Long userId);

    List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
