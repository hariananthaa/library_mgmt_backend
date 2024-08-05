package com.hsk.library_mgmt_backend.web.v1.payload.book;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BookRequest(
        @NotBlank(message = "Title is mandatory")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotBlank(message = "Author is mandatory")
        @Size(max = 255, message = "Author must be less than 255 characters")
        String author,

        @NotBlank(message = "ISBN is mandatory")
        @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
        String isbn,

        @NotBlank(message = "Genre is mandatory")
        @Size(max = 100, message = "Genre must be less than 100 characters")
        String genre,

        @NotNull(message = "Publication date is mandatory")
        @PastOrPresent(message = "Publication date must be in the past or present")
        LocalDate publicationDate,

        @NotNull(message = "Copies available is mandatory")
        @Positive(message = "Copies available must be a positive number")
        Integer copiesAvailable
) {
}
