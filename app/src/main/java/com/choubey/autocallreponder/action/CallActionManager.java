package com.choubey.autocallreponder.action;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.choubey.autocallreponder.UserContext;

import java.lang.reflect.Method;

/**
 * Created by choubey on 7/12/15.
 */
public class CallActionManager implements ActionManager{
    private TelephonyManager telephonyManager;
    private ITelephony telephonyService;
    private static final long DURATION_OF_RING_REQUIRED = 2000;

    @Override
    public void takeAction(UserContext userContext, Context context) {
        telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        Class c = null;
        try {
            c = Class.forName(telephonyManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "Error loading class TelephonyManager", e);
        }

        Method m = null;
        try {
            m = c.getDeclaredMethod("getITelephony");
        } catch (SecurityException| NoSuchMethodException  e) {
            Log.e(this.getClass().getSimpleName(), "Error loading method getITelephony for class TelephonyManager", e);
        }

        m.setAccessible(true);
        try {
            telephonyService = (ITelephony)m.invoke(telephonyManager);
            telephonyService.silenceRinger();
            Thread.sleep(DURATION_OF_RING_REQUIRED);
            telephonyService.endCall();
            Log.i(this.getClass().getSimpleName(), "Incoming call from number = " + userContext.getNumber() + " ended.");
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Error invoking getITelephony method", e);
        }
    }
}
