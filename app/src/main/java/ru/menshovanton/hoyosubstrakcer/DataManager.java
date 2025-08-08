package ru.menshovanton.hoyosubstrakcer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataManager {
    static void writeDB(Context context, Date[] dateArray, int start, int stop) {
        for (int i = start; i < stop + start; i++) {
            MainActivity.dbHelper.updateValue(i, dateArray[i].dayOfMonth, dateArray[i].month, dateArray[i].status, dateArray[i].subDaysRemaining);
        }
    }

    public static Date[] readDB(Context context) {
        Cursor cursor = MainActivity.dbHelper.getValue();

        if (MainActivity.dbHelper.isDatabaseEmpty()) {
            return null;
        }

        Date[] array = new Date[365];

        for (int i = 0; i <= 363; i++) {
            array[i] = new Date(
                    i,
                    getDayById(i, cursor),
                    getStatusById(i, cursor),
                    getSubDaysRemainingById(i, cursor),
                    getMonthById(i, cursor));
        }

        cursor.close();
        return array;
    }

    @SuppressLint("Range")
    public static int getDayById(int id, Cursor cursor) {
        int day = 1;

        if (cursor.moveToFirst()) {
            cursor.moveToPosition(id);
            day = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DAY));
        }

        return day;
    }

    @SuppressLint("Range")
    public static int getMonthById(int id, Cursor cursor) {
        int month = 1;

        if (cursor.moveToFirst()) {
            cursor.moveToPosition(id);
            month = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_MONTH));
        }

        return month;
    }

    @SuppressLint("Range")
    public static int getStatusById(int id, Cursor cursor) {
        int status = 0;

        if (cursor.moveToFirst()) {
            cursor.moveToPosition(id);

            switch (MainActivity.subType) {
                case 0:
                    status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUSGENSHIN));
                    break;
                case 1:
                    status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUSHSR));
                    break;
                case 2:
                    status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUSZZZ));
                    break;
            }
        }

        return status;
    }

    @SuppressLint("Range")
    public static int getSubDaysRemainingById(int id, Cursor cursor) {
        int days = 0;

        if (cursor.moveToFirst()) {
            cursor.moveToPosition(id);

            switch (MainActivity.subType) {
                case 0:
                    days = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DRGENSHIN));
                    break;
                case 1:
                    days = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DRHSR));
                    break;
                case 2:
                    days = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DRZZZ));
                    break;
            }
        }

        return days;
    }

    private static class DataItems {
        private Date[] dates;

        Date[] getDates() {
            return dates;
        }
        void setDates(Date[] dates) {
            this.dates = dates;
        }
    }
}
