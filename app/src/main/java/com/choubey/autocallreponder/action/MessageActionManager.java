package com.choubey.autocallreponder.action;

import android.content.Context;
import android.util.Log;

import com.choubey.autocallreponder.UserContext;
import com.choubey.autocallreponder.messagesender.MessageDetails;
import com.choubey.autocallreponder.messagesender.MessageSender;
import com.choubey.autocallreponder.messagesender.SmsMessageSender;

/**
 * Created by choubey on 7/12/15.
 */
public class MessageActionManager implements ActionManager {
    private MessageSender messageSender = new SmsMessageSender();

    @Override
    public void takeAction(UserContext userContext, Context context) {
        MessageDetails messageDetails = new MessageDetails();
        messageDetails.setMessage(userContext.getMessage());
        messageDetails.setPhoneNumber(userContext.getNumber());
        messageSender.sendMessage(messageDetails);
        Log.i(this.getClass().getSimpleName(), "Message successfully sent with details = " + String.valueOf(messageDetails));
    }
}
