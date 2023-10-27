package org.service.topsquad.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceValidateException extends Exception{

    private static final long serialVersionUID = 1L;

    public ResourceValidateException(String message){
        super(message);
    }
}