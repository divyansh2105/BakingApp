package com.example.bakingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.bakingapp.Adapters.RecipeAdapter;
import com.example.bakingapp.Adapters.RecipeDetailFragmentAdapter;
import com.example.bakingapp.Adapters.RecipeDetailFragmentAdapter2;
import com.example.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    JSONObject jsonObject;
    String jsonString;
    RecyclerView recyclerView,recyclerView2;
    RecipeDetailFragmentAdapter recipeDetailFragmentAdapter;
    RecipeDetailFragmentAdapter2 recipeDetailFragmentAdapter2;

    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepSelected(int position);
    }

    public JSONObject getJsonObjects() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObjects) {
        this.jsonObject = jsonObjects;
        jsonString= String.valueOf(jsonObjects);
        Log.d("jsonOnject",jsonObjects.toString());
    }

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        recyclerView=rootView.findViewById(R.id.recycler_view_frag1);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView2=rootView.findViewById(R.id.recycler_view_frag2);
        recyclerView2.setHasFixedSize(true);

        recyclerView2.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView2.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        if(savedInstanceState!=null)
        {

            jsonString= (String) savedInstanceState.getSerializable("object");
            try {
                jsonObject=new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("RecipeDetailStringSave",jsonString);
        }

        JSONArray jsonArray_ingredients=null;
        JSONArray jsonArray_steps=null;

            try {

                jsonArray_ingredients=jsonObject.getJSONArray("ingredients");
                recipeDetailFragmentAdapter=new RecipeDetailFragmentAdapter(jsonArray_ingredients);
                recyclerView.setAdapter(recipeDetailFragmentAdapter);

                jsonArray_steps=jsonObject.getJSONArray("steps");
                recipeDetailFragmentAdapter2=new RecipeDetailFragmentAdapter2(jsonArray_steps,(int clickedItemIndex) -> {
                    mCallback.onStepSelected(clickedItemIndex);
                });
                recyclerView2.setAdapter(recipeDetailFragmentAdapter2);
            } catch (JSONException e) {
                e.printStackTrace();
            }




        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnStepClickListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement OnStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("object",jsonString);
    }
}
