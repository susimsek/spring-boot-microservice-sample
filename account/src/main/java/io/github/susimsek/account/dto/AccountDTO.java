package io.github.susimsek.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Schema(
    name = "Account",
    description = "Schema to hold Account information"
)
public record AccountDTO(

    @Schema(
        description = "Account Number of Eazy Bank account", example = "3454433243"
    )
    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "AccountNumber must be 10 digits")
    Long accountNumber,

    @Schema(
        description = "Account type of Eazy Bank account", example = "Savings"
    )
    @NotEmpty(message = "AccountType can not be a null or empty")
    String accountType,

    @Schema(
        description = "Eazy Bank branch address", example = "123 NewYork"
    )
    @NotEmpty(message = "BranchAddress can not be a null or empty")
    String branchAddress) {

}