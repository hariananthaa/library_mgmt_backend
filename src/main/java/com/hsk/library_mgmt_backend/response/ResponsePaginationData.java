package com.hsk.library_mgmt_backend.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class ResponsePaginationData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2939255806215918577L;

    private int status = 200;
    private T data;
    private String message = "";
    private Boolean result;

    private int totalPages;

    private Long totalElements;

    private int pageNumber;
}

