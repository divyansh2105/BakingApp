package com.example.bakingapp.Activities;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bakingapp.BuildConfig;
import com.example.bakingapp.Fragments.StepsDetailFragment;
import com.example.bakingapp.R;
import com.example.bakingapp.Fragments.RecipeDetailFragment;
import com.example.bakingapp.RecipeWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener
                ,StepsDetailFragment.onNavigateEventListener{

    private boolean mTwoPane;
    JSONObject jsonObject;
    String jsonString;

    String total_size,current_id;
    String mainJsonObject;
    JSONObject mainJsonOnject_json;

    SharedPreferences sharedpreferences;
    RecipeDetailFragment recipeDetailFragment;

    int flag=0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("create","created");

        if(savedInstanceState==null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras==null)
            {
                jsonObject=null;
                jsonString=null;
            }
            else
            {

                jsonString=extras.getString("json");
                Log.d("RecipeDetailStringInten",jsonString);

            }
        }
        else
        {
            jsonString= (String) savedInstanceState.getSerializable("jsonStringObject");
            total_size=savedInstanceState.getString("total_size");
            current_id=savedInstanceState.getString("current_id_saved");
            mainJsonObject=savedInstanceState.getString("MainJsonObject_saved");
            Log.d("RecipeDetailStringSave",jsonString);
        }

        String file="MyFile";

        sharedpreferences= getSharedPreferences(file, Context.MODE_PRIVATE);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTwoPane = (findViewById(R.id.frag2_container) != null);
        if(mTwoPane)
        {
            FrameLayout frameLayout=findViewById(R.id.frag1_container);
            frameLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        }



            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("RecipeDetail_jsonOb",jsonObject.toString());



        recipeDetailFragment=new RecipeDetailFragment();

        FragmentManager fragmentManager=getSupportFragmentManager();

        recipeDetailFragment.setJsonObject(jsonObject);
        fragmentManager.beginTransaction()
                .replace(R.id.frag1_container,recipeDetailFragment)
                .commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);

        try {
            if ((sharedpreferences.getInt("ID", -1) == Integer.parseInt(jsonObject.getString("id")))){
                menu.findItem(R.id.mi_action_widget).setIcon(R.drawable.ic_star_white_48dp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mi_action_widget){
            boolean isRecipeInWidget = false;
            try {
                isRecipeInWidget = (sharedpreferences.getInt("ID", -1) == Integer.parseInt(jsonObject.getString("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isRecipeInWidget){
                sharedpreferences.edit()
                        .remove("ID")
                        .remove("WIDGET_TITLE")
                        .remove("WIDGET_CONTENT")
                        .apply();

                item.setIcon(R.drawable.ic_star_border_white_48dp);
                Toast.makeText(this, "Widget Removed", Toast.LENGTH_SHORT).show();
            }
            else
             {
                 String name="";
                 try {
                     id=Integer.parseInt(jsonObject.getString("id"));
                     name=jsonObject.getString("name");
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                sharedpreferences
                        .edit()
                     .putInt("ID",id )
                     .putString("WIDGET_TITLE", name)
                     .putString("WIDGET_CONTENT", getIngredients(jsonObject))
                        .apply();

                item.setIcon(R.drawable.ic_star_white_48dp);
                Toast.makeText(this, "Widget Added", Toast.LENGTH_SHORT).show();
            }

            // Put changes on the Widget
            ComponentName provider = new ComponentName(getApplication(), RecipeWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());
            int[] ids = appWidgetManager.getAppWidgetIds(provider);
            RecipeWidget bakingWidgetProvider = new RecipeWidget();
            bakingWidgetProvider.onUpdate(getApplication(), appWidgetManager, ids);
        }

        else
        {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("savedInstance",jsonString);
        outState.putString("jsonStringObject",jsonString);
        Log.d("restoreString1", String.valueOf(outState.getSerializable("jsonStringObject")));
        outState.putString("total_size",total_size);
        outState.putString("current_id_saved",current_id);
        outState.putString("MainJsonObject_saved",mainJsonObject);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        jsonString=savedInstanceState.getString("jsonStringObject");
        mainJsonObject=savedInstanceState.getString("MainJsonObject_saved");
        total_size=savedInstanceState.getString("total_size");
        current_id=savedInstanceState.getString("current_id_saved");
        Log.d("restoreInstance",jsonString);
    }


    public String getIngredients(JSONObject json)
    {
        JSONArray jsonArray;

        StringBuilder stringBuilder=new StringBuilder();
        try {
            jsonArray=json.getJSONArray("ingredients");
            for(int i=0;i<jsonArray.length();i++)
            {
                String quantity=jsonArray.getJSONObject(i).getString("quantity");
                String measure=jsonArray.getJSONObject(i).getString("measure");
                String ingredient=jsonArray.getJSONObject(i).getString("ingredient");
                stringBuilder.append(quantity).append(" ").append(measure).append("  ").append(ingredient).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    public void pressed(int cur, int tot, String direction) {

        StepsDetailFragment stepsDetailFragment =new StepsDetailFragment(false);

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
                .replace(R.id.frag2_container,stepsDetailFragment)
                .commit();

    }

    @Override
    public void onStepSelected(int position) {
        Log.d("position_recipeActivity", String.valueOf(position));

        String description="";
        String videoURL="";
        try {
            description=jsonObject.getJSONArray("steps").getJSONObject(position).getString("description");
            videoURL=jsonObject.getJSONArray("steps").getJSONObject(position).getString("videoURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (mTwoPane){



            StepsDetailFragment stepsDetailFragment =new StepsDetailFragment(false);

            FragmentManager fragmentManager=getSupportFragmentManager();
            try {
                mainJsonObject=jsonString;
                total_size=String.valueOf(jsonObject.getJSONArray("steps").length());
                current_id=String.valueOf(position);
                mainJsonOnject_json=new JSONObject(mainJsonObject);
//            stepsDetailFragment.setDescription_string(jsonObject.getString("description"));
                stepsDetailFragment.setDescription_string(mainJsonOnject_json.getJSONArray("steps").getJSONObject(Integer.parseInt(current_id)).getString("description"));
                stepsDetailFragment.setTotal_id(Integer.parseInt(total_size));
                stepsDetailFragment.setCurrent_id(Integer.parseInt(current_id));
                videoURL=mainJsonOnject_json.getJSONArray("steps").getJSONObject(Integer.parseInt(current_id)).getString("videoURL");
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
                    .replace(R.id.frag2_container,stepsDetailFragment)
                    .commit();

        }
        else {
            Intent intent=new Intent(this, StepsDetail.class);
            try {
                intent.putExtra("jsonObject",jsonObject.getJSONArray("steps").getJSONObject(position).toString());
                intent.putExtra("total_id",String.valueOf(jsonObject.getJSONArray("steps").length()));
                intent.putExtra("current_id",String.valueOf(position));
                intent.putExtra("MainJsonObject",String.valueOf(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }


    }
}
