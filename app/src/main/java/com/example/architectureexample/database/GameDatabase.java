package com.example.architectureexample.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.architectureexample.activities.R;

@Database(entities = {Game.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase {

    private static GameDatabase instance;

    private static Context activity;

    public abstract GameDao gameDao();

    public static synchronized GameDatabase getInstance(Context context) {
        activity = context.getApplicationContext();

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), GameDatabase.class, "game_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataBaseAsyncTask(instance).execute();
        }
    };

    private static class PopulateDataBaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private GameDao gameDao;

        private PopulateDataBaseAsyncTask(GameDatabase db) {
            gameDao = db.gameDao();
        }

        @Override
        protected Void doInBackground(Void... voids) { // Note: you can add games manually
//            gameDao.insert(new Game("Counter-Strike", "Shooter game...", 2013, "", 1, false));
//            gameDao.insert(new Game("Apex", "Shooter game...", 2017, "", 1, true));
//            gameDao.insert(new Game("League of Legends", "MOBA Game...", 2011, "", 2, false));
//            gameDao.insert(new Game("Hearthstone", "Card game from Blizzard...", 2016, "", 3, true));

            populateDBFromJson(activity);
            return null;
        }
    }

    private static void populateDBFromJson(Context context) {
        GameDao gameDao = getInstance(context).gameDao();

        JSONArray games = loadDBFromJSON(context);

        try {
            for (int i = 0; i < games.length(); i++) {
                JSONObject game = games.getJSONObject(i);
                String title = game.getString("title");
                String description = game.getString("description");
                int yearReleased = game.getInt("yearReleased");
                String logo = game.getString("logo");
                int type = game.getInt("type");
                boolean favorite = game.getInt("favorite") == 1;

                gameDao.insert(new Game(title, description, yearReleased, logo, type, favorite));
            }
        } catch (JSONException e) {
            Log.e("Error while loading json DB file", e.getMessage());
        }
    }

    private static JSONArray loadDBFromJSON(Context context) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(R.raw.games_db);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject object = new JSONObject(builder.toString());
            return object.getJSONArray("games");

        } catch (JSONException | IOException e) {
            Log.e("Error while getting json DB file", e.getMessage());
        }
        return null;
    }

}
