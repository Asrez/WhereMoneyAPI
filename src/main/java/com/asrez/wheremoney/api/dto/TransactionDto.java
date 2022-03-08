package com.asrez.wheremoney.api.dto;

import com.asrez.wheremoney.api.entity.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @JsonProperty(value = "calculate_in_monthly")
    private @NotNull Boolean calculateInMonthly;
    @JsonProperty(value = "created_date")
    @JsonIgnoreProperties(allowGetters = true)
    private LocalDateTime createdDate;
    @JsonProperty(value = "modified_date")
    @JsonIgnoreProperties(allowGetters = true)
    private LocalDateTime modifiedDate;

    @JsonProperty(value = "type_id")
    @JsonIgnoreProperties(allowSetters = true)
    private Long typeId;
    @JsonIgnoreProperties(allowGetters = true)
    private Type type;

}
