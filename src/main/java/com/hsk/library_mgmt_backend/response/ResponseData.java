package com.hsk.library_mgmt_backend.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class ResponseData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -1345641020442183676L;
    private int status = 200;
    private T data;
    private String message = "";
    private Boolean result;
}

