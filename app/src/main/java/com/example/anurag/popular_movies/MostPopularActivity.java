package com.example.anurag.popular_movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.zip.Inflater;

/**
 * Created by anurag on 3/4/16.
 */
public class MostPopularActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.new_movies) {
            startActivity(new Intent(this, NewMoviesActivity.class));
            return true;
        }
        if(id == R.id.sort_by_popularity) {
            startActivity(new Intent(this, MostPopularActivity.class));
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
