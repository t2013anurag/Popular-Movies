package com.example.anurag.popular_movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.AdapterView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private  final String LOG_TAG = MainActivity.class.getSimpleName();
    private GridView gridView;
    private  ProgressBar progressBar;

    private MostPopularAdapter popularAdapter;
    private  ArrayList<GridItem> gridData;
    private String base_URL =  "http://api.themoviedb.org/3/movie/popular";

    DbHelp dbHelp;
    SQLiteDatabase db;


    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelp = new DbHelp(this);
        db = dbHelp.getWritableDatabase();


        gridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //Initializing the empty data
        gridData = new ArrayList<>();
        popularAdapter = new MostPopularAdapter(this, R.layout.grid_item, gridData);
        gridView.setAdapter(popularAdapter);

        //Starting item on click event
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

                Log.e("BFd",item.getId()+" ");

                intent.putExtra("movie_id", item.getId())
                        .putExtra("image", item.getImage())
                        .putExtra("overview", item.getOverview())
                        .putExtra("title", item.getTitle())
                        .putExtra("release_date", item.getReleaseDate())
                        .putExtra("rating", item.getVote_average());

                startActivity(intent);
            }
        });

        //Starting to get the data from api
        //new FetchMovies().execute(base_URL);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gridData.clear();
                if(isNetworkConnected())
                new FetchMovies().execute(base_URL);
                else {
                    Snackbar.make(gridView, "Couldn't refresh feed. Please check your internet connection!", Snackbar.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        fetchFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.most_popular) {
            base_URL = "http://api.themoviedb.org/3/movie/popular";
            new FetchMovies().execute(base_URL);
            progressBar.setVisibility(View.VISIBLE);
            gridData.clear();
            return true;
        }
        if(id == R.id.highest_rated) {
            base_URL = "http://api.themoviedb.org/3/movie/top_rated";
            new FetchMovies().execute(base_URL);
            progressBar.setVisibility(View.VISIBLE);
            gridData.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchMovies extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        /*
        0 mov
        1 tra
        2 rev
         */

        private void getMovieDataFromJson(String moviesJsonstr)throws JSONException {
            JSONObject moviesJson = new JSONObject(moviesJsonstr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            GridItem item;
            for(int i=0; i<moviesArray.length(); i++) {
                JSONObject result = moviesArray.getJSONObject(i);
                String movie_id = result.getString("id");
                String title = result.getString("original_title");
                String poster_path = result.getString("poster_path");
                String overview = result.getString("overview");
                String releaseDate = result.getString("release_date");
                String rating = result.getString("vote_average");
                poster_path = "http://image.tmdb.org/t/p/w185/" + poster_path;
                Log.v(LOG_TAG, movie_id + "title " + title + "path " + poster_path + "overview" + overview + "release" + releaseDate
                + "rating" + rating);
                item = new GridItem();
                item.setId(movie_id);
                item.setTitle(title);
                item.setImage(poster_path);
                item.setReleaseDate(releaseDate);
                item.setOverview(overview);
                item.setVote_average(rating);
                gridData.add(item);


                Cursor c = db.rawQuery("SELECT * FROM " + DatabaseConstants.TABLE_NAME +" WHERE " + DatabaseConstants.MOVIE_ID
                + " IS " + movie_id,  null);

                if(!c.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseConstants.MOVIE_ID, movie_id);
                    contentValues.put(DatabaseConstants.MOVIE_TITLE, title);
                    contentValues.put(DatabaseConstants.MOVIE_POSTER, poster_path);
                    contentValues.put(DatabaseConstants.MOVIE_OVERVIEW, overview);
                    contentValues.put(DatabaseConstants.MOVIE_RELEASEDATE, releaseDate);
                    contentValues.put(DatabaseConstants.MOVIE_RATING, rating);
                    db.insert(DatabaseConstants.TABLE_NAME, DatabaseConstants.MOVIE_ID, contentValues);
                }
                c.close();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
           if(params.length == 0) {
               return null;
           }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonstr = null;
            String format = "json";
            String api_key = "66b412b7f7a1aa11e0ae6e6871ad7d04";
            try {
                String FEED_URL = params[0];
                FEED_URL = FEED_URL+"?";
                final String API = "api_key";

                Uri builtUri = Uri.parse(FEED_URL).buildUpon()
                        .appendQueryParameter(API, api_key)
                        .build();

                URL url = new URL(builtUri.toString());
//                Log.v(LOG_TAG, "build url " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine())!= null) {
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0) {
                    return null;
                }
                moviesJsonstr = buffer.toString();
              //  Log.v(LOG_TAG, " json received is " + moviesJsonstr);
                result = 1;

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream ", e);
                    }
                }
            }
            try {
               getMovieDataFromJson(moviesJsonstr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
                if(result == 1) {
                    popularAdapter.setGridData(gridData);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch movies! Check connectivity", Toast.LENGTH_SHORT).show();
                }
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }




    // Database methods
    public void fetchFromDb(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseConstants.TABLE_NAME ,null );
        GridItem item;
        gridData.clear();
        if(cursor.moveToFirst()){


            do {

                String id = cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOVIE_TITLE));
                String poster_path = cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOVIE_POSTER));
                String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOVIE_RELEASEDATE));
                String overview = cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOVIE_OVERVIEW));
                String rating = cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOVIE_RATING));

                item = new GridItem();
                item.setId(id);
                item.setTitle(title);
                item.setImage(poster_path);
                item.setReleaseDate(releaseDate);
                item.setOverview(overview);
                item.setVote_average(rating);
                gridData.add(item);
            }while (cursor.moveToNext());
            cursor.close();
            popularAdapter.setGridData(gridData);
            progressBar.setVisibility(View.GONE);

        }else
            new FetchMovies().execute(base_URL);
    }



// Checking if the user is connected to internet, if yes return true else false
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
