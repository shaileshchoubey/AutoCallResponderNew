package com.choubey.autocallreponder;

/**
 * Created by choubey on 7/11/15.
 */
public class EventBroadcastHandler {
/*    public static Activity parentActivity = null;
    private BroadcastReceiver callBlocker = new CustomBroadcastReceiver();
    private static EventBroadcastHandler eventBroadcastHandler = null;
    private IntentFilter filter = null;

    private EventBroadcastHandler(Activity parentActivity)
    {
        this.parentActivity = parentActivity;
    }

    public static EventBroadcastHandler createNewInstance(Activity parentActivity)
    {
        if(eventBroadcastHandler == null) {
            synchronized (EventBroadcastHandler.class) {
                if(eventBroadcastHandler == null) {
                    eventBroadcastHandler = new EventBroadcastHandler(parentActivity);
                }
            }
        }
        return eventBroadcastHandler;
    }

    public static EventBroadcastHandler getSingletonInstance()
    {
        if(eventBroadcastHandler == null)
        {
            throw new IllegalStateException("EventBroadcastHandler instance is not created yet! Please create an instance first.");
        }
        return eventBroadcastHandler;
    }

    public void registerBroadcastReceiver()
    {
        if(parentActivity != null) {
            filter = new IntentFilter("android.intent.action.PHONE_STATE");
            parentActivity.registerReceiver(callBlocker, filter);
        }
        else
        {
            throw new IllegalStateException("Parent activity is not set");
        }
    }

    public void unregisterBroadcastReceiver()
    {
        if (callBlocker != null && parentActivity != null)
        {
            parentActivity.unregisterReceiver(callBlocker);
            callBlocker = null;
        }
    }*/
}
