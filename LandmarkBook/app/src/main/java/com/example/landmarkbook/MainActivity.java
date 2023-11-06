package com.example.landmarkbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.landmarkbook.Adapter.LandmarkAdapter;
import com.example.landmarkbook.Models.Landmark;
import com.example.landmarkbook.databinding.ActivityDetailsBinding;
import com.example.landmarkbook.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    ArrayList<Landmark> landmarkArrayList;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());//inflat görünce xml ile kodu bağlamak aklına gelsin
        View view = binding.getRoot();
        setContentView(view);

        //data
        Landmark pisa = new Landmark("Pisa",R.drawable.pisa,"Italy");
        Landmark colleseum = new Landmark("Colleseum",R.drawable.colosseum,"Italy");
        Landmark eiffel = new Landmark("Eiffel",R.drawable.eiffel,"France");
        Landmark londonEye = new Landmark("London Eye",R.drawable.londoneye,"UK");

        landmarkArrayList = new ArrayList<>();
        landmarkArrayList.add(pisa);
        landmarkArrayList.add(colleseum);
        landmarkArrayList.add(eiffel);
        landmarkArrayList.add(londonEye);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LandmarkAdapter adapter = new LandmarkAdapter(landmarkArrayList);
        binding.recyclerView.setAdapter(adapter);

        /*
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                landmarkArrayList.stream().map(landmark -> landmark.getName()).collect(Collectors.toList()));

        binding.listView.setAdapter(arrayAdapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("Landmark",landmarkArrayList.get(i));
                startActivity(intent);
            }
        });*/

    }
}