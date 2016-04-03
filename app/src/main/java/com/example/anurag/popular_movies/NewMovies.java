package com.example.anurag.popular_movies;

import android.app.Activity;
import android.view.Menu;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by anurag on 3/4/16.
 */
public class NewMovies extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
