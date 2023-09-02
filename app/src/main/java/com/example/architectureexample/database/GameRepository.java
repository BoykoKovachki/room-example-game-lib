package com.example.architectureexample.database;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameRepository {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private GameDao gameDao;
    private LiveData<List<Game>> allGames;

    public GameRepository(Application application) {
        GameDatabase database = GameDatabase.getInstance(application);
        gameDao = database.gameDao();
        allGames = gameDao.getAllGames();
    }

    public void insert(Game game) {
        new InsertGameAsyncTask(gameDao).execute(game);
    }

    public void update(Game game) {
        new UpdateGameAsyncTask(gameDao).execute(game);
    }

    public void delete(Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.delete(game);
            }
        });
    }

    public void deleteAllGames() {
        new DeleteAllGamesAsyncTask(gameDao).execute();
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    public void getLastAddedGame(GetLastGameListener listener) {
        executor.execute(() -> {
            Game game = gameDao.getLastAddedGame();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.getLastGameOnTaskCompleted(game);
                }
            });
        });
    }

    public void getRandomGame(GetRandomGameListener listener) {
        new GetRandomGameAsyncTask(gameDao, listener).execute();
    }

    public void getGameByName(GetGameByNameListener listener, String name) {
        new GetGameByNameAsyncTask(gameDao, listener, name).execute();
    }

    public void getGamesByType(GetGamesByTypeListener listener, int type) {
        new GetGamesByTypeAsyncTask(gameDao, listener, type).execute();
    }

    public void getFavoriteGames(GetFavoriteGamesListener listener) {
        new GetFavoriteGamesAsyncTask(gameDao, listener).execute();
    }

    private static class InsertGameAsyncTask extends AsyncTask<Game, Void, Void> {
        private GameDao gameDao;

        private InsertGameAsyncTask(GameDao gameDao) {
            this.gameDao = gameDao;
        }

        @Override
        protected Void doInBackground(Game... games) {
            gameDao.insert(games[0]);
            return null;
        }
    }

    private static class UpdateGameAsyncTask extends AsyncTask<Game, Void, Void> {
        private GameDao gameDao;

        private UpdateGameAsyncTask(GameDao gameDao) {
            this.gameDao = gameDao;
        }

        @Override
        protected Void doInBackground(Game... games) {
            gameDao.update(games[0]);
            return null;
        }
    }

    private static class DeleteGameAsyncTask extends AsyncTask<Game, Void, Void> {
        private GameDao gameDao;

        private DeleteGameAsyncTask(GameDao gameDao) {
            this.gameDao = gameDao;
        }

        @Override
        protected Void doInBackground(Game... games) {
            gameDao.delete(games[0]);
            return null;
        }
    }

    private static class DeleteAllGamesAsyncTask extends AsyncTask<Void, Void, Void> {
        private GameDao gameDao;

        private DeleteAllGamesAsyncTask(GameDao gameDao) {
            this.gameDao = gameDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gameDao.deleteAllGames();
            return null;
        }
    }

    // --------------------------------------------------------------------------
    public interface GetLastGameListener {
        void getLastGameOnTaskCompleted(Game game);
    }

    private static class GetLastAddedGameAsyncTask extends AsyncTask<Void, Void, Game> {
        private GameDao gameDao;
        private GetLastGameListener listener;

        private GetLastAddedGameAsyncTask(GameDao gameDao, GetLastGameListener listener) {
            this.gameDao = gameDao;
            this.listener = listener;
        }

        @Override
        protected Game doInBackground(Void... voids) {
            return gameDao.getLastAddedGame();
        }

        @Override
        protected void onPostExecute(Game game) {
            listener.getLastGameOnTaskCompleted(game);
        }
    }

    // --------------------------------------------------------------------------
    public interface GetRandomGameListener {
        void getRandomGameOnTaskCompleted(Game game);
    }

    private static class GetRandomGameAsyncTask extends AsyncTask<Void, Void, Game> {
        private GameDao gameDao;
        private GetRandomGameListener listener;

        private GetRandomGameAsyncTask(GameDao gameDao, GetRandomGameListener listener) {
            this.gameDao = gameDao;
            this.listener = listener;
        }

        @Override
        protected Game doInBackground(Void... voids) {
            return gameDao.getRandomGame();
        }

        @Override
        protected void onPostExecute(Game game) {
            listener.getRandomGameOnTaskCompleted(game);
        }
    }

    // --------------------------------------------------------------------------
    public interface GetGameByNameListener {
        void getGameByNameOnTaskCompleted(Game game);
    }

    private static class GetGameByNameAsyncTask extends AsyncTask<Void, Void, Game> {
        private GameDao gameDao;
        private GetGameByNameListener listener;
        private String gameTitle;

        private GetGameByNameAsyncTask(GameDao gameDao, GetGameByNameListener listener, String name) {
            this.gameDao = gameDao;
            this.listener = listener;
            this.gameTitle = name;
        }

        @Override
        protected Game doInBackground(Void... voids) {
            return gameDao.getGameByTitle(gameTitle);
        }

        @Override
        protected void onPostExecute(Game game) {
            listener.getGameByNameOnTaskCompleted(game);
        }
    }

    // --------------------------------------------------------------------------
    public interface GetGamesByTypeListener {
        void getGamesByTypeOnTaskCompleted(List<Game> game);
    }

    private static class GetGamesByTypeAsyncTask extends AsyncTask<Void, Void, List<Game>> {
        private GameDao gameDao;
        private GetGamesByTypeListener listener;
        private int type;

        private GetGamesByTypeAsyncTask(GameDao gameDao, GetGamesByTypeListener listener, int type) {
            this.gameDao = gameDao;
            this.listener = listener;
            this.type = type;
        }

        @Override
        protected List<Game> doInBackground(Void... voids) {
            return gameDao.getGamesByType(type);
        }

        @Override
        protected void onPostExecute(List<Game> games) {
            listener.getGamesByTypeOnTaskCompleted(games);
        }
    }

    // --------------------------------------------------------------------------
    public interface GetFavoriteGamesListener {
        void getFavoriteGamesOnTaskCompleted(List<Game> game);
    }

    private static class GetFavoriteGamesAsyncTask extends AsyncTask<Void, Void, List<Game>> {
        private GameDao gameDao;
        private GetFavoriteGamesListener listener;

        private GetFavoriteGamesAsyncTask(GameDao gameDao, GetFavoriteGamesListener listener) {
            this.gameDao = gameDao;
            this.listener = listener;
        }

        @Override
        protected List<Game> doInBackground(Void... voids) {
            return gameDao.getFavoriteGames();
        }

        @Override
        protected void onPostExecute(List<Game> games) {
            listener.getFavoriteGamesOnTaskCompleted(games);
        }
    }
    // ================================================================================================================== //

}
