package com.example.myapplication2;

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
    public static final String EXTRA_MESSAGE = "com.example.myapplication2.MESSAGE";
    private Object AdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gestures_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //spinner.setSelection(, false);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        String item = arg0.getItemAtPosition(position).toString();

       // Toast.makeText(arg0.getContext(), item, Toast.LENGTH_LONG).show();
        if(item.equalsIgnoreCase("select")){

        }else {
            Intent intent = new Intent(this, LightOnActivity.class);
            switch(item) {
                case "LightOn":   intent.putExtra(EXTRA_MESSAGE, "LightOn");
                break;
                case "LightOff":   intent.putExtra(EXTRA_MESSAGE, "LightOff");
                    break;
                case "FanOn":   intent.putExtra(EXTRA_MESSAGE, "FanOn");
                    break;
                case "FanOff":   intent.putExtra(EXTRA_MESSAGE, "FanOff");
                 break;
                case "FanUp":   intent.putExtra(EXTRA_MESSAGE, "FanUp");
                    break;
                case "FanDown":   intent.putExtra(EXTRA_MESSAGE, "FanDown");
                    break;
                case "setThermo":   intent.putExtra(EXTRA_MESSAGE, "SetThermo");
                    break;
                case "Num0":   intent.putExtra(EXTRA_MESSAGE, "0");
                    break;
                case "Num1":   intent.putExtra(EXTRA_MESSAGE, "1");
                    break;
                case "Num2":   intent.putExtra(EXTRA_MESSAGE, "2");
                    break;
                case "Num3":   intent.putExtra(EXTRA_MESSAGE, "3");
                    break;
                case "Num4":   intent.putExtra(EXTRA_MESSAGE, "4");
                    break;
                case "Num5":   intent.putExtra(EXTRA_MESSAGE, "5");
                    break;
                case "Num6":   intent.putExtra(EXTRA_MESSAGE, "6");
                    break;
                case "Num7":   intent.putExtra(EXTRA_MESSAGE, "7");
                    break;
                case "Num8":   intent.putExtra(EXTRA_MESSAGE, "8");
                    break;
                case "Num9":   intent.putExtra(EXTRA_MESSAGE, "9");
                    break;

            }
            startActivity(intent);
            // }
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}