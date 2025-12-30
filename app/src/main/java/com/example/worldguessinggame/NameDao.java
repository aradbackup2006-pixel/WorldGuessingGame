package com.example.worldguessinggame;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NameDao {

    @Insert
        //void insertAll(List<NameEntity> names);
    void insert(NameEntity nameEntity);
    @Query("SELECT * FROM names")
    List<NameEntity> getAllNames();


}