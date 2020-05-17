package com.example.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.Activities.StepsDetail;
import com.example.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragmentAdapter2 extends RecyclerView.Adapter<RecipeDetailFragmentAdapter2.MyViewHolder> {


    JSONArray jsonArray;
    public int selectedPosition = -1;
    public interface StepItemClickListener {
        void onStepItemClick(int clickedItemIndex);
    }

    final private StepItemClickListener mOnClickListener;
    public RecipeDetailFragmentAdapter2(JSONArray jsonArray,StepItemClickListener clickListener) {
        this.jsonArray = jsonArray;
        this.mOnClickListener = clickListener;
        Log.d("RecipeFragmentAdapter2",jsonArray.toString());
    }

    @NonNull
    @Override
    public RecipeDetailFragmentAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_detail_fragment1_adapter2;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailFragmentAdapter2.MyViewHolder holder, int position) {
        try {
            Log.d("Adapter2_positions", String.valueOf(position));
            holder.bind(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("adapter2_length", String.valueOf(jsonArray.length()));
        if(jsonArray==null)
        {
            return 0;
        }
        return jsonArray.length();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView short_description_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            short_description_tv=itemView.findViewById(R.id.tv_short_description);
            itemView.setOnClickListener(this);

        }

        public void bind(int position) throws JSONException {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                short_description_tv.setText(Html.fromHtml("<b>"+"Step "+jsonArray.getJSONObject(position)
                        .getString("id")+"</b> &nbsp;"+jsonArray.getJSONObject(position).getString("shortDescription"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                short_description_tv.setText(Html.fromHtml("<b>"+"Step "+jsonArray.getJSONObject(position)
                        .getString("id")+"</b> &nbsp;"+jsonArray.getJSONObject(position).getString("shortDescription")));
            }


        }

        @Override
        public void onClick(View v) {


            selectedPosition = getAdapterPosition();
            mOnClickListener.onStepItemClick(selectedPosition);
            notifyDataSetChanged();

        }
    }
}
