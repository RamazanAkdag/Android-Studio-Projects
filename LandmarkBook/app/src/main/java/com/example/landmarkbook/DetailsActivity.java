package com.example.landmarkbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.landmarkbook.Models.Landmark;
import com.example.landmarkbook.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());//inflat görünce xml ile kodu bağlamak aklına gelsin
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        Landmark selectedLandmark =(Landmark) intent.getSerializableExtra("Landmark");
        binding.nameText.setText(selectedLandmark.getName());
        binding.countryText.setText(selectedLandmark.getCountry());
        binding.imageView.setImageResource(selectedLandmark.getImage());
    }
}