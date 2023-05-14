package com.example.myapplication6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.myapplication6.MESSAGE";
    private Object AdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gestures_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        String selectedItem = arg0.getItemAtPosition(position).toString();
        if (!selectedItem.equals("Select a Gesture")) {
            Intent gestureActivityIntent = new Intent(MainActivity.this, SecondScreen.class);
            gestureActivityIntent.putExtra("gesture_name", selectedItem);
            Log.i("Video_Gesture_Screen1",selectedItem);

            startActivity(gestureActivityIntent);
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}