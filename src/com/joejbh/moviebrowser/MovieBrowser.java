package com.joejbh.moviebrowser;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.joejbh.moviebrowser.R;
import com.joejbh.moviebrowser.database.MySQLiteOpenHelper;
import com.joejbh.moviebrowser.database.MovieContract.Movies;
import com.joejbh.sourcecode.MyDisplayCode;
import com.joejbh.sourcecode.ImageDownloader;

public class MovieBrowser {

	DisplayMetrics metrics;

	private int posterImageSampleHeightPx;
	private int posterImageSampleWidthPx;
	private int posterChildrenPaddingPx;
	private int posterChildrenMarginPx;
	private int posterFullWidthPx;
	
	private int screenWidthPx;
	
	private int maxPostersInRow;
		
	LayoutInflater mInflater;
	
	public MovieBrowser(Activity activity, 
			int posterImageSampleHeightDp, int posterImageSampleWidthDp,
			int posterChildrenPaddingDp, int posterChildrenMarginDp) {

		mInflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		
		metrics = new DisplayMetrics();

		activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

		screenWidthPx = metrics.widthPixels;

		// Find out how many pixels the posters will take up in width
		posterImageSampleHeightPx = MyDisplayCode.dpToPx(activity.getApplicationContext(), posterImageSampleHeightDp);
		posterImageSampleWidthPx = MyDisplayCode.dpToPx(activity.getApplicationContext(), posterImageSampleWidthDp);
		posterChildrenPaddingPx = MyDisplayCode.dpToPx(activity.getApplicationContext(), posterChildrenPaddingDp);
		posterChildrenMarginPx = MyDisplayCode.dpToPx(activity.getApplicationContext(), posterChildrenMarginDp);

		// Padding and Margin are * 2 because the padding and margin are applied
		// to the left and right of the poster
		posterFullWidthPx = (posterImageSampleWidthPx)
				+ (posterChildrenPaddingPx * 2) + (posterChildrenMarginPx * 2);
		
		// How much space the primary layout's padding will take up

		int reservedScreenPaddingDp = activity.getResources().getInteger(R.integer.activity_horizontal_margin_int);
		int reservedScreenPaddingPx = MyDisplayCode.dpToPx(activity.getApplicationContext(), reservedScreenPaddingDp);
		
		// Calculate how many posters can fit on one row.
		maxPostersInRow = (screenWidthPx - reservedScreenPaddingPx) / posterFullWidthPx;

		
	}	
	
	// Make the poster
	public LinearLayout makePoster(final Activity activity, final int _idPass, 
			String movieTitle, String movieImageUrl, final String isFavorite) {
		
		ImageDownloader imageDownloader = new ImageDownloader();
		
		// Inflate the movie poster layout
		LinearLayout moviePoster;
		moviePoster = (LinearLayout) mInflater.inflate(
				R.layout.layout_movie_poster, null, false);
		moviePoster.setPadding(posterChildrenMarginPx, posterChildrenMarginPx,
				posterChildrenMarginPx, posterChildrenMarginPx);

		// Set layout of the ImageView
		ImageView posterImageView;
		posterImageView = (ImageView) moviePoster
				.findViewById(R.id.imageViewPoster);
		posterImageView.getLayoutParams().width = posterImageSampleWidthPx;
		posterImageView.getLayoutParams().height = posterImageSampleHeightPx;
		posterImageView.setPadding(posterChildrenPaddingPx,
				posterChildrenPaddingPx - 1, posterChildrenPaddingPx, 0);
		posterImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setClassName("com.joejbh.moviebrowser", 
		            "com.joejbh.moviebrowser.ActivityViewMovieDetails");
		        intent.putExtra("_idPass", _idPass);
		        activity.startActivity(intent);
			}
		});
		
		
		// Set the TextView showing the Movie's Name
		TextView posterText = (TextView) moviePoster.findViewById(R.id.textViewPoster);
		posterText.setText(movieTitle);

		imageDownloader.download(movieImageUrl, posterImageView,
				posterImageSampleHeightPx, posterImageSampleWidthPx);
		
		
		// Configure the Poster Favorite Image View (indicates if the movie is a favorite)
		ImageView iViewIsFavorite = (ImageView) moviePoster.findViewById(R.id.iViewPosterFav);
		
		if(isFavorite.equals("1")){
			iViewIsFavorite.setImageResource(R.drawable.ic_action_important);
		}
		
		iViewIsFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleFavorite(v);				
			}

			private void toggleFavorite(View v) {
				MySQLiteOpenHelper dbHelper  = new MySQLiteOpenHelper(activity.getApplicationContext());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				
				ContentValues values = new ContentValues();
				
				// Messy comparison of Drawables to see if the movie is currently marked as a favorite.
				if( ((ImageView) v).getDrawable().getConstantState().equals
			            (activity.getResources().getDrawable(R.drawable.ic_action_important).getConstantState())){
					
					((ImageView) v).setImageResource(R.drawable.ic_action_not_important);
					values.put(Movies.IS_FAVORITE, false);
					MyDisplayCode.doToast(activity.getApplicationContext(), "Movie removed from favorites.");
					
				}
				
				else{
					
					((ImageView) v).setImageResource(R.drawable.ic_action_important);
					values.put(Movies.IS_FAVORITE, true);
					MyDisplayCode.doToast(activity.getApplicationContext(), "Movie added to favorites.");
				}
				
				db.update(Movies.TABLE_NAME, values, Movies._ID + "=" + _idPass, null);
				values.clear();
				db.close();
			}
		});
		
		
		ImageView iViewShare = (ImageView) moviePoster.findViewById(R.id.iViewShare);
		
		iViewShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyDisplayCode.doToast(activity.getApplicationContext(), 
						"If this were a real app, you could share something...");
			}
		});

		return moviePoster;
	}
	
	public void displayBrowser(TableLayout tableLayout, ArrayList<LinearLayout> posterList){
		
		TableRow tr = (TableRow) mInflater.inflate(R.layout.layout_new_row, null, false);

		tableLayout.setPadding(0, 0, 0, 0);
		
		// Keep track of how many posters in current row.
		int postersInRowCounter = 0;
		
		// Loop through all items in posterList
		for (LinearLayout posterItem : posterList) {

			// Add posterItem into the current row.  Increase postersInRowCounter.
			tr.addView(posterItem);
			postersInRowCounter++;
			
			// If reached the max number of posters permitted in row, put row into TableLayout; reset counter.
			if (postersInRowCounter >= maxPostersInRow) {
				tableLayout.addView(tr);
				postersInRowCounter = 0;
				
				// Make a new row to put posters into.
				tr = (TableRow) mInflater.inflate(R.layout.layout_new_row, null, false);
			}	
		}

		if (postersInRowCounter > 0)
			tableLayout.addView(tr);

	}
}
