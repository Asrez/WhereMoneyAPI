package com.asrez.wheremoney.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private @NotNull Long price;
    private String source;
    private String destination;
    @JsonProperty(value = "account_number")
    private String accountNumber;
    private String description;
    @JsonProperty(value = "is_income")
    private @NotNull Boolean isIncome;

}
