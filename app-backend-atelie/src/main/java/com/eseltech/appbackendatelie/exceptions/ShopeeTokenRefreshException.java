package com.eseltech.appbackendatelie.exceptions;

public class ShopeeTokenRefreshException extends RuntimeException {
    
    public ShopeeTokenRefreshException(String message) {
        super(message);
    }
    
    public ShopeeTokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }
}

