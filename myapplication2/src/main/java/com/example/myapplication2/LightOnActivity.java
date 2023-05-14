package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class LightOnActivity extends AppCompatActivity {
    VideoView videoView ;
    String dropDownSelected = "Select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_on);

        Intent intent = getIntent();
        dropDownSelected = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //TextView textView = findViewById(R.id.textView2);
        //textView.setText(message);

        videoView =findViewById(R.id.videoView1);
        MediaController m = new MediaController(this);
        String path ="";
        videoView.setMediaController(m);
        switch (dropDownSelected.toLowerCase()){
            case "lighton" :
                path = "android.resource://com.example.myapplication2/"+R.raw.lighton;
                break;
            case "lightoff":
                path = "android.resource://com.example.myapplication2/"+R.raw.lightoff;
                break;
        }

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri u = Uri.parse(path);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(u);
        videoView.requestFocus();
        videoView.start();
    }

    public void playVideo(View v) {
        Intent intent = new Intent(this, PracticeActivity.class);
        intent.putExtra("selectedItem",dropDownSelected);
        startActivity(intent);
    }
}