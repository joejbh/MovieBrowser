package com.joejbh.moviebrowser;

import java.util.ArrayList;

import android.content.res.Resources;

import com.joejbh.moviebrowser.R;
import com.joejbh.sourcecode.MyListItem;

public final class NavDrawerContents extends ArrayList<MyListItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3216443762106000779L;

	public NavDrawerContents(Resources resources) {
		
		
		// Items group 1
		this.add(new MyListItem(R.layout.drawer_list_head, R.id.drawer_list_head_imageView, R.id.drawer_list_head_textView, 
				R.drawable.ic_drawer, "My Stuff", 
				"Header",  ""));
		
		this.add(new MyListItem(R.layout.drawer_list_item, R.id.drawer_list_item_imageView, R.id.drawer_list_item_textView, 
				R.drawable.ic_action_play, "My Favorites", 
				"Item",  "filter-ActivityBrowseMovies"));
		
		
		// Items group 2
		this.add(new MyListItem(R.layout.drawer_list_head, R.id.drawer_list_head_imageView, R.id.drawer_list_head_textView, 
				R.drawable.ic_action_storage, "Browse", 
				"Header",  ""));
		this.add(new MyListItem(R.layout.drawer_list_item, R.id.drawer_list_item_imageView, R.id.drawer_list_item_textView, 
				R.drawable.ic_action_good, "Action", 
				"Item",  "filter-ActivityBrowseMovies"));
		this.add(new MyListItem(R.layout.drawer_list_item, R.id.drawer_list_item_imageView, R.id.drawer_list_item_textView, 
				R.drawable.ic_action_favorite, "Drama", 
				"Item",  "filter-ActivityBrowseMovies"));
		this.add(new MyListItem(R.layout.drawer_list_item, R.id.drawer_list_item_imageView, R.id.drawer_list_item_textView, 
				R.drawable.ic_action_group, "Comedy", 
				"Item",  "filter-ActivityBrowseMovies"));		
	}

}
