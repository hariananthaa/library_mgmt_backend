package com.hsk.library_mgmt_backend.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.hsk.library_mgmt_backend.persistent.entity.Book}
 */
public record BookDto(Long id,

                      LocalDateTime createdAt,

                      String createdBy,

                      LocalDateTime updatedAt,

                      String updatedBy,
                      String title, String author, String isbn, String genre,

                      LocalDate publicationDate,

                      int copiesAvailable) implements Serializable {
}