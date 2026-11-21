package com.gcc.communityHall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class SlotOccupiedException extends RuntimeException {

    public SlotOccupiedException(String msg){
        super(msg);

    }
}
