package com.example.denemeyenisurum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       editText = findViewById(R.id.editTextNumber);
       textView = findViewById(R.id.textView);

       sharedPreferences = this.getSharedPreferences("com.example.denemeyenisurum",MODE_PRIVATE);

       int storedAge = sharedPreferences.getInt("storedAge",0);

       if(storedAge == 0){
           textView.setText("Your age : ");
       }else{

           textView.setText("Your age :" +storedAge);
       }



    }

    public void save(View view){
        if(!editText.getText().toString().matches("")){//boşluk
            int age = Integer.parseInt(editText.getText().toString());
            textView.setText("Your age : "+age);
            sharedPreferences.edit().putInt("storedAge",age).apply();

        }else{
            textView.setText("Your age : ");
        }


    }

    public void delete(View view){
        int storedData = sharedPreferences.getInt("storedAge",0);

        if(storedData != 0){
            sharedPreferences.edit().remove("storedAge").apply();
            textView.setText("Your age : ");
        }
    }

}