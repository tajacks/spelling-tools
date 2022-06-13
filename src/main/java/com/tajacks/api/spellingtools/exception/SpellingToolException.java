package com.tajacks.api.spellingtools.exception;

public abstract class SpellingToolException extends RuntimeException {
    protected SpellingToolException(String msg) {
        super(msg);
    }
}
