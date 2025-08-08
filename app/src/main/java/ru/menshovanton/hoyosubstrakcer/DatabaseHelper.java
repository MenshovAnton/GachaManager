package ru.menshovanton.hoyosubstrakcer;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gachamanager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "calendar";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_STATUSGENSHIN = "statusgenshin";
    public static final String COLUMN_DRGENSHIN = "drgenshin";
    public static final String COLUMN_STATUSHSR = "statushsr";
    public static final String COLUMN_DRHSR = "drhsr";
    public static final String COLUMN_STATUSZZZ = "statuszzz";
    public static final String COLUMN_DRZZZ = "drzzz";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY + " INTEGER, " +
                COLUMN_MONTH + " INTEGER, " +
                COLUMN_STATUSGENSHIN + " INTEGER, " +
                COLUMN_DRGENSHIN + " INTEGER, " +
                COLUMN_STATUSHSR + " INTEGER, " +
                COLUMN_DRHSR + " INTEGER, " +
                COLUMN_STATUSZZZ + " INTEGER, " +
                COLUMN_DRZZZ + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void updateValue(int id, int day, int month, int status, int subDaysRemaining) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_MONTH, month);

        switch (MainActivity.subType) {
            case 0:
                values.put(COLUMN_STATUSGENSHIN, status);
                values.put(COLUMN_DRGENSHIN, subDaysRemaining);
                break;
            case 1:
                values.put(COLUMN_STATUSHSR, status);
                values.put(COLUMN_DRHSR, subDaysRemaining);
                break;
            case 2:
                values.put(COLUMN_STATUSZZZ, status);
                values.put(COLUMN_DRZZZ, subDaysRemaining);
                break;
        }

        id++;

        if (isRecordExists(id)) {
            db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        } else {
            db.insert(TABLE_NAME, null, values);
        }
    }

    public Cursor getValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null,
                null, null, null, null, null);
    }

    public boolean isRecordExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT EXISTS(SELECT 1 FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) == 1;
        }

        cursor.close();
        return exists;
    }

    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT EXISTS(SELECT id FROM calendar WHERE day=\"1\") AS day, " +
                "EXISTS(SELECT id FROM calendar WHERE month=\"1\") AS month;";

        Cursor cursor = db.rawQuery(query, null);
        boolean isEmpty = true;

        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0;
        }

        cursor.close();
        //db.close();
        return isEmpty;
    }
}
