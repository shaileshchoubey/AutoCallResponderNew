package com.choubey.autocallreponder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by choubey on 6/28/15.
 */
public class TemplatesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AutoCallResponder.UserTemplates.db";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL,%s TEXT NOT NULL, %s CHAR(1) NOT NULL)", UserTemplatesData.UserTemplates.TABLE_NAME,
                    UserTemplatesData.UserTemplates.COLUMN_NAME_TEMPLATE_ID, UserTemplatesData.UserTemplates.COLUMN_NAME_CONTACT_NUMBER,
                    UserTemplatesData.UserTemplates.COLUMN_NAME_CONTACT_NAME,
                    UserTemplatesData.UserTemplates.COLUMN_NAME_MESSAGE, UserTemplatesData.UserTemplates.COLUMN_NAME_ACTIVE);
            ;
            /*"CREATE TABLE " + UserTemplatesData.UserTemplates.TABLE_NAME + " (" +
                    UserTemplatesData.UserTemplates.COLUMN_NAME_TEMPLATE_ID + " INTEGER PRIMARY KEY," +
                    UserTemplatesData.UserTemplates.COLUMN_NAME_CONTACT_NUMBER + TEXT_TYPE + COMMA_SEP +
                    UserTemplatesData.UserTemplates.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    UserTemplatesData.UserTemplates.COLUMN_NAME_ACTIVE + TEXT_TYPE +
            " )";*/
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UserTemplatesData.UserTemplates.TABLE_NAME;

    /**
     * @param context
     */
    public TemplatesDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}