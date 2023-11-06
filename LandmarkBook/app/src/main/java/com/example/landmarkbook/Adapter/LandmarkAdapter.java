package com.example.landmarkbook.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landmarkbook.DetailsActivity;
import com.example.landmarkbook.Models.Landmark;
import com.example.landmarkbook.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.LandmarkHolder> {
    private ArrayList<Landmark> landmarkList;
    private LandmarkHolder holder;
    private int position;

    //Getters and Setters
    public ArrayList<Landmark> getLandmarkList() {
        return landmarkList;
    }

    public void setLandmarkList(ArrayList<Landmark> landmarkList) {
        this.landmarkList = landmarkList;
    }

    //Constructors
    public LandmarkAdapter(ArrayList<Landmark> landmarkList) {
        this.setLandmarkList(landmarkList);
    }

    //ViewHolder Class
    public class LandmarkHolder extends  RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;

        //Getters and Setters
        public RecyclerRowBinding getBinding() {
            return binding;
        }

        public void setBinding(RecyclerRowBinding binding) {
            this.binding = binding;
        }

        //Constructors
        public LandmarkHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.setBinding(binding);

        }
    }


    @NonNull
    @Override
    public LandmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new LandmarkHolder(recyclerRowBinding);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull LandmarkHolder holder, int position) {

        holder.getBinding().recyclerViewTextView.setText(landmarkList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("Landmark",landmarkList.get(position));
                    holder.itemView.getContext().startActivity(intent);
                }
        });
    }

    @Override
    public int getItemCount() {
        return this.getLandmarkList().size() ;
    }
}
