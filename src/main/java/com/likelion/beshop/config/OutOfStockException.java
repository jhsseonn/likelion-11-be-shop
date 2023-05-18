package com.likelion.beshop.constant;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String msg) { //오류 메시지를 받는다
        super(msg); //오류시 해당 메시지를 포함하여 오류 던짐
    }
}