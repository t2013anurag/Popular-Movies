package com.example.anurag.popular_movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.squareup.picasso.Picasso;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by anurag on 4/4/16.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private  final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private TextView textView_movie_title;
    private ImageView textView_movie_poster;
    private TextView textView_movie_rating;
    private TextView textView_movie_releaseDate;
    private TextView textView_movie_overview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_layout);
        Bundle bundle = getIntent().getExtras();

        String image = bundle.getString("image");
        String overview = bundle.getString("overview");
        String title = bundle.getString("title");
        String release_date = bundle.getString("release_date");
        String rating = bundle.getString("rating");


        textView_movie_poster = (ImageView) findViewById(R.id.movie_poster);
        Picasso.with(this).load(image).into(textView_movie_poster);

        textView_movie_title = (TextView) findViewById(R.id.movie_title);
        textView_movie_title.setText(title);

        textView_movie_releaseDate = (TextView) findViewById(R.id.movie_releaseDate);
        textView_movie_releaseDate.setText(release_date);

        textView_movie_overview = (TextView) findViewById(R.id.movie_overview);
        textView_movie_overview.setText(overview);

        textView_movie_rating = (TextView) findViewById(R.id.movie_ratings);
        textView_movie_rating.setText(rating);


    }

}
