package com.hsk.library_mgmt_backend.response;

import org.springframework.http.HttpStatus;


public class ResponseUtil {

    public static <T> ResponseData<T> responseConverter(T object) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setData(object);
        responseData.setStatus(HttpStatus.OK.value());
        responseData.setResult(true);
        return responseData;
    }

    public static <T> ResponseData<T> responseConverter(T object, int status) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setData(object);
        responseData.setStatus(status);
        responseData.setResult(true);
        return responseData;
    }

}
