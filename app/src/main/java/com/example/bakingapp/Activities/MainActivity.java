package com.example.bakingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.R;
import com.example.bakingapp.Adapters.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    public static final String BASE_URL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static int[] image_array= {R.drawable.nutella_pie, R.drawable.brownies,R.drawable.yellow_cake,R.drawable.cheesecake};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkConnectivity();

    }



    public class FetchInfo extends AsyncTask<String,Void, List<JSONObject>>
    {

        @Override
        protected List<JSONObject> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String string_url = params[0];

            URL url = null;
            try {
                url = new URL(string_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray1=null;

            String response_string=null;
            try {

                response_string=getResponseFromHTTPurl(url);
                jsonArray1= new JSONArray(response_string);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            List<JSONObject> jsonObjects=new ArrayList<>();
            for(int i=0;i<jsonArray1.length();i++)
            {
                try {
                    jsonObjects.add(jsonArray1.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return jsonObjects;
        }

        @Override
        protected void onPostExecute(List<JSONObject> jsonObjects) {

            List<String> names=new ArrayList<>();
            for(int i=0;i<jsonObjects.size();i++)
            {
                try {
                    names.add(jsonObjects.get(i).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            recipeAdapter=new RecipeAdapter(image_array,names,jsonObjects);
            recyclerView.setAdapter(recipeAdapter);

        }
    }

    public void checkConnectivity()
    {
        if(!isOnline())
        {
            NoInternet();
        }
        else {
            new FetchInfo().execute(BASE_URL);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void NoInternet()
    {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Info")
                    .setMessage(R.string.main_no_network)
                    .setNegativeButton(R.string.main_no_network_try_again, (dialog, id) -> checkConnectivity())
                    .setPositiveButton(R.string.main_no_network_close, (dialog, id) -> finish());
            builder.create().show();
        } catch (Exception e) {
        }
    }

    public String getResponseFromHTTPurl(URL movielisturl) throws IOException {

        Log.d("main_url", String.valueOf(movielisturl));

        HttpURLConnection urlConnection = (HttpURLConnection) movielisturl.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }


}
