package com.carhub.api.auth.utils.exception;

public class ElementNotDeletedException extends RuntimeException {

    private Class<? extends Object> element;

    public ElementNotDeletedException(Class<? extends Object> element){
        this.element = element;
    }

    @Override
    public String getMessage() {
        return "No element of type " + element.getSimpleName() + " was deleted";
    }
}
