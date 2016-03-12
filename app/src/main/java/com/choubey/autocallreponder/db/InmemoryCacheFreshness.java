package com.choubey.autocallreponder.db;

/**
 * Created by choubey on 7/12/15.
 */
public class InmemoryCacheFreshness {
    private static boolean inmemoryCacheDirty = false;

    public static synchronized void setCacheFresh(boolean newStatus)
    {
        inmemoryCacheDirty = newStatus;
    }

    public static boolean isCacheFresh()
    {
        return inmemoryCacheDirty;
    }
}
