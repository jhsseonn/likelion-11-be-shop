package com.likelion.beshop.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String msg) {
        super(msg);
    }
}