package com.fpassword.core;

public class EncryptionException extends Exception {

    private static final long serialVersionUID = 1L;

    public EncryptionException() {
    }

    public EncryptionException(String detailMessage) {
        super(detailMessage);
    }

    public EncryptionException(Throwable throwable) {
        super(throwable);
    }

    public EncryptionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
