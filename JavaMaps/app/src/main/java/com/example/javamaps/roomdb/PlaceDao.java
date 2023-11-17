package com.example.javamaps.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.javamaps.models.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDao {

    @Query("select * from Place")
    public Flowable<List<Place>> getAll();

    @Insert
    public Completable insert(Place place);

     @Delete
    public Completable delete(Place place);
}
