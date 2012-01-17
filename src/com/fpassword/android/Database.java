package com.fpassword.android;

import static com.fpassword.android.Helper.formatDate;
import static com.fpassword.android.Helper.getLongOnCursor;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {

    private static final String DATABASE_NAME = "fpassword.db";

    private static final int DATABASE_VERSION = 1;

    public static class Keys {

        private static final String TABLE_NAME = "keys";

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_USED_KEY = "used_key";

        public static final String COLUMN_LAST_USED = "last_used";

        private static final String CREATE_TABLE =
            "create table " + TABLE_NAME + " ( " +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_USED_KEY + " text unique, " +
                COLUMN_LAST_USED + " text " +
            ");";
    
        private static final String CREATE_INDEX_ON_LAST_USED =
            "create index index_" + TABLE_NAME + "_" + COLUMN_LAST_USED +
            " on " + TABLE_NAME + " ( " + COLUMN_LAST_USED + " desc );";

    }

    private final Context context;

    private SQLiteDatabase db;

    public Database(Context context) {
        this.context = context;
    }

    public void open() {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        if (db.getVersion() == 0) {
            createTables();
            db.setVersion(DATABASE_VERSION);
        }
    }

    private void createTables() {
        createTableKeys();
    }

    private void createTableKeys() {
        db.execSQL(Keys.CREATE_TABLE);
        db.execSQL(Keys.CREATE_INDEX_ON_LAST_USED);
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public void insertOrUpdateUsedKey(String usedKey) {
        ContentValues values = new ContentValues();
        values.put(Keys.COLUMN_USED_KEY, usedKey);
        values.put(Keys.COLUMN_LAST_USED, formatDate(new Date()));

        Cursor cursor = db.query(Keys.TABLE_NAME, new String[] { Keys.COLUMN_ID }, Keys.COLUMN_USED_KEY + " = ?",
                new String[] { usedKey }, null, null, null);
        if (cursor.moveToFirst()) {
            long id = getLongOnCursor(cursor, Keys.COLUMN_ID);
            db.update(Keys.TABLE_NAME, values, Keys.COLUMN_ID + " = " + id, null);
        } else {
            db.insert(Keys.TABLE_NAME, null, values);
        }
    }

    public Cursor queryUsedKeys(String keyPrefix) {
        String selection = null;
        String[] selectionArgs = null;
        if (keyPrefix != null && keyPrefix.length() > 0) {
            selection = Keys.COLUMN_USED_KEY + " like ? || '%'";
            selectionArgs = new String[] { keyPrefix };
        }
        return db.query(Keys.TABLE_NAME, new String[] { Keys.COLUMN_ID, Keys.COLUMN_USED_KEY }, selection,
                selectionArgs, null, null, Keys.COLUMN_LAST_USED + " desc", "10");
    }

    public void deleteKeys() {
        db.delete(Keys.TABLE_NAME, null, null);
    }

}
