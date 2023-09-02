package com.example.architectureexample.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_table")
public class Game {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    @ColumnInfo(name = "year_released") // Note: set manually column name
    private int yearReleased;

    private String logo;

    private int type;

    private boolean favorite;

    public Game(String title, String description, int yearReleased, String logo, int type, boolean favorite) {
        this.title = title;
        this.description = description;
        this.yearReleased = yearReleased;
        this.logo = logo;
        this.type = type;
        this.favorite = favorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public String getLogo() {
        return logo;
    }

    public int getType() {
        return type;
    }

    public boolean isFavorite() {
        return favorite;
    }

}
