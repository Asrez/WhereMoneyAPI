package com.asrez.wheremoney.api.service;

import com.asrez.wheremoney.api.entity.Transaction;
import com.asrez.wheremoney.api.entity.User;
import com.asrez.wheremoney.api.enums.SortByEnum;
import com.asrez.wheremoney.api.enums.SortDirEnum;
import com.asrez.wheremoney.api.exception.ApiRequestException;
import com.asrez.wheremoney.api.repository.TransactionRepository;
import com.asrez.wheremoney.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
        if (transaction.getCalculateInMonthly())
            if (!allowedToMakeTransaction(balance, transaction))
                throw new ApiRequestException("Insufficient balance!", " INSUFFICIENT_BALANCE");

        transaction.setUserId(user.getId());
        transaction.setCreatedDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(UserDetails userDetails, Long id) {
        User user = getUser(userDetails);
        Optional<Transaction> transactionOptional = transactionRepository.findByUserIdAndId(user.getId(), id);
        if (transactionOptional.isEmpty())
            throw new ApiRequestException("Transaction not found!", " TRANSACTION_NOT_FOUND");

        return transactionOptional.get();
    }

    public List<Transaction> getAll(UserDetails userDetails, Integer pageNo, Integer pageSize, SortByEnum sortBy, SortDirEnum sortDir) {
        Pageable paging = PageRequest.of(pageNo, pageSize,
                sortDir == SortDirEnum.ASC ? Sort.by(sortBy.toString()).ascending()
                        : Sort.by(sortBy.toString()).descending());
        Page<Transaction> transactionPage = transactionRepository.findAllByUserId(getUser(userDetails).getId(), paging);

        if (transactionPage.hasContent())
            return transactionPage.getContent();

        return new ArrayList<Transaction>();

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
            if (transaction.getCalculateInMonthly())
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
            if (transaction.getCalculateInMonthly())
                if (!allowedToMakeTransaction(balance, transaction))
                    throw new ApiRequestException("Insufficient balance!", " INSUFFICIENT_BALANCE");

            oldTransaction.setIsIncome(transaction.getIsIncome());
        }

        oldTransaction.setModifiedDate(LocalDateTime.now());
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
        Long totalIncome = transactionRepository.totalIncome(user.getId());
        Long totalOutcome = transactionRepository.totalOutcome(user.getId());

        Map<Object, Object> model = new HashMap<>();
        model.put("balance", balance);
        model.put("total_income", totalIncome);
        model.put("total_outcome", totalOutcome);
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
