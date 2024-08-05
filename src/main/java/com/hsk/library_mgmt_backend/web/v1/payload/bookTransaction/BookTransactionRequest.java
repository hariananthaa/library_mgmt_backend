package com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction;

import jakarta.validation.constraints.NotNull;

public record BookTransactionRequest(
        @NotNull(message = "Book ID is mandatory")
        Long bookId,
        @NotNull(message = "Member ID is mandatory")
        Long memberId
) {
}
