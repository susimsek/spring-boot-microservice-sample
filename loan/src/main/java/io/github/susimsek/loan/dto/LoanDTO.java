package io.github.susimsek.loan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "Loan",
    description = "Schema to hold Loan information"
)
public record LoanDTO(

    @Schema(
        description = "Mobile Number of Customer", example = "4365327698"
    )
    @NotEmpty(message = "Mobile Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    String mobileNumber,

    @Schema(
        description = "Loan Number of the customer", example = "548732457654"
    )
    @NotEmpty(message = "Loan Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{12})",message = "LoanNumber must be 12 digits")
    String loanNumber,

    @Schema(
        description = "Type of the loan", example = "Home Loan"
    )
    @NotEmpty(message = "LoanType can not be a null or empty")
    String loanType,

    @Schema(
        description = "Total loan amount", example = "100000"
    )
    @Positive(message = "Total loan amount should be greater than zero")
    int totalLoan,

    @Schema(
        description = "Total loan amount paid", example = "1000"
    )
    @PositiveOrZero(message = "Total loan amount paid should be equal or greater than zero")
    int amountPaid,

    @PositiveOrZero(message = "Total outstanding amount should be equal or greater than zero")
    @Schema(
        description = "Total outstanding amount against a loan", example = "99000"
    )
    int outstandingAmount
) {

}