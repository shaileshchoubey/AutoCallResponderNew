package com.choubey.autocallreponder.messagesender;

/**
 * Created by choubey on 7/11/15.
 */
public class MessageDetails {
    private String message;
    private String phoneNumber;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString()
    {
        return "message:" + message + " " +
                "phoneNumber:" + phoneNumber;
    }
}
