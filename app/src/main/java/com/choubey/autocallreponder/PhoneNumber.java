package com.choubey.autocallreponder;

import android.telephony.PhoneNumberUtils;
import android.util.Log;

/**
 * Created by choubey on 12/13/15.
 */
public class PhoneNumber {
    private final String number;

    public PhoneNumber(String number)
    {
        this.number = number;
    }

    @Override
    public boolean equals(Object phoneNumber)
    {
        if(phoneNumber == null)
        {
            return false;
        }
        PhoneNumber otherNumber = (PhoneNumber)phoneNumber;
        Log.i(this.getClass().getSimpleName(), "Matching " + otherNumber.getNumber() + " and " + this.getNumber());
        return PhoneNumberUtils.compare(otherNumber.getNumber(), this.getNumber());
    }

    @Override
    public int hashCode()
    {
        int prime = 31;
        int hashCode = 1;
        hashCode = hashCode*prime + (this.getNumber() != null ? this.getNumber().hashCode() : 0);
        return hashCode;
    }

    public String getNumber()
    {
        return number;
    }
}