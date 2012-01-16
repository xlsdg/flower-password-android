package com.fpassword.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;

public class Helper {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static long getLongOnCursor(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static String getStringOnCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

}
