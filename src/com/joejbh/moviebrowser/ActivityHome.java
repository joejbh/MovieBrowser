package com.joejbh.moviebrowser;

import java.util.ArrayList;

import com.joejbh.sourcecode.AbstractNavDrawer;
import com.joejbh.sourcecode.MyDisplayCode;
import com.joejbh.sourcecode.MyArrayAdapter;
import com.joejbh.sourcecode.MyListItem;
import com.joejbh.moviebrowser.database.*;
import com.joejbh.moviebrowser.database.MovieContract.Movies;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ActivityHome extends AbstractNavDrawer {

	String logTag = "ActivityHome";
	
	int screenHeightPx;
	int screenWidthPx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater myInflater = LayoutInflater.from(this);

		// Put the layout of the actual activity into the Drawer Layout
		LinearLayout layoutInsertPoint = (LinearLayout) findViewById(R.id.activity_content);
		LinearLayout activityLayout = (LinearLayout) myInflater.inflate(
				R.layout.activity_home, layoutInsertPoint, false);

		// Create a list of MyListItems for the ArrayAdapter
		ArrayList<MyListItem> drawerListItems = new NavDrawerContents(
				getResources());

		// Create the ArrayAdapter for the Drawer.
		ArrayAdapter<MyListItem> drawerAdapter = new MyArrayAdapter(this,
				drawerListItems);

		// Create the drawer. This is a method of AbstractNavDrawer.
		createDrawer(drawerAdapter);

		layoutInsertPoint.addView(activityLayout);
		
		/*
		 * -------- The above is default to permit the use of the Navigation Bar
		 * --------
		 */

		animateMenuArrow();
		
		
		// Check if this is the first time the app is being run.  If so, create Movies DB and add default items.
		final String PREFS_NAME = "MyPrefsFile";

		SharedPreferences appHistory = getSharedPreferences(PREFS_NAME, 0);

		if (appHistory.getBoolean("first_run", true)) {
		   		
			setDefaultDbValues();
			
			appHistory.edit().putBoolean("first_run", false).commit(); 
		}
		
	}
	
	// Perhaps later I will have the information retrieved from a remote source
	// but for now, I'm inserting values into a local sqlite db
	private void setDefaultDbValues() {
		
		MyDisplayCode.doToastL(this, "Welcome.  Since this is your first time using this app...");
		MyDisplayCode.doToast(this, "...Creating default db");
		
		Log.i(logTag, "Creating default db because first time use of app");
		
		MySQLiteOpenHelper dbHelper  = new MySQLiteOpenHelper(this);
		
		// Get DB in writable format
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Store values to be put into DB
		ContentValues values = new ContentValues();
		
		
		values.put(Movies.NAME, "Office Space");
		values.put(Movies.DESCRIPTION, "Office space is a comedic tale of company workers who " +
				"hate their jobs and decide to rebel against their greedy boss. In this scene, " +
				"outside 'consultants' are brought in to decide who will be 'let go'.");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/officeSpace_sq.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/Office_Space_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Die Hard");
		values.put(Movies.DESCRIPTION, "International terrorists have taken over a building and " +
				"are holding every one as hostage. Good guy NY cop John McCLane interfers with their " +
				"plans. In this scene, one of the suave hostages attempts to negotiate with the " +
				"terrorists saying that he can get John McClane to give himself up.");
		values.put(Movies.GENRE, "Action");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/diehard_sq.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/Die_Hard_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Voices");
		values.put(Movies.DESCRIPTION, "Audrey attempts to interview troubled Evelyn.");
		values.put(Movies.GENRE, "Drama");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/voices_sq.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/Voices_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Prototype");
		values.put(Movies.DESCRIPTION, "A prototype of the ROD project is under development. " +
				"Stan, as lead project manager, prepares to take his latest research to the boss " +
				"while the other employees have their minds on the birthday party planned for that night.");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/prototype_sq.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/prototype_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Raising Arizona");
		values.put(Movies.DESCRIPTION, "When a childless couple of an ex-con and an ex-cop decide to " +
				"help themselves to one of another family's quintupelets, their lives get more complicated " +
				"than they anticipated. In this scene, 2 friends visit the ex-con, and tempt him to " +
				"take part in one last heist.");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/raisingArizona_sq.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/Arizona_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "The Terminator");
		values.put(Movies.DESCRIPTION, "Skynet is a 21st century computer waging a losing war against " +
				"humans. To defeat the humans, Skynet sends a cyborg-machine back in time to destroy John " +
				"Connor, the future leader of the human resistance.. In retaliation, John, his mother, and " +
				"another cyborg-machine attempt to infiltrate the company lab that would someday develop Skynet.");
		values.put(Movies.GENRE, "Action");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/terminator_sq.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/Terminator_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Where Are We?");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Action");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/where_are_we.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/where_are_we_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "The Long Night");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Drama");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/the_long_night.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/the_long_night_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Sand");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Drama");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/sand.jpg"); 
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/sand_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Mangia");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/mangia.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/mangia_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Clay Friends");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/clay_friends.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/clay_friends_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "The Long Road");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Drama");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/the_long_road.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/the_long_road_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "The Cathedral");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Drama");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/the_cathedral.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/the_cathedral_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Pumpkins");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/pumpkins.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/pumpkins_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "He Could Do It");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Comedy");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/he_could_do_it.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/he_could_do_it_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		
		values.put(Movies.NAME, "Being Watched");
		values.put(Movies.DESCRIPTION, "Arma virumque cano, Troiae qui primus ab oris" +
				"Italiam, fato profugus, Laviniaque venit" +
				"litora, multum ille et terris iactatus et alto" +
				"vi superum saevae memorem Iunonis ob iram;");
		values.put(Movies.GENRE, "Action");
		values.put(Movies.IMAGE_URL, "http://www.tersefilms.com/images/additional/being_watched.jpg");
		values.put(Movies.WIDE_IMAGE_URL, "http://www.tersefilms.com/images/additional/being_watched_350.jpg");
		values.put(Movies.IS_FAVORITE, false);
		
		db.insert(Movies.TABLE_NAME, null, values);
		values.clear();
		
		db.close();
		
	}
	
	private void animateMenuArrow() {
		DisplayMetrics metrics;
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenHeightPx = metrics.heightPixels;
		screenWidthPx = metrics.widthPixels;
		

		final View target = findViewById(R.id.imageViewMenuArrow);
		final View targetParent = (View) target.getParent();

		Animation a = new TranslateAnimation(0.0f, targetParent.getWidth()
				- target.getWidth() - targetParent.getPaddingLeft()
				- targetParent.getPaddingRight(), 0.0f, 0.0f);
		a.setDuration(1500);
		a.setStartOffset(500);
		a.setFillAfter(true);
		a.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.bounce_interpolator));
		target.startAnimation(a);	
	}

	void doToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
