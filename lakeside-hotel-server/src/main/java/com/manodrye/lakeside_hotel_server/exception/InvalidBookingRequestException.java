package com.manodrye.lakeside_hotel_server.exception;

public class InvalidBookingRequestException extends RuntimeException{

    public InvalidBookingRequestException(String message){
        super(message);
    }
}
