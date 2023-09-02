package com.example.architectureexample.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDao {

    @Insert
    void insert(Game game);

    @Update
    void update(Game game);

    @Delete
    void delete(Game game);

    @Query("DELETE FROM game_table")
    void deleteAllGames();

    @Query("SELECT * FROM game_table ORDER BY id DESC")
    LiveData<List<Game>> getAllGames();

    @Query("SELECT * FROM game_table ORDER BY RANDOM() LIMIT 1")
    Game getRandomGame();

    @Query("SELECT * FROM game_table ORDER BY ID DESC LIMIT 1")
    Game getLastAddedGame();

    @Query("SELECT * FROM game_table WHERE title LIKE :title")
    Game getGameByTitle(String title);

    @Query("SELECT * FROM game_table WHERE type LIKE :type")
    List<Game> getGamesByType(int type);

    @Query("SELECT * FROM game_table WHERE favorite = 1")
    List<Game> getFavoriteGames();

}
