package com.choubey.autocallreponder.db;

import android.provider.BaseColumns;

/**
 * Created by choubey on 6/28/15.
 */
public final class UserTemplatesData {
    private String templateId;
    private String contactNumber;
    private String contactName;
    private String message;
    private ActiveStatus status;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ActiveStatus getStatus() {
        return status;
    }

    public void setStatus(ActiveStatus status) {
        this.status = status;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public static class UserTemplates implements BaseColumns
    {
        public static final String TABLE_NAME = "USER_TEMPLATES";
        public static final String COLUMN_NAME_TEMPLATE_ID = "TEMPLATE_ID";
        public static final String COLUMN_NAME_CONTACT_NUMBER = "CONTACT_NUMBER";
        public static final String COLUMN_NAME_CONTACT_NAME = "CONTACT_NAME";
        public static final String COLUMN_NAME_MESSAGE = "MESSAGE";
        public static final String COLUMN_NAME_ACTIVE = "ACTIVE";
    }

    public static enum ActiveStatus
    {
        Y,
        N
    }

    @Override
    public String toString()
    {
        StringBuilder toString = new StringBuilder();
        toString.append("templateId:" + templateId + " ");
        toString.append("contactNumber:" + contactNumber + " ");
        toString.append("contactName:" + contactName + " ");
        toString.append("message:" + message + " ");
        toString.append("status:" + status);
        return String.valueOf(toString);
    }
}
