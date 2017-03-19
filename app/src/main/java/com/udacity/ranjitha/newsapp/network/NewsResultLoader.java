package com.udacity.ranjitha.newsapp.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.udacity.ranjitha.newsapp.data.NewsPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads a list of NewsData by using an AsyncTask to perform the  network request to the given URL.
 */
public class NewsResultLoader extends AsyncTaskLoader<List<NewsPojo>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsResultLoader.class.getName();
    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsResultLoader}.
     * * @param context of the activity
     * * @param url to load data from
     */
    public NewsResultLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsPojo> loadInBackground() {

        //check if url is null then simply return;
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and ex tract a list of NewsData
        List<NewsPojo> newsData = fetchData(mUrl);
        return newsData;
    }

    private static List<NewsPojo> fetchData(String requestUrl) {
        //Create Url object
        URL url = makeUrl(requestUrl);

        String jsonResponse = callAPI(url);

        List<NewsPojo> newsData = extractFromJson(jsonResponse);

        return newsData;
    }

    /* generate the url form the given string
     *Here method is declare static because without creating the object we call this method
     */
    private static URL makeUrl(String input) {
        //create Url object
        URL url = null;
        try {
            url = new URL(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*Make http request to the server and return the json response
     */
    private static String callAPI(URL url) {
        //create json response and also httpurlrequest and inptstream
        String jsonResponse = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        //if Url is null then simply return empty jsonResponse
        if (url == null) {
            return jsonResponse;
        }
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //set the http request method
            httpURLConnection.setRequestMethod("GET");
            //establish the connection
            httpURLConnection.connect();


            //if the request was successfully then it return 200 code
            //then read the stream and parse the response
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //check if all the data is retrive so close the http connection
            //and release the resources
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            //close the inputStream and release the inputstream resource
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return jsonResponse;
    }

    //get the input stream and convert to  the string json
    private static String readFromStream(InputStream input) throws IOException {
        StringBuilder data = new StringBuilder();
        if (input != null) {
            InputStreamReader inputStream = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStream);
            String line = reader.readLine();
            while (line != null) {
                data.append(line);
                line = reader.readLine();
            }
        }
        return data.toString();
    }

    //extract data from json and return list of the newsData object
    private static List<NewsPojo> extractFromJson(String newsDataJson) {
        //check if the json string empty or null
        if (TextUtils.isEmpty(newsDataJson)) {
            return null;
        }

        //Create empty arrayList in which store the all the newsData object

        List<NewsPojo> newsData = new ArrayList<>();

        //Parsing the json and get the required data form the string url

        try {
            //create the root json object of the give string
            JSONObject baseObject = new JSONObject(newsDataJson);

            //create the json array and fetch all array object from given string
            JSONObject responseObject = baseObject.getJSONObject("response");
            //create the json array and fetch all array object from given string
            JSONArray resultArray = responseObject.getJSONArray("results");

            //fetch all the json array object one by one
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentNewsData = resultArray.getJSONObject(i);

                //extract all the information that required
                String sectionName = currentNewsData.optString("sectionName");
                String webTitle = currentNewsData.optString("webTitle");
                String webUrl = currentNewsData.optString("webUrl");
                String authorName = currentNewsData.optString("byline");
                String date = currentNewsData.optString("webPublicationDate");
                String webPublicationDate = "";
                for (int j = 0; date.charAt(j) != 'T'; j++) {
                    webPublicationDate += Character.toString(date.charAt(j));
                }
                newsData.add(new NewsPojo(sectionName, webTitle, authorName, webPublicationDate, webUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsData;
    }

}
