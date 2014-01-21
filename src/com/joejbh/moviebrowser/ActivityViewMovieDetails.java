package com.joejbh.moviebrowser;


import java.util.ArrayList;

import com.joejbh.moviebrowser.database.MySQLiteOpenHelper;
import com.joejbh.moviebrowser.database.MovieContract.Movies;
import com.joejbh.sourcecode.AbstractNavDrawer;
import com.joejbh.sourcecode.ImageDownloader;
import com.joejbh.sourcecode.MyArrayAdapter;
import com.joejbh.sourcecode.MyDisplayCode;
import com.joejbh.sourcecode.MyListItem;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityViewMovieDetails extends AbstractNavDrawer {

	// Views for Navigation Drawer
	LinearLayout layoutInsertPoint;
	RelativeLayout activityLayout;
	LayoutInflater myInflater;
	
	
	// Views for displaying Movie Content
	ImageView iViewMovieImage, iViewIsFavorite, iViewShare;
	TextView tViewMovieName, tViewMovieDescription;
	
	
	// Objects for dealing with SQLite DB 
	SQLiteDatabase db;
	Cursor cursor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* ----------------------------------------
		 * -------- Navigation Drawer stuff -------
		 */
		
		myInflater = LayoutInflater.from(this);

		
		// Put the layout of the actual activity into the Drawer Layout
		layoutInsertPoint = (LinearLayout) findViewById(R.id.activity_content);
		activityLayout = (RelativeLayout) myInflater.inflate(
				R.layout.activity_view_movie_details, layoutInsertPoint, false);
		
		
		// Create a list of MyListItems for the ArrayAdapter
		ArrayList<MyListItem> drawerListItems = new NavDrawerContents(
				getResources());

		
		// Create the ArrayAdapter for the Drawer.
		ArrayAdapter<MyListItem> drawerAdapter = new MyArrayAdapter(this,
				drawerListItems);

		
		// Create the drawer. This is a method of AbstractNavDrawer.
		createDrawer(drawerAdapter);

		layoutInsertPoint.addView(activityLayout);
		
		/* ------ End Navigation Drawer stuff -----
		 * ---------------------------------------- */
		
		// Get the id of the movie that was clicked
		int movieId = getIntent().getExtras().getInt("_idPass");
		
		
		// Get an ImageDownloader for downloading my MovieImages
		ImageDownloader imageDownloader = new ImageDownloader();
		
		 
		MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(this);
		
		
		// Find all of the Movie ImageViews 
		iViewMovieImage = (ImageView) findViewById(R.id.iViewMovieImage);
		iViewIsFavorite = (ImageView) findViewById(R.id.iViewIsFavorite);
		iViewShare = (ImageView) findViewById(R.id.iViewShare);
		
		// Find all of the Movie TextViews
		tViewMovieName = (TextView) findViewById(R.id.tViewMovieName);
		tViewMovieDescription = (TextView) findViewById(R.id.tViewMovieDescription);
		
		
		// Obtain SQLite Db
		db = dbHelper.getWritableDatabase();
		
		
		// Get all the information for the Movie with the correct id
		cursor = db.query(
				Movies.TABLE_NAME,  	
				null,			    	
			    Movies._ID + "=" + movieId,				
			    null,                  	
			    null,                  	
			    null,                  	
			    null
			    );
		
		
		// Obtain DisplayMetrics in order to help determine the best size for the Movie's ImageView
		DisplayMetrics metrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

		
		// We're mostly concerned with 
		int screenWidthPx = metrics.widthPixels;
		int posterWidth = screenWidthPx - MyDisplayCode.dpToPx(this, 50);
		
		
		// Make sure that the query returned something
		if (cursor != null && cursor.moveToFirst()) {
			
			
			// Set the size of the Movie image placeholder
			iViewMovieImage.getLayoutParams().width = posterWidth;
			iViewMovieImage.getLayoutParams().height = MyDisplayCode.dpToPx(this, 230);

			
			// Download the Image with imageDownloader
			imageDownloader.download(cursor.getString(cursor.getColumnIndex(Movies.WIDE_IMAGE_URL)), iViewMovieImage,
					screenWidthPx, screenWidthPx);
			
			
			// Set the text for the Name and Description of the movie
			tViewMovieName.setText( cursor.getString(cursor.getColumnIndex(Movies.NAME)) );
			tViewMovieDescription.setText( cursor.getString(cursor.getColumnIndex(Movies.DESCRIPTION)) );
			
			
			
			// Configure the isFavorite Image View
			
			// If the movie is a favorite, change the isFavorite Image View
			if(cursor.getString(cursor.getColumnIndex(Movies.IS_FAVORITE)).equals("1")){
				iViewIsFavorite.setImageResource(R.drawable.ic_action_important);
			}
			
			// set the onClickListener to be able to toggle isFavorite for this movie.
			iViewIsFavorite.setOnClickListener(new OnClickListener() {
				
				// call toggleFavorite upon click.
				@Override
				public void onClick(View v) {
					toggleFavorite(v);				
				}

				//
				private void toggleFavorite(View v) {
					
					// For inserting values into DB
					ContentValues values = new ContentValues();
					
					// Messy comparison of Drawables to see if the movie is currently marked as a favorite.
					// If the movie is currently a favorite, put false into values for isFavorite and change image.
					if( ((ImageView) v).getDrawable().getConstantState().equals
				            (getResources().getDrawable(R.drawable.ic_action_important).getConstantState())){
						
						((ImageView) v).setImageResource(R.drawable.ic_action_not_important);
						values.put(Movies.IS_FAVORITE, false);
						MyDisplayCode.doToast(getApplicationContext(), "Movie removed from favorites.");
						
					}
					// If the movie is not a favorite, put true into values for isFavorite and change image.
					else{
						
						((ImageView) v).setImageResource(R.drawable.ic_action_important);
						values.put(Movies.IS_FAVORITE, true);
						MyDisplayCode.doToast(getApplicationContext(), "Movie added to favorites.");
					}
					
					// Insert new isFavorite value
					db.update(Movies.TABLE_NAME, values, Movies._ID + "=" + cursor.getString(0), null);
					values.clear();
				}
			});
		}
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		cursor.close();
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.view_movie_details, menu);
		return true;
	}

}
