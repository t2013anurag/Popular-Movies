package com.example.anurag.popular_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class RatingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);


        Intent intent = getIntent();
        String movie_id = intent.getStringExtra("movie_id");
        String title = intent.getStringExtra("title");

        Log.e("gdsgs",movie_id +" "+title);

        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setText(movie_id);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
