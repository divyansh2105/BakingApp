package com.example.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.Activities.RecipeDetail;
import com.example.bakingapp.R;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    public List<String> imageIds;
    List<JSONObject> jsonObjects;
    List<String> names=new ArrayList<>();
    public int[] image_array;
    public String[] st_ar={"Nutella Pie","Brownies","Yellow Cake","Cheese Cake"};
    public static final String BASE_IMAGE_URL="https://image.tmdb.org/t/p/w185/";


    private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public RecipeAdapter(){}

    public RecipeAdapter(int[] ar,List<String> names,List<JSONObject> jsonObjects) {
        this.image_array = ar;
        this.names=names;
        this.jsonObjects=jsonObjects;
    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_adapter_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount()
    {
        if(image_array==null)
        {
            return 0;
        }
        return image_array.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        TextView tv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //tv=itemView.findViewById(R.id.tv);

            imageView=itemView.findViewById(R.id.food_image);
            tv= itemView.findViewById(R.id.food_name_tv);

            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            imageView.setImageResource(image_array[position]);
            tv.setText(names.get(position));


        }


        @Override
        public void onClick(View v) {

            JSONObject object=new JSONObject();
            Intent intent=new Intent(v.getContext(), RecipeDetail.class);
//            Log.d("recipe_adapter_jsonOb",jsonObjects_stringlist.toString());
            object=jsonObjects.get(getAdapterPosition());
            intent.putExtra("json", String.valueOf(object));
            v.getContext().startActivity(intent);
//
        }
    }

}

