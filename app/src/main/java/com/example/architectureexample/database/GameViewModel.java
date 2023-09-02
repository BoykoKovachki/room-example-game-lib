package com.example.architectureexample.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GameViewModel extends AndroidViewModel {

    private GameRepository repository;

    private LiveData<List<Game>> allGames;

    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new GameRepository(application);
        allGames = repository.getAllGames();
    }

    public LiveData<List<Game>> getAllGamesForPaging() {
        return allGames;
    }

    public void insert(Game game) {
        repository.insert(game);
    }

    public void update(Game game) {
        repository.update(game);
    }

    public void delete(Game game) {
        repository.delete(game);
    }

    public void deleteAllGames() {
        repository.deleteAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    public void getLastAddedGame(GameRepository.GetLastGameListener listener) {
        repository.getLastAddedGame(listener);
    }

    public void getRandomGame(GameRepository.GetRandomGameListener listener) {
        repository.getRandomGame(listener);
    }

    public void getGameByName(GameRepository.GetGameByNameListener listener, String name) {
        repository.getGameByName(listener, name);
    }

    public void getGamesByTypes(GameRepository.GetGamesByTypeListener listener, int type) {
        repository.getGamesByType(listener, type);
    }

    public void getFavoriteGames(GameRepository.GetFavoriteGamesListener listener) {
        repository.getFavoriteGames(listener);
    }

}