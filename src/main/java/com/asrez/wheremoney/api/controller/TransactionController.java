package com.asrez.wheremoney.api.controller;

import com.asrez.wheremoney.api.dto.TransactionDto;
import com.asrez.wheremoney.api.entity.Transaction;
import com.asrez.wheremoney.api.enums.SortByEnum;
import com.asrez.wheremoney.api.enums.SortDirEnum;
import com.asrez.wheremoney.api.service.TransactionService;
import com.asrez.wheremoney.api.utils.ApplicationConst;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService service;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionController(TransactionService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> addTransaction(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        TransactionDto transactionDtoResponse = modelMapper.map(service.addTransaction(userDetails, transaction), TransactionDto.class);
        return ok(transactionDtoResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionDto> getTransaction(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id) {
        TransactionDto transactionDtoResponse = modelMapper.map(service.getTransaction(userDetails, id), TransactionDto.class);
        return ok(transactionDtoResponse);
    }

    @GetMapping
    public List<TransactionDto> getAll(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam(defaultValue = ApplicationConst.pageNo, name = "page") @Valid @Min(0) Integer pageNo,
                                       @RequestParam(defaultValue = ApplicationConst.pageSize, name = "page_size") @Valid @Min(1) @Max(100) Integer pageSize,
                                       @RequestParam(defaultValue = ApplicationConst.sortBy, name = "sort_by") SortByEnum sortBy,
                                       @RequestParam(defaultValue = ApplicationConst.sortDir, name = "sort_dir") SortDirEnum sortDir) {

        return service.getAll(userDetails, pageNo, pageSize, sortBy, sortDir).stream().map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping("{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long id, @RequestBody TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        TransactionDto transactionDtoResponse = modelMapper.map(service.updateTransaction(userDetails, id, transaction), TransactionDto.class);
        return ok(transactionDtoResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTransaction(@AuthenticationPrincipal UserDetails userData, @PathVariable("id") Long id) {
        return new ResponseEntity<Object>(service.deleteTransaction(userData, id), HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<Object> getUserBalance(@AuthenticationPrincipal UserDetails userData) {
        return new ResponseEntity<Object>(service.getUserBalance(userData), HttpStatus.OK);
    }
}
