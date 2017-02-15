package com.example.munix.moviement;

import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CatalogueFragment extends Fragment{

    private ArrayAdapter<String> mMovieDataAdapter;
    private ArrayAdapter<Image> mMovieAdapter;

    public CatalogueFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cataloguefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            updateCatalogue();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<Image> tempMovieCat = new ArrayList<Image>();

        mMovieAdapter = new ArrayAdapter<Image>(
                getActivity(),
                R.layout.list_catalogue,
                R.id.item_catalogue_imageView,
                tempMovieCat);

        GridView movieView = (GridView) rootView.findViewById(R.id.grid_catalogue);
        movieView.setAdapter(mMovieAdapter);

        // set callback on click
        movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image movieSelected = mMovieAdapter.getItem(position);
                Toast.makeText(getActivity(), "Selected" + movieSelected, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public boolean updateCatalogue(){
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCatalogue();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]>{
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            final String APIKey = "81250b70641edd9d8d65df96a437b2d8";

            try {
                final String baseURL = "https://api.themoviedb.org/3/";
                final String moviePopular = baseURL + "movie/popular?";
                final String APIKeyFlag = "api_key";

                Uri buildUri = Uri.parse(moviePopular).buildUpon()
                        .appendQueryParameter(APIKeyFlag, APIKey).build();
                URL url = new URL(buildUri.toString());

                Log.v(LOG_TAG, "Built URL : " + url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                String[] result = getMovieDataFromJson(movieJsonStr);
                Log.v(LOG_TAG, "Data FETCHED: ");
                for (String aResult : result) Log.v(LOG_TAG, aResult);
                return result;
            }catch(JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            mMovieDataAdapter.clear();
            mMovieDataAdapter.addAll(strings);

            Image locandina;
            mMovieAdapter.
        }

        private String[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_TITLE = "title";
            final String TMDB_ORIG_LANG = "original_language";
            final String TMDB_ORIG_TITLE = "original_title";
            final String TMDB_ID = "id";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_GENRE_IDS = "genre_ids";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_ADULT = "adult";
            final String TMDB_POSTER_PATH = "poster_path";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            // retrieve movie data form DB
            String[] resultStrs = new String[movieArray.length()];
            for(int i = 0; i < movieArray.length(); i++) {

                JSONObject movieData = movieArray.getJSONObject(i);

                // get movie data
                String title = movieData.getString(TMDB_TITLE);
                String image = movieData.getString(TMDB_POSTER_PATH);
                resultStrs[i] = title + '-' + image;
            }
            return resultStrs;
        }
    }

}
