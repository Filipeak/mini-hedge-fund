package com.psio.market;

public class ValueBelowZeroException extends Exception{
    public ValueBelowZeroException(String message){
        super(message);
    }
}
