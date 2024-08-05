package com.hsk.library_mgmt_backend.helper;

import com.hsk.library_mgmt_backend.dto.BookTransactionDto;
import com.hsk.library_mgmt_backend.mapper.BookTransactionMapper;
import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationUtil;
import com.hsk.library_mgmt_backend.response.ResponseUtil;
import com.hsk.library_mgmt_backend.service.BookTransactionService;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionRequest;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookTransactionHelper {
    private final BookTransactionService bookTransactionService;
    private final BookTransactionMapper bookTransactionMapper;

    public ResponseData<BookTransactionDto> addBookTransaction(BookTransactionRequest bookTransactionRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        BookTransaction income = bookTransactionService.addBookTransaction(bookTransactionRequest);
        return ResponseUtil.responseConverter(bookTransactionMapper.toDto(income), 201);
    }

    public ResponseData<BookTransactionDto> getBookTransactionById(Long id) {
        BookTransaction income = bookTransactionService.getBookTransactionById(id);
        return ResponseUtil.responseConverter(bookTransactionMapper.toDto(income));
    }

    public ResponsePaginationData<List<BookTransactionDto>> getAllBookTransactions(Pageable pageable,String query, @Valid String status, Long memberId) {
        Page<BookTransactionDto> bookTransactionDtoPage = bookTransactionService.getAllBookTransaction(pageable,query, status,memberId);
        return ResponsePaginationUtil.responsePaginationConverter(bookTransactionDtoPage.getContent(),
                bookTransactionDtoPage.getTotalElements(),
                bookTransactionDtoPage.getTotalPages(),
                bookTransactionDtoPage.getNumber());
    }

    public ResponseData<BookTransactionDto> updateBookTransactionById(Long id, BookTransactionUpdateRequest bookTransactionRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        BookTransaction bookTransaction = bookTransactionService.updateBookTransactionById(id, bookTransactionRequest);
        return ResponseUtil.responseConverter(bookTransactionMapper.toDto(bookTransaction));
    }

    public ResponseData<Object> deleteBookTransactionById(Long id) {
        bookTransactionService.deleteBookTransactionById(id);
        return ResponseUtil.responseConverter(null);
    }

    public ResponsePaginationData<List<BookTransactionDto>> getAllOverdueBookTransactions(Pageable pageable, Long memberId) {
        Page<BookTransactionDto> bookTransactionDtoPage = bookTransactionService.getAllOverdueBookTransactions(pageable,memberId);
        return ResponsePaginationUtil.responsePaginationConverter(bookTransactionDtoPage.getContent(),
                bookTransactionDtoPage.getTotalElements(),
                bookTransactionDtoPage.getTotalPages(),
                bookTransactionDtoPage.getNumber());
    }
}
