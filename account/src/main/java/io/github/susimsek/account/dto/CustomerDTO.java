package io.github.susimsek.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Schema(
    name = "Customer",
    description = "Schema to hold Customer and Account information"
)
public record CustomerDTO(

    @Schema(
        description = "Name of the customer", example = "Eazy Bytes"
    )
    @NotEmpty(message = "Name can not be a null or empty")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between {min} and {max}")
    String name,

    @Schema(
        description = "Email address of the customer", example = "tutor@eazybytes.com"
    )
    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value")
    String email,

    @Schema(
        description = "Mobile Number of the customer", example = "9345432123"
    )
    @Pattern(regexp = "(^$|\\d{10})", message = "Mobile number must be 10 digits")
    String mobileNumber,

    @Schema(
        description = "Account details of the Customer"
    )
    @Valid
    AccountDTO account) {

}