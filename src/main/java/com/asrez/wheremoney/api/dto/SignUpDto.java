package com.asrez.wheremoney.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private @NotBlank(message = "Name can't be blank.") String name;
    @JsonProperty(value = "family_name")
    private @NotBlank(message = "family_name can't be blank.") String familyName;
    private @NotBlank(message = "username can't be blank.") String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private @NotBlank(message = "password can't be blank.") String password;
}
