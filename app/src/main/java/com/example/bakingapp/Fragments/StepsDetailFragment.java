package com.example.bakingapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bakingapp.Activities.MainActivity;
import com.example.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsDetailFragment extends Fragment implements MediaController.MediaPlayerControl {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface onNavigateEventListener {
        public void pressed(int cur,int tot,String direction);
    }

    onNavigateEventListener navigateListener;

    Button nextButton,prevButton;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    View rootView;
    JSONObject jsonObject;

    public static String getArgParam1() {
        return ARG_PARAM1;
    }

    SharedPreferences sharedpreferences;


    public int getCurrent_id() {
        return current_id;
    }

    public void setCurrent_id(int current_id) {
        this.current_id = current_id;
    }

    public int getTotal_id() {
        return total_id;
    }

    public void setTotal_id(int total_id) {
        this.total_id = total_id;
    }

    String videoURL;
    int current_id,total_id;
    Button nextbutoon, prevbutton;

    TextView textView;

    public String getVideoUrlString() {
        return videoUrlString;
    }

    public void setVideoUrlString(String videoUrlString) {
        this.videoUrlString = videoUrlString;
    }

    public String getDescription_string() {
        return description_string;
    }

    public void setDescription_string(String description_string) {
        this.description_string = description_string;
    }

    String videoUrlString=null;
    String description_string=null;
    boolean rotated;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public StepsDetailFragment()
    {

    }

    public StepsDetailFragment(boolean rotated) {
        this.rotated = rotated;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
        {
            description_string= (String) savedInstanceState.getSerializable("desc");
            videoUrlString= (String) savedInstanceState.getSerializable("video");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_steps_detail, container, false);

//        if(savedInstanceState!=null)
//        {
//            description_string= (String) savedInstanceState.getSerializable("desc");
//            videoUrlString= (String) savedInstanceState.getSerializable("video");
//        }

        sharedpreferences = getActivity().getSharedPreferences("MY_FILE", Context.MODE_PRIVATE);


        mPlayerView =rootView.findViewById(R.id.playerView);

        textView=(TextView) rootView.findViewById(R.id.tv_description);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.brownies));

        nextbutoon=rootView.findViewById(R.id.next_button);
        prevButton=rootView.findViewById(R.id.prev_button);


            videoURL=videoUrlString;


            initializePlayer();
            textView.setText(description_string);

            if(getCurrent_id()==0)
            {
                prevButton.setVisibility(View.INVISIBLE);
            }
            else if(getCurrent_id()==getTotal_id()-1)
            {
                nextbutoon.setVisibility(View.INVISIBLE);
            }


        Log.d("current_id", String.valueOf(current_id));
            Log.d("total_id", String.valueOf(total_id));

        nextbutoon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("next_pressed","next Pressed");
                navigateListener.pressed(getCurrent_id(),getTotal_id(),"next");


            }
          }

        );

            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("prev_pressed","prev Pressed");
                    navigateListener.pressed(getCurrent_id(),getTotal_id(),"prev");

                }
            });


            if(rotated) {
                int position = sharedpreferences.getInt("pos", 0);
                mExoPlayer.seekTo(position);
            }

        return rootView;
    }

    private void initializePlayer() {

        if(videoURL.equals("")) {

            mPlayerView.setVisibility(View.GONE);
            return;
        }
        else
        {
            //initializePlayer(Uri.parse(videoURL));
            mPlayerView.setVisibility(View.VISIBLE);
        }

        Uri mediaUri=Uri.parse(videoURL);
        Log.d("StepsDetailVideoURL",videoURL);
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(rootView.getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(rootView.getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    rootView.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);



        }
    }


    private void releasePlayer() {
//        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void nextButtonClicked(View rootView)
    {
        Toast.makeText(rootView.getContext(),"next clicked",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            navigateListener = (onNavigateEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
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
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(mExoPlayer!=null)
        {
            return (int) mExoPlayer.getCurrentPosition();
        }
        return 0;
    }


    @Override
    public void seekTo(int pos) {
        if(mExoPlayer!=null) {
            long seekPosition = Math.min(Math.max(0, pos), getDuration());
            mExoPlayer.seekTo(seekPosition);
        }
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("video",videoUrlString);
        outState.putString("desc",description_string);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
            initializePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mExoPlayer==null)
        {
            return;
        }
            startPlayer();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if(!videoURL.equals("") && mExoPlayer!=null) {
                releasePlayer();

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer!=null)
        {
            pausePlayer();
        }
        if (Util.SDK_INT <= 23) {
            if(!videoURL.equals("") && mExoPlayer!=null) {
                releasePlayer();

            }
        }
        int pos=getCurrentPosition();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("pos", pos);
        editor.apply();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!videoURL.equals("") && mExoPlayer!=null) {
            releasePlayer();
        }
    }

    private void pausePlayer(){
        mExoPlayer.setPlayWhenReady(false);
        mExoPlayer.getPlaybackState();
    }
    private void startPlayer(){
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.getPlaybackState();
    }

}
