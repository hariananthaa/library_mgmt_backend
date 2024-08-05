package com.hsk.library_mgmt_backend.service;


import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.persistent.entity.Book;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Book addBook(Book book);

    Book updateBookById(Long id, Book book);

    void deleteBookById(Long id);

    Book getBookById(Long id);

    Page<BookDto> getAllBook(Pageable pageable, String query, String genre);

    List<Book> addBookBulky(List<Book> entity);
}
