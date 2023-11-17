package com.example.retrofitjava.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.retrofitjava.R;
import com.example.retrofitjava.adapter.RecyclerViewAdapter;
import com.example.retrofitjava.model.CryptoModel;
import com.example.retrofitjava.service.ICryptoApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ArrayList<CryptoModel> cryptoModels;
    private final String BASE_URL = "https://raw.githubusercontent.com/";
    Retrofit retrofit;
    RecyclerView recyclerView;

    RecyclerViewAdapter recyclerViewAdapter;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrofit & Json
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();

        recyclerView = findViewById(R.id.recyclerView);

        loadData();
    }

    private void loadData(){

        ICryptoApi cryptoApi = retrofit.create(ICryptoApi.class);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cryptoApi.getData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponse));

        /*Call<List<CryptoModel>> call = cryptoApi.getData();

        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                if(response.isSuccessful()){
                    List<CryptoModel> responseList = response.body();
                    cryptoModels = new ArrayList<>(responseList);

                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    recyclerViewAdapter = new RecyclerViewAdapter(cryptoModels);

                    recyclerView.setAdapter(recyclerViewAdapter);

                    //for(CryptoModel cryptoModel :  cryptoModels){
                      //  System.out.println("currency : "+cryptoModel.getCurrency() +" Price : "+ cryptoModel.getPrice());
                   // }
                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });*/

    }
    private void handleResponse(List<CryptoModel> cryptoModelList){
        cryptoModels = new ArrayList<>(cryptoModelList);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerViewAdapter = new RecyclerViewAdapter(cryptoModels);

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}