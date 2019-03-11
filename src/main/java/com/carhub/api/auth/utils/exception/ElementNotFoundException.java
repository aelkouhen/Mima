package com.carhub.api.auth.utils.exception;

public class ElementNotFoundException extends RuntimeException {

    private Class<? extends Object> element;

    public ElementNotFoundException(Class<? extends Object> element){
        this.element = element;
    }

    @Override
    public String getMessage() {
        return "No element of type " + element.getSimpleName() + " was found";
    }
}
