package com.hsk.library_mgmt_backend.web.v1.payload.book;

import java.util.List;

public record BulkBookRequest(
        List<BookRequest> bookRequestList
) {
}
