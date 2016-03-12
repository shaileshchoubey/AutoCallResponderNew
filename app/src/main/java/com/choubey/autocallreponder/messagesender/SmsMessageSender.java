package com.choubey.autocallreponder.messagesender;

import android.telephony.SmsManager;

import com.choubey.autocallreponder.Utils;

/**
 * Created by choubey on 7/11/15.
 */
public class SmsMessageSender implements MessageSender {

    @Override
    public void sendMessage(MessageDetails messageDetails) {
        SmsManager smsManager = SmsManager.getDefault();
        Utils.validateNotNull(messageDetails.getMessage(), "Message is null.");
        Utils.validateNotNull(messageDetails.getPhoneNumber(), "Phone number is null.");
        smsManager.sendTextMessage(messageDetails.getPhoneNumber(), null, messageDetails.getMessage(), null, null);
    }
}


