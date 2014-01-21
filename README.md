MovieDatabase
=============

Demo of a Movie Browser App.

Lets the user browse movies according to genre and "favorites" chosen by the user.
Image content is downloaded from a remote server with a Customized ImageDownloader which optimizes the size of images stored locally and downloads images with AsyncTask.
The content regarding the movies is stored in a local SQLite DB, which is created the first time the user uses the app.
Handles multiple screen sizes, as well as vertical and horizontal orientation.
The user can change the view size of the "MoviePosters"

----------

Particular Features:

Custom ActionBar.

Custom Abstract Navigation Drawer Activity.
	1) Navigation Drawer turned into Abstract Activity, so it can be reused for any activities.

Custom Items for Navigation Drawer:
	1) Non-clickable heading items
	2) Clickable list items which open a new activity depending on their tag
	3) Filterable content for passing to Navigation Drawer
	4) Can insert images to the left of the Navigation Drawer text

Customized ImageDownloader which optimizes the size of images stored locally and downloads images from remote server with AsyncTask.

Handles first time use with SharedPrefs.

Local SQLite database for storing dummy data as well as the "favorite" movies of the user.