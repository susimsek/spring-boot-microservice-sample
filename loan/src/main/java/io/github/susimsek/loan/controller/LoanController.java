package io.github.susimsek.loan.controller;

import io.github.susimsek.loan.constants.LoanConstants;
import io.github.susimsek.loan.dto.ErrorResponseDTO;
import io.github.susimsek.loan.dto.LoanContactInfoDTO;
import io.github.susimsek.loan.dto.LoanDTO;
import io.github.susimsek.loan.dto.ResponseDTO;
import io.github.susimsek.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(
    name = "loan",
    description = "CRUD REST APIs for Loans in EazyBank"
)
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class LoanController {

    private final LoanService loanService;

    private final Environment environment;

    private final LoanContactInfoDTO loanContactInfo;

    @Value("${build.version}")
    private String buildVersion;

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Create Loan REST API",
        description = "REST API to create new loan inside EazyBank"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Created"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    }
    )
    @PostMapping("/loan")
    public ResponseEntity<ResponseDTO> createLoan(
        @RequestParam
        @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
        String mobileNumber) {
        loanService.createLoan(mobileNumber);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ResponseDTO(LoanConstants.STATUS_201, LoanConstants.MESSAGE_201));
    }

    @Operation(
        summary = "Fetch Loan Details REST API",
        description = "REST API to fetch loan details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ok"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    }
    )
    @GetMapping("/loan")
    public ResponseEntity<LoanDTO> fetchLoanDetails(
        @RequestHeader("X-Correlation-Id") String correlationId,
        @RequestParam
        @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
        String mobileNumber) {
        log.debug("fetchLoanDetails method start");
        var model = loanService.fetchLoan(mobileNumber);
        log.debug("fetchLoanDetails method end");
        return ResponseEntity.ok(model);
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Update Loan Details REST API",
        description = "REST API to update loan details based on a loan number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ok"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Expectation Failed"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    }
    )
    @PutMapping("/loan/{loanNumber}")
    public ResponseEntity<ResponseDTO> updateLoanDetails(
        @NotEmpty(message = "Loan Number can not be a null or empty")
        @Pattern(regexp = "(^$|[0-9]{12})", message = "LoanNumber must be 12 digits")
        @PathVariable String loanNumber,
        @Valid @RequestBody LoanDTO loan) {
        boolean isUpdated = loanService.updateLoan(loanNumber, loan);
        if (isUpdated) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDTO(LoanConstants.STATUS_417, LoanConstants.MESSAGE_417_UPDATE));
        }
    }


    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Delete Loan Details REST API",
        description = "REST API to delete Loan details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "No content"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    }
    )
    @DeleteMapping("/loan")
    public ResponseEntity<Void> deleteLoanDetails(
        @RequestParam
        @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
        String mobileNumber
    ) {
        loanService.deleteLoan(mobileNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get Build information",
        description = "Get Build information that is deployed into loan microservice"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ok"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    }
    )
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
            .ok(buildVersion);
    }

    @Operation(
        summary = "Get Java version",
        description = "Get Java versions details that is installed into loan microservice"
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Ok"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(
                    schema = @Schema(implementation = ErrorResponseDTO.class)
                )
            )
        }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
        summary = "Get Contact Info",
        description = "Contact Info details that can be reached out in case of any issues"
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Ok"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(
                    schema = @Schema(implementation = ErrorResponseDTO.class)
                )
            )
        }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<LoanContactInfoDTO> getContactInfo() {
        log.debug("Invoked Loans contact-info API");
        return ResponseEntity.ok(loanContactInfo);
    }
}