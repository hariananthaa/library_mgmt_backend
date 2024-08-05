package com.hsk.library_mgmt_backend.web.v1;

import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.helper.BookHelper;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationData;
import com.hsk.library_mgmt_backend.web.v1.payload.book.BookRequest;
import com.hsk.library_mgmt_backend.web.v1.payload.book.BulkBookRequest;
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
 * REST controller for managing books in the library system.
 */
@RestController
@RequestMapping("/api/v1/book")
@SecurityRequirement(name = "bearerAuth")
public class BookApi {

    private final BookHelper bookHelper;

    @Autowired
    public BookApi(BookHelper bookHelper) {
        this.bookHelper = bookHelper;
    }

    /**
     * Adds a list of books in bulk.
     *
     * @param request         the request containing details of books to be added
     * @param bindingResult  the result of the validation
     * @return a response containing the list of added books
     */
    @PostMapping("/bulk")
    @Operation(summary = "Add multiple books", tags = "Book")
    @PreAuthorize("hasAuthority('admin:create')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Books successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseData<List<BookDto>> addBookBulk(@Valid @RequestBody BulkBookRequest request,
                                                   BindingResult bindingResult) {
        return bookHelper.addBookBulky(request, bindingResult);
    }

    /**
     * Adds a single book.
     *
     * @param request         the request containing details of the book to be added
     * @param bindingResult  the result of the validation
     * @return a response containing the added book
     */
    @PostMapping
    @Operation(summary = "Add a single book", tags = "Book")
    @PreAuthorize("hasAuthority('admin:create')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseData<BookDto> addBook(@Valid @RequestBody BookRequest request,
                                         BindingResult bindingResult) {
        return bookHelper.addBook(request, bindingResult);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to be retrieved
     * @return a response containing the book details
     */
    @GetMapping("/{bookId}")
    @Operation(summary = "Get book by ID", tags = "Book")
    @PreAuthorize("hasAnyAuthority('admin:read','member:read')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseData<BookDto> getBookById(@PathVariable("bookId") Long id) {
        return bookHelper.getBookById(id);
    }

    /**
     * Retrieves all books with optional search and pagination.
     *
     * @param query        the search query to filter books
     * @param genre        the genre to filter books by
     * @param pageNumber   the page number to retrieve
     * @param pageSize     the number of items per page
     * @return a response containing a paginated list of books
     */
    @GetMapping("")
    @Operation(summary = "Get all books", tags = "Book")
    @PreAuthorize("hasAnyAuthority('admin:read','member:read')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    public ResponsePaginationData<List<BookDto>> getAllBooks(
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @RequestParam(value = "genre", defaultValue = "", required = false) String genre,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "2", required = false) Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bookHelper.getAllBooks(pageable, query, genre);
    }

    /**
     * Updates the details of a book by its ID.
     *
     * @param id            the ID of the book to be updated
     * @param request       the request containing updated book details
     * @param bindingResult the result of the validation
     * @return a response containing the updated book
     */
    @PutMapping("/{bookId}")
    @Operation(summary = "Update book by ID", tags = "Book")
    @PreAuthorize("hasAuthority('admin:update')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseData<BookDto> updateBookById(@PathVariable("bookId") Long id,
                                                @Valid @RequestBody BookRequest request,
                                                BindingResult bindingResult) {
        return bookHelper.updateBookById(id, request, bindingResult);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to be deleted
     * @return a response confirming the deletion
     */
    @DeleteMapping("/{bookId}")
    @Operation(summary = "Delete book by ID", tags = "Book")
    @PreAuthorize("hasAuthority('admin:delete')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseData<Object> deleteBookById(@PathVariable("bookId") Long id) {
        return bookHelper.deleteBookById(id);
    }
}
