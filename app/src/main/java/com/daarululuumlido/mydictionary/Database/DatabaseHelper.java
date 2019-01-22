package com.daarululuumlido.mydictionary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.DATABASE_NAME;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.DATABASE_VERSION;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.DictionaryColumns.TRANSLATE;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.DictionaryColumns.WORD;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.TABLE_NAME_INDOINGGRIS;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.TABLE_NAME_INGGRISINDO;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private  static String CREATE_TABLE_INDOINGGRIS = "create table " + TABLE_NAME_INDOINGGRIS +
            " (" + _ID + " integer primary key autoincrement, " +
            WORD + " text not null, " +
            TRANSLATE + " text not null);";

    private  static String CREATE_TABLE_NAME_INGGRISINDO = "create table " + TABLE_NAME_INGGRISINDO +
            " (" + _ID + " integer primary key autoincrement, " +
            WORD + " text not null, " +
            TRANSLATE + " text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INDOINGGRIS);
        db.execSQL(CREATE_TABLE_NAME_INGGRISINDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INGGRISINDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INDOINGGRIS);
        onCreate(db);
    }
}
