package com.vikas.finadvisor.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseCreateRequest(
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank String category,
        @NotNull LocalDate spentOn,
        @Pattern(regexp = "^[A-Z]{3}$") String currency,
        @Size(max = 255) String note
) {}
