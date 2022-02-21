package com.asrez.wheremoney.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class SignUpDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private @NotBlank(message = "Name can't be blank.") String name;
    @Email
    private @NotBlank(message = "Email can't be blank.") String email;
    @JsonProperty(value = "family_name")
    private @NotBlank(message = "family_name can't be blank.") String familyName;
    private @NotBlank(message = "username can't be blank.") String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private @NotBlank(message = "password can't be blank.") String password;

    @JsonProperty(value = "created_date")
    @JsonIgnoreProperties(allowGetters = true)
    private LocalDateTime createdDate;
    @JsonProperty(value = "modified_date")
    @JsonIgnoreProperties(allowGetters = true)
    private LocalDateTime modifiedDate;
}
