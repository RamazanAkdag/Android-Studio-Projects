package com.example.retrofitjava.service;

import com.example.retrofitjava.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ICryptoApi {
    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    Observable<List<CryptoModel>> getData();

    //Call<List<CryptoModel>> getData();
}
