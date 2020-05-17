package com.example.bakingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bakingapp.Fragments.StepsDetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StepsDetail extends AppCompatActivity implements StepsDetailFragment.onNavigateEventListener {



    String jsonString;
    JSONObject jsonObject;
    String total_size,current_id;
    Bundle extras;
    String mainJsonObject;
    JSONObject mainJsonOnject_json;
    boolean rotated=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState==null)
        {
             extras= getIntent().getExtras();
            if(extras==null)
            {
                jsonObject=null;
                jsonString=null;
                total_size = null;
                current_id=null;
                mainJsonObject=null;
            }
            else
            {
                rotated=false;
                jsonString=extras.getString("jsonObject");
                total_size=extras.getString("total_id");
                current_id=extras.getString("current_id");
                mainJsonObject=extras.getString("MainJsonObject");
                Log.d("stepsdetailStringIntent",jsonString);

            }
        }
        else
        {
            jsonString= (String) savedInstanceState.getSerializable("jsonStringObject2");
            total_size=savedInstanceState.getString("total_size");
            current_id=savedInstanceState.getString("current_id_saved");
            mainJsonObject=savedInstanceState.getString("MainJsonObject_saved");
            Log.d("stepsDetailStringSave",jsonString);
        }




            try {
                jsonObject = new JSONObject(jsonString);
                Log.d("StepsDetailjsonOb",jsonObject.getString("videoURL"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        try {
            setTitle(jsonObject.getString("shortDescription"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StepsDetailFragment stepsDetailFragment =new StepsDetailFragment(rotated);

        FragmentManager fragmentManager=getSupportFragmentManager();
        try {
            mainJsonOnject_json=new JSONObject(mainJsonObject);
            stepsDetailFragment.setDescription_string(mainJsonOnject_json.getJSONArray("steps").getJSONObject(Integer.parseInt(current_id)).getString("description"));
            stepsDetailFragment.setTotal_id(Integer.parseInt(total_size));
            stepsDetailFragment.setCurrent_id(Integer.parseInt(current_id));
            String videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(Integer.parseInt(current_id)).getString("videoURL");
//            String videoURL=jsonObject.getString("videoURL");
            if(videoURL.equals(""))
            {
                videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(Integer.parseInt(current_id)).getString("thumbnailURL");
//                videoURL=jsonObject.getString("thumbnailURL");
            }
            stepsDetailFragment.setVideoUrlString(videoURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,stepsDetailFragment)
                .commit();



    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("jsonStringObject2",jsonString);
        outState.putString("total_size",total_size);
        outState.putString("current_id_saved",current_id);
        outState.putString("MainJsonObject_saved",mainJsonObject);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        jsonString=savedInstanceState.getString("jsonStringObject2");
        total_size=savedInstanceState.getString("total_size");
        current_id=savedInstanceState.getString("current_id_saved");
        mainJsonObject=savedInstanceState.getString("MainJsonObject_saved");
    }

    @Override
    public void pressed(int cur, int tot, String direction) {

        rotated=false;
        StepsDetailFragment stepsDetailFragment =new StepsDetailFragment(rotated);

        FragmentManager fragmentManager=getSupportFragmentManager();


        if(direction.equals("next"))
        {

            try {
                mainJsonOnject_json=new JSONObject(mainJsonObject);
                stepsDetailFragment.setDescription_string(mainJsonOnject_json.getJSONArray("steps").getJSONObject(cur+1).getString("description"));
                stepsDetailFragment.setTotal_id(tot);
                stepsDetailFragment.setCurrent_id(cur+1);
                String videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(cur+1).getString("videoURL");
                if(videoURL.equals(""))
                {
                    videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(cur+1).getString("thumbnailURL");
                }
                current_id=String.valueOf(cur+1);
                stepsDetailFragment.setVideoUrlString(videoURL);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        else if(direction.equals("prev"))
        {
            try {
                mainJsonOnject_json=new JSONObject(mainJsonObject);
                stepsDetailFragment.setDescription_string(mainJsonOnject_json.getJSONArray("steps").getJSONObject(cur-1).getString("description"));
                stepsDetailFragment.setTotal_id(tot);
                stepsDetailFragment.setCurrent_id(cur-1);
                String videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(cur-1).getString("videoURL");
                if(videoURL.equals(""))
                {
                    videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(cur-1).getString("thumbnailURL");
                }
                current_id=String.valueOf(cur-1);

                stepsDetailFragment.setVideoUrlString(videoURL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,stepsDetailFragment)
                .commit();


    }
}
