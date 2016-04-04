package com.example.anurag.popular_movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.AdapterView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private GridView gridView;
    private ProgressBar progressBar;

    private MostPopularActivity popularAdapter;
    private ArrayList<GridItem> gridData;
    private String base_URL =  "http://api.themoviedb.org/3/movie/popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initializing the empty data
        gridData = new ArrayList<>();
        popularAdapter = new MostPopularActivity(this, R.layout.grid_item, gridData);
        gridView.setAdapter(popularAdapter);

        //Starting to get the data from api
        new FetchMovies().execute(base_URL);
        progressBar.setVisibility(View.VISIBLE);

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
        if (id == R.id.new_movies) {
            startActivity(new Intent(this, NewMoviesActivity.class));
            return true;
        }
        if(id == R.id.sort_by_popularity) {
            startActivity(new Intent(this, MostPopularActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchMovies extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = FetchMovies.class.getSimpleName();


        private void getMovieDataFromJson(String moviesJsonstr)throws JSONException {
            JSONObject moviesJson = new JSONObject(moviesJsonstr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            GridItem item;
            for(int i=0; i<moviesArray.length(); i++) {
                JSONObject result = moviesArray.getJSONObject(i);
                String title = result.getString("original_title");
                String poster_path = result.getString("poster_path");
                poster_path = "http://image.tmdb.org/t/p/w185/" + poster_path;
                Log.v(LOG_TAG, "title " + title + "path " + poster_path);
                item = new GridItem();
                item.setTitle(title);
                item.setImage(poster_path);
                gridData.add(item);
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
                final String FEED_URL = "http://api.themoviedb.org/3/movie/popular?";
                final String API = "api_key";

                Uri builtUri = Uri.parse(FEED_URL).buildUpon()
                        .appendQueryParameter(API, api_key)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "build url " + builtUri.toString());

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
                Log.v(LOG_TAG, " json received is " + moviesJsonstr);
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
                    Toast.makeText(MainActivity.this, "Failed to fetch movies!", Toast.LENGTH_SHORT).show();
                }
            progressBar.setVisibility(View.GONE);
        }
    }
}
