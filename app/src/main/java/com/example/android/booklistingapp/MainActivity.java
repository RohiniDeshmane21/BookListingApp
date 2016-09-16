package com.example.android.booklistingapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText searchString;
    Button btnSearch;
    ArrayList<bookInfo> booklist = new ArrayList<bookInfo>();
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button)findViewById(R.id.buttonSearch);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        progress.setMax(10);
        progress.setVisibility(View.GONE);
        searchString = (EditText) findViewById(R.id.editTextBookName);
       // final String search = searchString.getText().toString();

        setTitle("Book List");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0);
                if (!booklist.isEmpty()) {
                    booklist.clear();

                }
                try {
                    String enteredText = URLEncoder.encode(searchString.getText().toString(), "utf-8");

                    String url = "https://www.googleapis.com/books/v1/volumes?q=" + enteredText + "&maxResults=10";

                    ConnectivityManager cm =
                            (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();

                    if (isConnected == true) {
                        new HttpAsyncTask().execute(url);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void showData(ArrayList<bookInfo> booklistNew)
    {

        bookInfoAdapter attractionAdapter = new bookInfoAdapter(MainActivity.this, booklistNew);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(attractionAdapter);
        attractionAdapter.notifyDataSetChanged();

    }

    public String GET(String url){
        InputStream inputStream = null;
        String result = "";
        StringBuilder total = null;
        HttpsURLConnection urlConnection = null;

        try
        {
            URL urlVar = new URL(url);
            urlConnection =
                    (HttpsURLConnection) urlVar.openConnection();

            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();


            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_ACCEPTED) {
               // Log.d(TAG, "doInBackground(): connection failed: statusCode: " + statusCode);
                //                    return null;
            }

            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }

            Log.d("InputStream",in.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return total.toString();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = GET(urls[0]);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                for(int i=0; i< jsonObject.getJSONArray("items").length();i++)
                {
                    JSONObject info = jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("volumeInfo");

                    String bookName = info.get("title").toString();
                    String author = info.getJSONArray("authors").get(0).toString();
                    String publisher =  "Publisher : " + info.get("publisher").toString();
                    String publishedDate = "Date : "+info.get("publishedDate").toString();
                    String imageLink = info.getJSONObject("imageLinks").getString("thumbnail").toString();

                    booklist.add(new bookInfo(bookName, author,publisher,publishedDate,imageLink));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            showData(booklist);
            progress.setVisibility(View.GONE);
        }
    }

}