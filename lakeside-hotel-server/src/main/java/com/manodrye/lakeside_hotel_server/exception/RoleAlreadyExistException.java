package com.manodrye.lakeside_hotel_server.exception;

public class RoleAlreadyExistException extends RuntimeException {

    public RoleAlreadyExistException(String message) {
        super(message);
    }
}
