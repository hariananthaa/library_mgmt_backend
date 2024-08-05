package com.hsk.library_mgmt_backend.service;


import com.hsk.library_mgmt_backend.dto.BookTransactionDto;
import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionRequest;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookTransactionService {
    BookTransaction addBookTransaction(BookTransactionRequest bookTransactionRequest);

    BookTransaction updateBookTransactionById(Long id, BookTransactionUpdateRequest bookTransactionRequest);

    void deleteBookTransactionById(Long id);

    BookTransaction getBookTransactionById(Long id);

    Page<BookTransactionDto> getAllBookTransaction(Pageable pageable,String query, String status, Long memberId);

    Page<BookTransactionDto> getAllOverdueBookTransactions(Pageable pageable, Long memberId);
}
