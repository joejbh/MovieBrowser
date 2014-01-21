package com.joejbh.moviebrowser;


import com.joejbh.moviebrowser.database.MySQLiteOpenHelper;
import com.joejbh.moviebrowser.database.MovieContract.Movies;
import com.joejbh.sourcecode.ImageDownloader;
import com.joejbh.sourcecode.MyDisplayCode;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityViewMovieDetails extends Activity {

	ImageView iViewMovieImage;
	TextView tViewMovieName, tViewMovieDescription;
	
	ImageView iViewIsFavorite, iViewShare;
	
	int movieId;
	
	ImageDownloader imageDownloader;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_movie_details);
		
		iViewMovieImage = (ImageView) findViewById(R.id.iViewMovieImage);
		tViewMovieName = (TextView) findViewById(R.id.tViewMovieName);
		tViewMovieDescription = (TextView) findViewById(R.id.tViewMovieDescription);
		iViewIsFavorite = (ImageView) findViewById(R.id.iViewIsFavorite);
		iViewShare = (ImageView) findViewById(R.id.iViewShare);
		
		movieId = getIntent().getExtras().getInt("_idPass");
		
		imageDownloader = new ImageDownloader();

		MySQLiteOpenHelper dbHelper  = new MySQLiteOpenHelper(this);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor;
		
		cursor = db.query(
				Movies.TABLE_NAME,  	
				null,			    	
			    Movies._ID + "=" + movieId,				
			    null,                  	
			    null,                  	
			    null,                  	
			    null
			    );
		
		DisplayMetrics metrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

		int screenWidthPx = metrics.widthPixels;
		int posterWidth = screenWidthPx - MyDisplayCode.dpToPx(this, 50);
		
		if (cursor != null && cursor.moveToFirst()) {
			
			iViewMovieImage.getLayoutParams().width = posterWidth;
			iViewMovieImage.getLayoutParams().height = MyDisplayCode.dpToPx(this, 230);
			
			imageDownloader.download(cursor.getString(cursor.getColumnIndex(Movies.WIDE_IMAGE_URL)), iViewMovieImage,
					screenWidthPx, screenWidthPx);
			
			tViewMovieName.setText( cursor.getString(cursor.getColumnIndex(Movies.NAME)) );
			tViewMovieDescription.setText( cursor.getString(cursor.getColumnIndex(Movies.DESCRIPTION)) );
			
			
		}
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_movie_details, menu);
		return true;
	}

}
