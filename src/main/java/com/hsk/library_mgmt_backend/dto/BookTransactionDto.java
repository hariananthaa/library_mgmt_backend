package com.hsk.library_mgmt_backend.dto;

import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.hsk.library_mgmt_backend.persistent.entity.BookTransaction}
 */
public record BookTransactionDto(
        LocalDateTime createdAt,

        String createdBy,

        LocalDateTime updatedAt,

        String updatedBy,
        Long id,

        BookDto book,

        MemberDto member,

        LocalDate issueDate,

        LocalDate dueDate,

        LocalDate returnDate,

        LocalDate requestDate,
        BookTransaction.Status status
) implements Serializable {
}