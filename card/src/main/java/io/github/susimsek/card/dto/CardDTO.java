package io.github.susimsek.card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "Card",
    description = "Schema to hold Card information"
)
public record CardDTO(

    @Schema(
        description = "Mobile Number of Customer", example = "4354437687"
    )
    @NotEmpty(message = "Mobile Number can not be a null or empty")
    @Pattern(regexp = "(^$|\\d{10})", message = "Mobile Number must be 10 digits")
    String mobileNumber,


    @Schema(
        description = "Card Number of the customer", example = "100646930341"
    )
    @NotEmpty(message = "Card Number can not be a null or empty")
    @Pattern(regexp = "(^$|\\d{12})", message = "CardNumber must be 12 digits")
    String cardNumber,

    @Schema(
        description = "Type of the card", example = "Credit Card"
    )
    @NotEmpty(message = "CardType can not be a null or empty")
    String cardType,

    @Schema(
        description = "Total amount limit available against a card", example = "100000"
    )
    @Positive(message = "Total card limit should be greater than zero")
    int totalLimit,

    @Schema(
        description = "Total amount used by a Customer", example = "1000"
    )
    @PositiveOrZero(message = "Total amount used should be equal or greater than zero")
    int amountUsed,

    @Schema(
        description = "Total available amount against a card", example = "90000"
    )
    @PositiveOrZero(message = "Total available amount should be equal or greater than zero")
    int availableAmount
) {

}