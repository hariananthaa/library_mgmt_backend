package com.hsk.library_mgmt_backend.helper;

import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.mapper.BookMapper;
import com.hsk.library_mgmt_backend.persistent.entity.Book;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationUtil;
import com.hsk.library_mgmt_backend.response.ResponseUtil;
import com.hsk.library_mgmt_backend.service.BookService;
import com.hsk.library_mgmt_backend.web.v1.payload.book.BookRequest;
import com.hsk.library_mgmt_backend.web.v1.payload.book.BulkBookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookHelper {
    private final BookService bookService;
    private final BookMapper bookMapper;

    public ResponseData<BookDto> addBook(BookRequest bookRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        Book income = bookService.addBook(bookMapper.toEntity(bookRequest));
        return ResponseUtil.responseConverter(bookMapper.toDto(income), 201);
    }

    public ResponseData<BookDto> getBookById(Long id) {
        Book income = bookService.getBookById(id);
        return ResponseUtil.responseConverter(bookMapper.toDto(income));
    }

    public ResponsePaginationData<List<BookDto>> getAllBooks(Pageable pageable, @Valid String query, String genre) {
        Page<BookDto> bookDtoPage = bookService.getAllBook(pageable, query,genre);
        return ResponsePaginationUtil.responsePaginationConverter(bookDtoPage.getContent(),
                bookDtoPage.getTotalElements(),
                bookDtoPage.getTotalPages(),
                bookDtoPage.getNumber());
    }

    public ResponseData<BookDto> updateBookById(Long id, BookRequest bookRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        Book book = bookService.updateBookById(id, bookMapper.toEntity(bookRequest));
        return ResponseUtil.responseConverter(bookMapper.toDto(book));
    }

    public ResponseData<Object> deleteBookById(Long id) {
        bookService.deleteBookById(id);
        return ResponseUtil.responseConverter(null);
    }

    public ResponseData<List<BookDto>> addBookBulky(BulkBookRequest request, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        List<Book> income = bookService.addBookBulky(bookMapper.toEntity(request.bookRequestList()));
        return ResponseUtil.responseConverter(bookMapper.toDto(income), 201);
    }
}
