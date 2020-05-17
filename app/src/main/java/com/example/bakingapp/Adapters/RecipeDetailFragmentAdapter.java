package com.example.bakingapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;

public class RecipeDetailFragmentAdapter extends RecyclerView.Adapter<RecipeDetailFragmentAdapter.MyViewHolder> {


    JSONArray jsonArray;

    public RecipeDetailFragmentAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        Log.d("RecipeFragmentAdapter1",jsonArray.toString());
    }

    @NonNull
    @Override
    public RecipeDetailFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_detail_fragment1_adapter;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailFragmentAdapter.MyViewHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(jsonArray==null)
        {
            return 0;
        }
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quantity_tv, measure_tv, ingredient_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quantity_tv=itemView.findViewById(R.id.tv_quantity);
            measure_tv=itemView.findViewById(R.id.tv_measure);
            ingredient_tv=itemView.findViewById(R.id.tv_ingredient);
        }

        public void bind(int position) throws JSONException {
            quantity_tv.setText(jsonArray.getJSONObject(position)
                    .getString("quantity")+" "+jsonArray.getJSONObject(position).getString("measure"));
            //measure_tv.setText(jsonArray.getJSONObject(position).getString("measure"));
            ingredient_tv.setText(jsonArray.getJSONObject(position).getString("ingredient"));
        }
    }
}
