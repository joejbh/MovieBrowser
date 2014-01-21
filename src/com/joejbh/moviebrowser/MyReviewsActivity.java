package com.joejbh.moviebrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.joejbh.moviebrowser.R;

public class MyReviewsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reviews);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.my_reviews, menu);
		return true;
	}

}
