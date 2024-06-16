package com.manodrye.lakeside_hotel_server.exception;

public class InternalServerException extends RuntimeException{

    public InternalServerException(String message){
        super(message);
    }
}
