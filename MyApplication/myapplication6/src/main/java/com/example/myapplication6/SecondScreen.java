package com.example.myapplication6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;


public class SecondScreen extends AppCompatActivity {
    private VideoView videoView;
    private String gestureSelected;
    private String GESTURE_TO_PLAY;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);

        Intent intent = getIntent();
        gestureSelected = intent.getStringExtra("gesture_name");

        videoView =findViewById(R.id.videoView2);
        switch(gestureSelected) {
            case "Turn On Lights":
                GESTURE_TO_PLAY = "lightson";
                break;
            case "Turn Off Lights":
                GESTURE_TO_PLAY = "lightsoff";
                break;

        }

        Log.i("Video_Gesture",GESTURE_TO_PLAY);
        findViewById(R.id.practiceBtn).setOnClickListener(v -> openPractice());
        findViewById(R.id.replay).setOnClickListener(v->replayVideo());

    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
            videoView.pause();
    }

    private void initializePlayer() {
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + GESTURE_TO_PLAY);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    public void replayVideo() {
        initializePlayer();
    }
    public void openPractice(){
        Intent practiceGestureActivityIntent = new Intent(SecondScreen.this, ThirdScreen_1.class);
        practiceGestureActivityIntent.putExtra("gesture_name", gestureSelected);
        startActivity(practiceGestureActivityIntent);
    }

}