package com.hsk.library_mgmt_backend.response;

public record MemberDashboardCountResponse(
        long totalBooks,
        long totalRequestedBooks,
        long totalBorrowedBooks,
        long totalOverdueBooks
) {
}
