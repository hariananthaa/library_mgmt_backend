package com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction;

import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookTransactionUpdateRequest(
        @FutureOrPresent(message = "Due date must be in the present or future")
        LocalDate dueDate,

        @FutureOrPresent(message = "Return date must be in the present or future")
        LocalDate returnDate,

        @NotNull(message = "Status is mandatory")
        BookTransaction.Status status

) {
}
