package com.chatApplication.Authentication.exceptions;

public class TokenInvalidOrExpiredException extends RuntimeException{
    public TokenInvalidOrExpiredException(String message){
        super(message);
    }
}
