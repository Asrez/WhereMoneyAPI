package com.asrez.wheremoney.api.service;

import com.asrez.wheremoney.api.dto.TransactionDto;
import com.asrez.wheremoney.api.entity.Transaction;
import com.asrez.wheremoney.api.entity.Type;
import com.asrez.wheremoney.api.entity.User;
import com.asrez.wheremoney.api.enums.SortByEnum;
import com.asrez.wheremoney.api.enums.SortDirEnum;
import com.asrez.wheremoney.api.exception.ApiRequestException;
import com.asrez.wheremoney.api.repository.TransactionRepository;
import com.asrez.wheremoney.api.repository.UserRepository;
import org.modelmapper.ModelMapper;
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
    private final TypeService typeService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TypeService typeService, UserRepository userRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.typeService = typeService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    public Transaction addTransaction(UserDetails userDetails, TransactionDto transactionDto) {
        User user = getUser(userDetails);
        Long balance = transactionRepository.accountBalance(user.getId());
        Type type = typeService.getType(transactionDto.getTypeId());

        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

        if (transaction.getCalculateInMonthly())
            if (!allowedToMakeTransaction(balance, transaction))
                throw new ApiRequestException("Insufficient balance!", " INSUFFICIENT_BALANCE");

        transaction.setUserId(user.getId());
        transaction.setType(type);
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

    public Transaction updateTransaction(UserDetails userDetails, Long id, TransactionDto transactionDto) {
        User user = getUser(userDetails);
        Optional<Transaction> transactionOptional = transactionRepository.findByUserIdAndId(user.getId(), id);
        if (transactionOptional.isEmpty())
            throw new ApiRequestException("Transaction not found!", " TRANSACTION_NOT_FOUND");


        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

        Transaction oldTransaction = transactionOptional.get();

        if (transactionDto.getTypeId() != null) {
            Type type = typeService.getType(transactionDto.getTypeId());
            oldTransaction.setType(type);
        }

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
