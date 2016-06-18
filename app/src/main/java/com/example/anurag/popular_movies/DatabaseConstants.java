package com.example.anurag.popular_movies;

import android.provider.BaseColumns;
import android.widget.ImageView;
import android.widget.TextView;

public  abstract class DatabaseConstants implements BaseColumns {
    // DB specific constants
    public static final String TABLE_NAME = "movies";
    public static final String MOVIE_ID= "MOVIE_id";
    public static final String MOVIE_TITLE= "MOVIE_title";
    public static final String MOVIE_POSTER= "MOVIE_poster";
    public static final String MOVIE_RATING= "MOVIE_rating";
    public static final String MOVIE_RELEASEDATE= "MOVIE_date";
    public static final String MOVIE_OVERVIEW= "MOVIE_overview";


    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "MOVIE.db";


}
