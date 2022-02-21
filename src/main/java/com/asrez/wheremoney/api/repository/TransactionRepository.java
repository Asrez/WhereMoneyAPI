package com.asrez.wheremoney.api.repository;

import com.asrez.wheremoney.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT (SELECT COALESCE(SUM(price),0)\n" +
            "FROM transactions\n" +
            "WHERE (is_income=TRUE AND calculate_in_monthly=TRUE AND user_id= ?1))\n" +
            "- \n" +
            "(SELECT COALESCE(SUM(price),0) \n" +
            "FROM transactions\n" +
            "WHERE (is_income=FALSE AND calculate_in_monthly=TRUE AND user_id= ?1)) AS balance", nativeQuery = true)
    Long accountBalance(Long userId);

    @Query(value = "SELECT (SELECT COALESCE(SUM(price),0)\n" +
            "FROM transactions\n" +
            "WHERE (is_income=TRUE AND calculate_in_monthly=TRUE AND user_id= ?1)) as total_income", nativeQuery = true)
    Long totalIncome(Long userId);

    @Query(value = "SELECT (SELECT COALESCE(SUM(price),0)\n" +
            "FROM transactions\n" +
            "WHERE (is_income=FALSE AND calculate_in_monthly=TRUE AND user_id= ?1)) as total_income", nativeQuery = true)
    Long totalOutcome(Long userId);

    Optional<Transaction> findByUserIdAndId(Long userId, Long id);

    List<Transaction> findAllByUserId(Long userId);
}
