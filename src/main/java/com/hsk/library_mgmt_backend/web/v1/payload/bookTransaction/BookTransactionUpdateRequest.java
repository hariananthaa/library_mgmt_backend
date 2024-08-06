package com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction;

import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record BookTransactionUpdateRequest(
        @FutureOrPresent(message = "Due date must be in the present or future")
        LocalDate dueDate,

        @PastOrPresent(message = "Return date must be in the past or present")
        LocalDate returnDate,

        @NotNull(message = "Status is mandatory")
        BookTransaction.Status status

) {
}
