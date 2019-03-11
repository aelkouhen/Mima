package com.carhub.api.auth.utils.exception;

public class ElementNotUpdatedException extends RuntimeException {

    private Class<? extends Object> element;

    public ElementNotUpdatedException(Class<? extends Object> element){
        this.element = element;
    }

    @Override
    public String getMessage() {
        return "No element of type " + element.getSimpleName() + " was updated";
    }
}
