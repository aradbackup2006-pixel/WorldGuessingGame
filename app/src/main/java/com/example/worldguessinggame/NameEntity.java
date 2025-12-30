package com.example.worldguessinggame;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "names")
public class NameEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public NameEntity(String name) {
        this.name = name;
    }
}