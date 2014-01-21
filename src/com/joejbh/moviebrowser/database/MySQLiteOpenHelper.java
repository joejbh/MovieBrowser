package com.joejbh.moviebrowser.database;

import com.joejbh.moviebrowser.database.MovieContract.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String BOOLEAN_TYPE = " BOOLEAN";
	private static final String COMMA_SEP = ",";	
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "movies_db";
	
	
	
	// Create and Delete Strings for the movies table	
	private static final String SQL_CREATE_MOVIES =
			"CREATE TABLE " + Movies.TABLE_NAME + " (" +
					Movies._ID + " INTEGER PRIMARY KEY," +
					Movies.NAME + TEXT_TYPE + COMMA_SEP +
					Movies.GENRE + TEXT_TYPE + COMMA_SEP +
					Movies.DESCRIPTION + TEXT_TYPE + COMMA_SEP +
					Movies.WIDE_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
					Movies.IMAGE_URL + TEXT_TYPE + COMMA_SEP +
					Movies.IS_FAVORITE + BOOLEAN_TYPE +
					" )";

	private static final String SQL_DELETE_MOVIES =
			"DROP TABLE IF EXISTS " + Movies.TABLE_NAME;
	
	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIES);
    }


	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_MOVIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }    
    
}

