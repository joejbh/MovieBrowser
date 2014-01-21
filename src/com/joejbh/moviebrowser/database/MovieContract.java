package com.joejbh.moviebrowser.database;

import android.provider.BaseColumns;

public final class MovieContract {
    
	public static final int LOAD_ERROR_URI_ID = -1;
	
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MovieContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Movies implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        
        public static final String NAME = "moviename";
        public static final String DESCRIPTION = "moviedescription";
        public static final String GENRE = "genre";
        public static final String IMAGE_URL = "imageurl";
        public static final String WIDE_IMAGE_URL = "wideimageurl";
        public static final String IS_FAVORITE = "isfavorite";
    }
    
}
