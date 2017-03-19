package com.udacity.ranjitha.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.udacity.ranjitha.newsapp.data.NewsInfoAdapter;
import com.udacity.ranjitha.newsapp.data.NewsPojo;
import com.udacity.ranjitha.newsapp.network.NewsResultLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsPojo>> {

    String input = "https://content.guardianapis.com/search?api-key=52adc473-ff73-4947-88fd-3835dd550c3b";

    private NewsInfoAdapter newsDataAdapter;

    View progressBar;

    public MainActivity() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Find a reference to the {@link ListView} in the layout
        ListView newsDataListView = (ListView) findViewById(R.id.list);


        //Create a new adapter that takes an empty list of earthquakes as input
        newsDataAdapter = new NewsInfoAdapter(this, new ArrayList<NewsPojo>());

        //set the adapter on the {@link ListView}
        newsDataListView.setAdapter(newsDataAdapter);

        //set the onItemClickListener
        newsDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsPojo currentData = newsDataAdapter.getItem(position);
                final String URL = "detailUrl";
                final String TITLE = "toolbarTitle";
                String mUrl = currentData.getUrl();

                //send the string from the one activity to another activity
                Intent sendIntent = new Intent(MainActivity.this, NewsDetail.class);
                sendIntent.putExtra(URL, mUrl);
                sendIntent.putExtra(TITLE, currentData.getTitle());

                // Send the intent to launch a new activity
                startActivity(sendIntent);
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        progressBar = findViewById(R.id.loading_indicator);
        // If there is a network connection, fetch data

        if (networkInfo != null && networkInfo.isConnected()) {
            //get the reference of the Progressbar and set visibility VISIBLE
            progressBar.setVisibility(View.VISIBLE);

            getSupportLoaderManager().initLoader(1, null,this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            Toast.makeText(this, "Check Your internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public android.support.v4.content.Loader<List<NewsPojo>> onCreateLoader(int id, Bundle args) {
        return new NewsResultLoader(this, input);
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<NewsPojo>> loader, List<NewsPojo> newsData) {

        //set Visibility to GONE to Progressbar
        progressBar.setVisibility(View.GONE);

        newsDataAdapter.clear();
        // If there is a valid list of {@link NewsData}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsData != null && !newsData.isEmpty()) {
            newsDataAdapter.addAll(newsData);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<NewsPojo>> loader) {
        // Loader reset, so we can clear out our existing data.
        newsDataAdapter.clear();
    }
}
