package com.joejbh.moviebrowser;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.joejbh.moviebrowser.R;
import com.joejbh.sourcecode.AbstractNavDrawer;
import com.joejbh.sourcecode.MyArrayAdapter;
import com.joejbh.sourcecode.MyListItem;
import com.joejbh.moviebrowser.database.*;
import com.joejbh.moviebrowser.database.MovieContract.Movies;

public class ActivityBrowseMovies extends AbstractNavDrawer{

	String logTag = "MyMoviesActivity";
	
	
	// tLayout Should be outside of onCreate so that optionsMenu can access it
	TableLayout tLayout;
	ArrayList<LinearLayout> posterList;

	int posterImgSampleHeightDp = 140;
	int posterImgSampleWidthDp = 140;
	int posterChildrenPaddingDp = 5;
	int posterChildrenMarginDp = 12;
	
	MovieBrowser mBrowser;

	TextView tViewBanner;
	String filter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* ----------------------------------------
		 * -------- Navigation Drawer stuff -------
		 */
		LayoutInflater myInflater = LayoutInflater.from(this);

		// Put the layout of the actual activity into the Drawer Layout
		LinearLayout layoutInsertPoint = (LinearLayout) findViewById(R.id.activity_content);
		RelativeLayout activityLayout = (RelativeLayout) myInflater.inflate(
				R.layout.activity_browse_movies, layoutInsertPoint, false);

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
		
		// Find the banner for displaying the genre
		tViewBanner = (TextView) activityLayout.findViewById(R.id.activityBanner);
		
		// Find out what filter/genre to display
		filter = (String) getIntent().getExtras().get("filter");
		
		
		// Create a MovieBrowser object with the appropriate sizing, padding, and margins
		mBrowser = new MovieBrowser(this, 
				posterImgSampleHeightDp, posterImgSampleWidthDp, 
				posterChildrenPaddingDp, posterChildrenMarginDp);

		
		posterList = retrievePosterList(filter);
		
		tLayout = (TableLayout) activityLayout.findViewById(R.id.tLayoutBrowseMovies);
		
		mBrowser.displayBrowser(tLayout, posterList);
		
	}
	
	
	/**
	 * Obtains a list of posters to display.
	 * This function uses imageDownloader and will have to be called again in order to re-Display the images
	 * @param genre
	 * @return ArrayList<LinearLayout> of 'posters'
	 */
	private ArrayList<LinearLayout> retrievePosterList(String lFilter) {

		// Set the banner text view depending on the filter
		if (lFilter.equals("My Favorites")){
			tViewBanner.setText("My Favorite Movies");
		}else{
			tViewBanner.setText(lFilter + " Movies");
		} 
		
		
		// LinearLayout that will be used for the posters
		LinearLayout moviePoster;
		
		
		// Array list of completed posters
		ArrayList<LinearLayout> posterList = new ArrayList<LinearLayout>();

		
		// Get readable database
		MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(this);
		SQLiteDatabase readableDb = dbHelper.getReadableDatabase();
		
		
		// Cursor for info returned from query
		Cursor cursor;
		
		
		// Set which columns should be retrieved from the db
		String[] moviesProjection = {
		    Movies._ID,
		    Movies.NAME,
		    Movies.IMAGE_URL,
		    Movies.GENRE,
		    Movies.IS_FAVORITE
		    };


		// Set the selection part of the query - contingent on the filter.
		String movieSelection;
		
		if (lFilter.equals("My Favorites"))
			movieSelection = Movies.IS_FAVORITE + "=1";
		else
			movieSelection = Movies.GENRE + "='" + lFilter + "'";
		
		
		cursor = readableDb.query(
				Movies.TABLE_NAME,
				moviesProjection,
				movieSelection,
			    null,
			    null,
			    null,
			    null
			    );
		
		
		Log.i(logTag, "Checking if cursor is null...");
		
		// If the cursor is not empty
		if (cursor != null && cursor.moveToFirst()) {
			
			Log.i(logTag, "Cursor not null and about to traverse the cursor");
			
			do {
				moviePoster = mBrowser.makePoster(
						this,
						cursor.getInt(cursor.getColumnIndex(Movies._ID)),
						cursor.getString(cursor.getColumnIndex(Movies.NAME)),
						cursor.getString(cursor.getColumnIndex(Movies.IMAGE_URL)),
						cursor.getString(cursor.getColumnIndex(Movies.IS_FAVORITE))
						);
				posterList.add(moviePoster);	
			} while(cursor.moveToNext());
			
		}
		readableDb.close();
		return posterList;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.browse, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		    case R.id.view_large_posters:
		    	posterImgSampleHeightDp = 190;
		    	posterImgSampleWidthDp = 190;
		    	posterChildrenPaddingDp = 8;
		    	posterChildrenMarginDp = 20;
		    	
		    	tLayout.removeAllViews();
		    	
		    	// Get a new MovieBrowser with the new sizes.
		    	mBrowser = new MovieBrowser(this, 
						posterImgSampleHeightDp, posterImgSampleWidthDp, 
						posterChildrenPaddingDp, posterChildrenMarginDp);

		    	
		    	// Need a new posterList since the MovieBrowser configuration has changed
				posterList = retrievePosterList(filter);
				
				mBrowser.displayBrowser(tLayout, posterList);
		    	
			    return true;
		    
		    case R.id.view_normal_posters:
		    	posterImgSampleHeightDp = 140;
		    	posterImgSampleWidthDp = 140;
		    	posterChildrenPaddingDp = 5;
		    	posterChildrenMarginDp = 10;
		    	
		    	tLayout.removeAllViews();
		    	
		    	mBrowser = new MovieBrowser(this, 
						posterImgSampleHeightDp, posterImgSampleWidthDp, 
						posterChildrenPaddingDp, posterChildrenMarginDp);

				posterList = retrievePosterList(filter);
				
				mBrowser.displayBrowser(tLayout, posterList);		    
		    default:
		    return super.onOptionsItemSelected(item);
		}
	}
}




/*
 * movie_poster = (LinearLayout)
 * myInflater.inflate(R.layout.layout_movie_poster, null, false);
 * 
 * posterImageView = (ImageView)
 * movie_poster.findViewById(R.id.posterImageView);
 * imageDownloader.download("http://www.tersefilms.com/images/officeSpace_sq.jpg"
 * , posterImageView);
 * 
 * textViewPoster = (TextView) movie_poster.findViewById(R.id.textViewPoster);
 * textViewPoster.setText("Office Space");
 * 
 * tr.addView(movie_poster);
 * 
 * 
 * 
 * movie_poster = (LinearLayout)
 * myInflater.inflate(R.layout.layout_movie_poster, null, false);
 * 
 * posterImageView = (ImageView)
 * movie_poster.findViewById(R.id.posterImageView);
 * imageDownloader.download("http://www.tersefilms.com/images/diehard_sq.jpg",
 * posterImageView);
 * 
 * textViewPoster = (TextView) movie_poster.findViewById(R.id.textViewPoster);
 * textViewPoster.setText("Diehard");
 * 
 * tr.addView(movie_poster);
 * 
 * tl.addView(tr);
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * tr = (TableRow) myInflater.inflate(R.layout.layout_new_row, null, false);
 * 
 * 
 * movie_poster = (LinearLayout)
 * myInflater.inflate(R.layout.layout_movie_poster, null, false);
 * 
 * posterImageView = (ImageView)
 * movie_poster.findViewById(R.id.posterImageView);
 * imageDownloader.download("http://www.tersefilms.com/images/prototype_sq.jpg",
 * posterImageView);
 * 
 * textViewPoster = (TextView) movie_poster.findViewById(R.id.textViewPoster);
 * textViewPoster.setText("Prototype");
 * 
 * tr.addView(movie_poster);
 * 
 * 
 * 
 * movie_poster = (LinearLayout)
 * myInflater.inflate(R.layout.layout_movie_poster, null, false);
 * 
 * posterImageView = (ImageView)
 * movie_poster.findViewById(R.id.posterImageView);
 * imageDownloader.download("http://www.tersefilms.com/images/raisingArizona_sq.jpg"
 * , posterImageView);
 * 
 * textViewPoster = (TextView) movie_poster.findViewById(R.id.textViewPoster);
 * textViewPoster.setText("Raising Arizona");
 * 
 * tr.addView(movie_poster);
 * 
 * tl.addView(tr);
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * tr = (TableRow) myInflater.inflate(R.layout.layout_new_row, null, false);
 * 
 * 
 * movie_poster = (LinearLayout)
 * myInflater.inflate(R.layout.layout_movie_poster, null, false);
 * 
 * posterImageView = (ImageView)
 * movie_poster.findViewById(R.id.posterImageView);
 * imageDownloader.download("http://www.tersefilms.com/images/terminator_sq.jpg"
 * , posterImageView);
 * 
 * textViewPoster = (TextView) movie_poster.findViewById(R.id.textViewPoster);
 * textViewPoster.setText("The Terminator");
 * 
 * tr.addView(movie_poster);
 * 
 * 
 * 
 * movie_poster = (LinearLayout)
 * myInflater.inflate(R.layout.layout_movie_poster, null, false);
 * 
 * posterImageView = (ImageView)
 * movie_poster.findViewById(R.id.posterImageView);
 * imageDownloader.download("http://www.tersefilms.com/images/voices_sq.jpg",
 * posterImageView);
 * 
 * textViewPoster = (TextView) movie_poster.findViewById(R.id.textViewPoster);
 * textViewPoster.setText("Voices");
 * 
 * tr.addView(movie_poster);
 * 
 * tl.addView(tr);
 */

/*
 * GridView gridView = (GridView) findViewById(R.id.gridview);
 * 
 * 
 * ArrayList<MyListItem> movieListItems = new ArrayList<MyListItem>();
 * ArrayAdapter<MyListItem> moviesAdapter = new MyArrayAdapter(this,
 * movieListItems);
 * 
 * 
 * 
 * The following is static code, but a real version of this would have a local
 * database to store information. I would use a SyncAdapter to make sure that
 * the content in the local database stayed current The SyncAdapter would use
 * REST API calls to access the data.
 * 
 * moviesAdapter.add(new MyListItem( R.layout.layout_movie_poster,
 * R.id.posterImageView, R.id.textViewPoster,
 * R.drawable.ic_action_play_over_video, "Text", "Item", ""));
 * 
 * moviesAdapter.add(new MyListItem( R.layout.layout_movie_poster,
 * R.id.posterImageView, R.id.textViewPoster,
 * "http://www.tersefilms.com/images/officeSpace_sq.jpg", "Text", "Item", ""));
 * 
 * moviesAdapter.add(new MyListItem( R.layout.layout_movie_poster,
 * R.id.posterImageView, R.id.textViewPoster,
 * "http://www.tersefilms.com/images/officeSpace_sq.jpg", "Text", "Item", ""));
 * 
 * moviesAdapter.add(new MyListItem( R.layout.layout_movie_poster,
 * R.id.posterImageView, R.id.textViewPoster,
 * "http://www.tersefilms.com/images/officeSpace_sq.jpg", "Text", "Item", ""));
 * 
 * moviesAdapter.add(new MyListItem( R.layout.layout_movie_poster,
 * R.id.posterImageView, R.id.textViewPoster,
 * "http://www.tersefilms.com/images/officeSpace_sq.jpg", "Text", "Item", ""));
 * 
 * moviesAdapter.add(new MyListItem( R.layout.layout_movie_poster,
 * R.id.posterImageView, R.id.textViewPoster,
 * "http://www.tersefilms.com/images/officeSpace_sq.jpg", "Text", "Item", ""));
 * 
 * 
 * gridView.setAdapter(moviesAdapter);
 */