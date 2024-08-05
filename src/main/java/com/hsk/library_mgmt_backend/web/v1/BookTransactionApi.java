package com.hsk.library_mgmt_backend.web.v1;

import com.hsk.library_mgmt_backend.dto.BookTransactionDto;
import com.hsk.library_mgmt_backend.helper.BookTransactionHelper;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationData;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionRequest;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing book transactions in the library system.
 */
@RestController
@RequestMapping("/api/v1/book-transaction")
@SecurityRequirement(name = "bearerAuth")
public class BookTransactionApi {

    private final BookTransactionHelper bookTransactionHelper;

    @Autowired
    public BookTransactionApi(BookTransactionHelper bookTransactionHelper) {
        this.bookTransactionHelper = bookTransactionHelper;
    }

    /**
     * Adds a new book transaction.
     *
     * @param request         the request containing details of the book transaction to be added
     * @param bindingResult  the result of the validation
     * @return a response containing the added book transaction
     */
    @PostMapping
    @Operation(summary = "Add a new book transaction", tags = "BookTransaction")
    @PreAuthorize("hasAnyAuthority('member:create')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book transaction successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseData<BookTransactionDto> addBookTransaction(@Valid @RequestBody BookTransactionRequest request,
                                                               BindingResult bindingResult) {
        return bookTransactionHelper.addBookTransaction(request, bindingResult);
    }

    /**
     * Retrieves a book transaction by its ID.
     *
     * @param id the ID of the book transaction to be retrieved
     * @return a response containing the book transaction details
     */
    @GetMapping("/{bookTransactionId}")
    @Operation(summary = "Get book transaction by ID", tags = "BookTransaction")
    @PreAuthorize("hasAnyAuthority('admin:read','member:read')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book transaction retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book transaction not found")
    })
    public ResponseData<BookTransactionDto> getBookTransactionById(@PathVariable("bookTransactionId") Long id) {
        return bookTransactionHelper.getBookTransactionById(id);
    }

    /**
     * Retrieves all book transactions with optional search and pagination.
     *
     * @param query        the search query to filter book transactions
     * @param status       the status to filter book transactions by
     * @param memberId     the ID of the member whose transactions to filter
     * @param pageNumber   the page number to retrieve
     * @param pageSize     the number of items per page
     * @return a response containing a paginated list of book transactions
     */
    @GetMapping("")
    @Operation(summary = "Get all book transactions", tags = "BookTransaction")
    @PreAuthorize("hasAnyAuthority('admin:read','member:read')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book transactions retrieved successfully")
    })
    public ResponsePaginationData<List<BookTransactionDto>> getAllBookTransactions(
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "memberId", defaultValue = "", required = false) Long memberId,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bookTransactionHelper.getAllBookTransactions(pageable, query, status, memberId);
    }

    /**
     * Retrieves all overdue book transactions for a member.
     *
     * @param memberId     the ID of the member whose overdue transactions to retrieve
     * @param pageNumber   the page number to retrieve
     * @param pageSize     the number of items per page
     * @return a response containing a paginated list of overdue book transactions
     */
    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue book transactions", tags = "BookTransaction")
    @PreAuthorize("hasAnyAuthority('member:read')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue book transactions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponsePaginationData<List<BookTransactionDto>> getAllOverdueBookTransactions(
            @RequestParam(value = "memberId", defaultValue = "", required = false) Long memberId,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bookTransactionHelper.getAllOverdueBookTransactions(pageable, memberId);
    }

    /**
     * Updates the details of a book transaction by its ID.
     *
     * @param id            the ID of the book transaction to be updated
     * @param request       the request containing updated book transaction details
     * @param bindingResult the result of the validation
     * @return a response containing the updated book transaction
     */
    @PutMapping("/{bookTransactionId}")
    @Operation(summary = "Update book transaction by ID", tags = "BookTransaction")
    @PreAuthorize("hasAnyAuthority('admin:update','member:update')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book transaction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Book transaction not found")
    })
    public ResponseData<BookTransactionDto> updateBookTransactionById(@PathVariable("bookTransactionId") Long id,
                                                                      @Valid @RequestBody BookTransactionUpdateRequest request,
                                                                      BindingResult bindingResult) {
        return bookTransactionHelper.updateBookTransactionById(id, request, bindingResult);
    }

    /**
     * Deletes a book transaction by its ID.
     *
     * @param id the ID of the book transaction to be deleted
     * @return a response confirming the deletion
     */
    @DeleteMapping("/{bookTransactionId}")
    @Operation(summary = "Delete book transaction by ID", tags = "BookTransaction")
    @PreAuthorize("hasAnyAuthority('admin:delete')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book transaction not found")
    })
    public ResponseData<Object> deleteBookTransactionById(@PathVariable("bookTransactionId") Long id) {
        return bookTransactionHelper.deleteBookTransactionById(id);
    }
}
