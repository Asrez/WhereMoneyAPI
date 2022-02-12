package com.wheremoney.demo.service;

import com.wheremoney.demo.dto.TransactionDto;
import com.wheremoney.demo.entity.Transaction;
import com.wheremoney.demo.entity.User;
import com.wheremoney.demo.exception.ApiRequestException;
import com.wheremoney.demo.repository.TransactionRepository;
import com.wheremoney.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }


    public Transaction addTransaction(UserDetails userDetails, Transaction transaction) {
        User user = getUser(userDetails);
        Long balance = transactionRepository.accountBalance(user.getId());
        if (!allowedToMakeTransaction(balance, transaction))
            throw new ApiRequestException("Insufficient balance!", " INSUFFICIENT_BALANCE");

        transaction.setUserId(user.getId());
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(UserDetails userDetails, Long id) {
        User user = getUser(userDetails);
        Optional<Transaction> transactionOptional = transactionRepository.findByUserIdAndId(user.getId(), id);
        if (transactionOptional.isEmpty())
            throw new ApiRequestException("Transaction not found!", " TRANSACTION_NOT_FOUND");

        return transactionOptional.get();
    }

    public List<Transaction> getAll(UserDetails userDetails) {
        return transactionRepository.findAllByUserId(getUser(userDetails).getId());
    }

    public Transaction updateTransaction(UserDetails userDetails, Long id, Transaction transaction) {
        User user = getUser(userDetails);
        Optional<Transaction> transactionOptional = transactionRepository.findByUserIdAndId(user.getId(), id);
        if (transactionOptional.isEmpty())
            throw new ApiRequestException("Transaction not found!", " TRANSACTION_NOT_FOUND");

        Transaction oldTransaction = transactionOptional.get();
        if (transaction.getSource() != null) {
            oldTransaction.setSource(transaction.getSource());
        }

        if (transaction.getPrice() != null) {
            if (transaction.getIsIncome() == null)
                throw new ApiRequestException("is_income has to be set in order to update the price!", " IS_INCOME_NOT_SET");

            Long balance = transactionRepository.accountBalance(user.getId());
            if (!allowedToMakeTransaction(balance, transaction))
                throw new ApiRequestException("Insufficient balance!", " INSUFFICIENT_BALANCE");

            oldTransaction.setPrice(transaction.getPrice());
        }

        if (transaction.getDescription() != null) {
            oldTransaction.setDescription(transaction.getDescription());
        }

        if (transaction.getDestination() != null) {
            oldTransaction.setDestination(transaction.getDestination());
        }

        if (transaction.getAccountNumber() != null) {
            oldTransaction.setAccountNumber(transaction.getAccountNumber());
        }

        if (transaction.getIsIncome() != null) {
            if (transaction.getPrice() == null)
                throw new ApiRequestException("price has to be set in order to update the price!", " PRICE_NOT_SET");

            Long balance = transactionRepository.accountBalance(user.getId());
            if (!allowedToMakeTransaction(balance, transaction))
                throw new ApiRequestException("Insufficient balance!", " INSUFFICIENT_BALANCE");

            oldTransaction.setIsIncome(transaction.getIsIncome());
        }

        transactionRepository.save(oldTransaction);
        return oldTransaction;

    }

    public Map<Object, Object> deleteTransaction(UserDetails userDetails, Long id) {
        User user = getUser(userDetails);
        Optional<Transaction> transactionOptional = transactionRepository.findByUserIdAndId(user.getId(), id);
        if (transactionOptional.isEmpty())
            throw new ApiRequestException("Transaction not found!", " TRANSACTION_NOT_FOUND");

        transactionRepository.deleteById(id);
        Map<Object, Object> model = new HashMap<>();
        model.put("id", id);
        model.put("success", true);
        return model;
    }

    public Map<Object, Object> getUserBalance(UserDetails userDetails) {
        User user = getUser(userDetails);
        Long balance = transactionRepository.accountBalance(user.getId());
        System.out.println(balance);

        if (balance == null)
            balance = 0L;

        Map<Object, Object> model = new HashMap<>();
        model.put("balance", balance);
        return model;
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername()).get();
    }

    private boolean allowedToMakeTransaction(Long balance, Transaction transaction) {
        if (balance != null && !transaction.getIsIncome() && balance < transaction.getPrice())
            return false;

        return true;
    }
}
