package com.example.android.booklistingapp;


import android.app.Activity;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
                if(!booklist.isEmpty())
                {
                   booklist.clear();

                }
                String url = "https://www.googleapis.com/books/v1/volumes?q="+ searchString.getText().toString().trim() + "&maxResults=10";
                new HttpAsyncTask().execute(url);
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
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
            {
                result = convertInputStreamToString(inputStream);

            }
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
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

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
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