package com.example.adapterproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<CharSequence> cities = getCities();
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, cities);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        Button buttonKaydet = (Button) findViewById(R.id.buttonKaydet);
        final EditText editTextSehir = (EditText) findViewById(R.id.editTextSehir);
        final TextView durumTextView = (TextView) findViewById(R.id.durumTextView);
        buttonKaydet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sehirAdi = editTextSehir.getText().toString();
                cities.add(sehirAdi);
                adapter.notifyDataSetChanged();
                editTextSehir.setText("");
                durumTextView.setText(sehirAdi + " " + "Eklendi !");
            }
        });

    }
    private List<CharSequence> getCities() {
        List<CharSequence> cities = new ArrayList<CharSequence>();
        cities.add("Ankara");
        cities.add("Izmir");
        cities.add("Istanbul");
        cities.add("Mugla");
        return cities;
    }
}