package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";
    public String NOW_PLAYING_URL;

    List<Movie> movies;
    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NOW_PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=" + getString(R.string.movies_api_key));
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);
        //RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);


        //Set the adapter on the recycler view
        binding.rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager on the recycler view
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));



        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {








            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
               JSONObject jsonObject =  json.jsonObject;
               try {
                   JSONArray results = jsonObject.getJSONArray("results");
                   Log.i(TAG, "results" + results.toString() );
                   movies.addAll(Movie.fromJsonArray(results));
                   movieAdapter.notifyDataSetChanged();
                   Log.i(TAG, "Movies" + movies.size());

               } catch(JSONException e) {
                   Log.e(TAG, "Hit json exception", e);
               }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}