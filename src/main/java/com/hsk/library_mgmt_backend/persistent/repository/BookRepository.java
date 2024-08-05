package com.hsk.library_mgmt_backend.persistent.repository;

import com.hsk.library_mgmt_backend.persistent.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select i from Book i where i.isbn = :isbn")
    Book findByIsbn(String isbn);

    @Query("SELECT COUNT(b) FROM Book b")
    long countTotalBooks();
}
