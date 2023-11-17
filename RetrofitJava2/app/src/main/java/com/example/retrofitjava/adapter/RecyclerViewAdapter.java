package com.example.retrofitjava.adapter;

import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitjava.R;
import com.example.retrofitjava.model.CryptoModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {
    private ArrayList<CryptoModel> cryptoModelArrayList;
    public String[] colors = {"#ff2400","#317873","#ff7f50","#002b4d","#7f5a7a"};

    public RecyclerViewAdapter(ArrayList<CryptoModel> cryptoModelArrayList) {
        this.cryptoModelArrayList = cryptoModelArrayList;
    }

    public class RowHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textPrice;

        public RowHolder(View itemView) {
            super(itemView);



        }

        public void bind(CryptoModel model,String[] colors,int position){
            itemView.setBackgroundColor(Color.parseColor(colors[position % 5]));
            textName = itemView.findViewById(R.id.recyclerViewNameText);
            textPrice = itemView.findViewById(R.id.recyclerViewPriceText);
            textName.setText(model.getCurrency().toString());
            textPrice.setText(model.getPrice().toString());

        }
    }
    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.bind(cryptoModelArrayList.get(position),colors,position);
    }

    @Override
    public int getItemCount() {
        return cryptoModelArrayList.size();
    }
}
