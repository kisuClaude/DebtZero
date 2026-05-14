package com.chubeo.DebtZero.dto.response;

import com.chubeo.DebtZero.enums.Severity;
import com.chubeo.DebtZero.enums.WarningType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class WarningResponse {
    private WarningType type;
    private Severity severity;
    private String messgae;
    private UUID debtId;
}
