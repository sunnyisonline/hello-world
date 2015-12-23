package com.ezdi.audioplayer.audioResponse;

/**
 * Created by sunny.k on 10/20/2015.
 */
public class Header {
    private boolean success;
    private int code;
    private String message;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
