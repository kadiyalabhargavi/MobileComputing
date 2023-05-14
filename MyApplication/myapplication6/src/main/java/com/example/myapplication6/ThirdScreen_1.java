package com.example.myapplication6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ThirdScreen_1 extends AppCompatActivity implements View.OnClickListener{

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    PreviewView previewView;
    private VideoCapture videoCapture;
    private Button bRecord;
    private Button bUpload;
    File videoFile ;
    String videoFilePath;
    private String gestureSelected3;
    HashMap<String, String> gestureNameAndValue;
    private CountDownTimer timer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_screen1);
        Intent intent = getIntent();
        gestureSelected3 = intent.getStringExtra("gesture_name");

        gestureNameAndValue = new HashMap<>();

        gestureNameAndValue.put("Select a Gesture", "selectGesture");
        gestureNameAndValue.put("Turn On Lights", "lightsOn");
        gestureNameAndValue.put("Turn Off Lights", "lightsOff");
        gestureNameAndValue.put("Turn On Fan", "fanOn");
        gestureNameAndValue.put("Turn Off Fan", "fanOff");
        gestureNameAndValue.put("Increase Fan Speed", "fanUp");
        gestureNameAndValue.put("Decrease Fan Speed", "fanDown");
        gestureNameAndValue.put("Set Thermostat to specified temperature", "setThermo");
        gestureNameAndValue.put("0", "num0");
        gestureNameAndValue.put("1", "num1");
        gestureNameAndValue.put("2", "num2");
        gestureNameAndValue.put("3", "num3");
        gestureNameAndValue.put("4", "num4");
        gestureNameAndValue.put("5", "num5");
        gestureNameAndValue.put("6", "num6");
        gestureNameAndValue.put("7", "num7");
        gestureNameAndValue.put("8", "num8");
        gestureNameAndValue.put("9", "num9");


        previewView = findViewById(R.id.previewView);
        bUpload = findViewById(R.id.bUpload);
        bRecord = findViewById(R.id.bRecord);
        bRecord.setText("start recording"); // Set the initial text of the button

        bRecord.setOnClickListener(this);
        bUpload.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(30)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, videoCapture);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bRecord:
                if (bRecord.getText() == "start recording") {
                    recordVideo();
                    timer = new CountDownTimer(6000, 1000) {
                        @Override
                        public void onTick(long l) {
                            bRecord.setText("stop recording");
                            Log.i("INSIDE_COUNTER", (String) bRecord.getText());
                        }

                        @Override
                        public void onFinish() {
                            bRecord.setText("start recording");
                            videoCapture.stopRecording();
                        }
                    };
                    timer.start();
                }else {
                    bRecord.setText("start recording");
                    videoCapture.stopRecording();
                }
                break;
            case R.id.bUpload:
                alertDialog();
                break;
        }
    }

    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to UPLOAD this practice video ?");
        dialog.setTitle("Confirm Upload");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Uploading to Server",Toast.LENGTH_LONG).show();
                        postRequest();
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Denied Uploading",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    @SuppressLint("RestrictedApi")
    private void recordVideo() {
        if (videoCapture != null) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
            if(!mediaStorageDir.exists()){
                mediaStorageDir.mkdir();
            }
            Date date = new Date();
            String timestamp = String.valueOf(date.getTime());
            videoFilePath = mediaStorageDir.getAbsolutePath()+ "/" +gestureNameAndValue.get(gestureSelected3)+"_PRACTICE_"+ ".mp4";
            videoFile = new File(videoFilePath);
//            long timestamp = System.currentTimeMillis();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                videoCapture.startRecording(
                        new VideoCapture.OutputFileOptions.Builder(
                                videoFile
                        ).build(),
                        getExecutor(),
                        new VideoCapture.OnVideoSavedCallback() {
                            @Override
                            public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                                Toast.makeText(ThirdScreen_1.this, "Video is ready for upload", Toast.LENGTH_SHORT).show();
                                Log.i("Video Result",outputFileResults.getSavedUri().toString());
                            }

                            @Override
                            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                                Toast.makeText(ThirdScreen_1.this, "Error saving video: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void postRequest() {
        String[] dirarray = videoFilePath.split("/");
        String file_name = dirarray[6];
        Log.i("MEDIA_FILE_PATH",videoFilePath);
        Log.i("MEDIA_FILE_NAME",file_name);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file_name, RequestBody.create(new File(videoFilePath), MediaType.parse("video/mp4")))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.0.165:5000/upload")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                runOnUiThread(() -> {

                    try {
                        String response_body = response.body().string();
                        System.out.println(response_body);
                        Toast.makeText(getApplicationContext(), response_body, Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent gotoMainActivity = new Intent(ThirdScreen_1.this, MainActivity.class);
                        startActivity(gotoMainActivity);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                call.cancel();
                runOnUiThread(() -> {
//                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}