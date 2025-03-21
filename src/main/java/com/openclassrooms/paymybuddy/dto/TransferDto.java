package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long receiverId;

    @NotNull
    @Min(value = 1, message = "Le montant doit être supérieur ou égal à 1.")
    private BigDecimal amount;

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères.")
    private String description;
}
