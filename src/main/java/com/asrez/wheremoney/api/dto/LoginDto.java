package com.asrez.wheremoney.api.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class LoginDto {
    private @NotBlank String username;
    private @NotBlank String password;
}
