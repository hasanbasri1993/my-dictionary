package com.daarululuumlido.mydictionary.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.daarululuumlido.mydictionary.Model.DictionaryModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.DictionaryColumns.TRANSLATE;
import static com.daarululuumlido.mydictionary.Database.DatabaseContract.DictionaryColumns.WORD;


public class DictionaryHelper {

    private static String TABLE_NAME_INDOINGGRIS = DatabaseContract.TABLE_NAME_INDOINGGRIS;
    private static String TABLE_NAME_INGGRISINDO = DatabaseContract.TABLE_NAME_INGGRISINDO;

    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public DictionaryHelper(Context context) {
        this.context = context;
    }


    public void insertTransaction(DictionaryModel dictionaryModel, boolean isIndo) {
        String TABLE = isIndo ? TABLE_NAME_INDOINGGRIS : TABLE_NAME_INGGRISINDO;

        String sql = "INSERT INTO " + TABLE + " (" + WORD + ", " + TRANSLATE
                + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, dictionaryModel.getWord());
        stmt.bindString(2, dictionaryModel.getTranslate());
        stmt.execute();
        stmt.clearBindings();

    }

    public ArrayList<DictionaryModel> getDataByWord(boolean isIndo, String word) {
        String TABLE = isIndo ? TABLE_NAME_INDOINGGRIS : TABLE_NAME_INGGRISINDO;
        Cursor cursor = database.query(TABLE, null, WORD + " LIKE ?", new String[]{"%" + word + "%"}, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<DictionaryModel> arrayList = new ArrayList<>();
        DictionaryModel dictionaryModel;
        if (cursor.getCount() > 0) {
            do {
                dictionaryModel = new DictionaryModel();
                dictionaryModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                dictionaryModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(WORD)));
                dictionaryModel.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(TRANSLATE)));

                arrayList.add(dictionaryModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<DictionaryModel> getAllData(boolean isIndo) {
        String TABLE = isIndo ? TABLE_NAME_INDOINGGRIS : TABLE_NAME_INGGRISINDO;
        Cursor cursor = database.query(TABLE, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<DictionaryModel> arrayList = new ArrayList<>();
        DictionaryModel dictionaryModel;
        if (cursor.getCount() > 0) {
            do {
                dictionaryModel = new DictionaryModel();
                dictionaryModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                dictionaryModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(WORD)));
                dictionaryModel.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(TRANSLATE)));
                arrayList.add(dictionaryModel);
                cursor.moveToNext();


            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public DictionaryHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }


}
