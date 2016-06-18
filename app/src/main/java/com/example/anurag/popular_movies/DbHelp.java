package com.example.anurag.popular_movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelp extends SQLiteOpenHelper {
    
    

    private static final String TEXT_TYPE = " TEXT";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
    
            "CREATE TABLE " + DatabaseConstants.TABLE_NAME + " (" +
                    DatabaseConstants.MOVIE_ID +  TEXT_TYPE + PRIMARY_KEY +  COMMA_SEP +
                    DatabaseConstants.MOVIE_TITLE + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.MOVIE_OVERVIEW +  TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.MOVIE_POSTER + TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.MOVIE_RATING +  TEXT_TYPE + COMMA_SEP +
                    DatabaseConstants.MOVIE_RELEASEDATE
                    + TEXT_TYPE +
                    " )";


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_NAME;

    public DbHelp(Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }



    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}