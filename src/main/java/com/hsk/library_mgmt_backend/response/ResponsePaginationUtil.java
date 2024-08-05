package com.hsk.library_mgmt_backend.response;

import org.springframework.http.HttpStatus;

public class ResponsePaginationUtil {

    public static <T> ResponsePaginationData<T> responsePaginationConverter(T object, Long totalElements, int totalPages, int pageNumber) {

        ResponsePaginationData<T> responsePaginationData = new ResponsePaginationData<>();
        responsePaginationData.setData(object);
        responsePaginationData.setTotalElements(totalElements);
        responsePaginationData.setTotalPages(totalPages);
        responsePaginationData.setPageNumber(pageNumber);
        responsePaginationData.setStatus(HttpStatus.OK.value());
        responsePaginationData.setResult(true);
        return responsePaginationData;
    }

}
