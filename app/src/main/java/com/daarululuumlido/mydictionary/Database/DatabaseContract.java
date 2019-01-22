package com.daarululuumlido.mydictionary.Database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private DatabaseContract(){}

    public static final String TABLE_NAME_INDOINGGRIS = "table_indoinggris";
    public static final String TABLE_NAME_INGGRISINDO = "table_inggrisindo";
    public static final String DATABASE_NAME = "dbkamusku";
    public static final int DATABASE_VERSION = 1;

    static final class DictionaryColumns implements BaseColumns {
        static String WORD = "WORD";
        static String TRANSLATE = "TRANSLATE";
    }
}
