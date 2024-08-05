package com.hsk.library_mgmt_backend.response;

public record AdminDashboardCountResponse(
        long totalBooks,
        long totalBorrowedBooks,
        long totalOverdueBooks,
        long totalRequestedBooks,
        long totalMembers
) {
}
