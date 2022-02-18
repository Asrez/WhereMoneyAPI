package com.asrez.wheremoney.api.repository;

import com.asrez.wheremoney.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT ((SELECT SUM(price) FROM transactions WHERE is_income=true AND user_id= ?1) -\n" +
            "        (SELECT SUM(price) FROM transactions WHERE is_income=false AND user_id= ?1)\n" +
            "       ) as balance", nativeQuery = true)
    Long accountBalance(Long userId);

    Optional<Transaction> findByUserIdAndId(Long userId, Long id);

    List<Transaction> findAllByUserId(Long userId);
}
