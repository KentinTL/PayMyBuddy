package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    private BigDecimal amount;

    private String description;
}
