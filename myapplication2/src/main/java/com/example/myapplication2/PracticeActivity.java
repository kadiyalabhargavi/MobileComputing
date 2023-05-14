package com.example.myapplication2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PracticeActivity extends AppCompatActivity {
//    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD = 101;
    private Uri videoUri = null ;
    String filePath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoIntent,VIDEO_RECORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==VIDEO_RECORD) {
            if(resultCode==RESULT_OK) {
                videoUri = data.getData();
            Log.i("Video_Recorded_Tag","Video recorded path "+videoUri);
            Log.i("Video_URI_SCHEME","Video uri scheme "+videoUri.getScheme());
                if (videoUri != null && "content".equals(videoUri.getScheme())) {
                    Cursor cursor = this.getContentResolver().query(videoUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                    Log.i("Video_Recorded_Tag","Video recorded path "+filePath);

                    cursor.close();
                } else {
                    filePath = videoUri.getPath();
                }
                Log.d("","Chosen path = "+ filePath);
            } else if( requestCode == RESULT_CANCELED) {
                Log.i("Video_Recorded_Tag","Video cancelled ");
            }
            else {
                Log.i("Video_Recorded_Tag","Error Recording Video");
            }
        }
    }

    public void uploadVideo(View view) {
        Log.i("Video_Upload_Tag","Video being uploaded ");

        String postUrl= "http://127.0.0.1:6001/upload";
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody postBody = RequestBody.create(mediaType, filePath);

        postRequest(postUrl, postBody);
        Log.i("Video_Upload_Tag","Video Uploaded successfully ");

    }
    void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Video_PostFail_Tag","Video Upload failed with Error: "
                                +e.getStackTrace());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i("Video_PostSuccess_Tag","Video Upload success: "
                                +response.body().toString());
                    }
                });
            }
        });
    }
}