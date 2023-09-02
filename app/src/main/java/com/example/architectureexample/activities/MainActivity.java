package com.example.architectureexample.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.architectureexample.adapters.GameAdapter;
import com.example.architectureexample.database.Game;
import com.example.architectureexample.database.GameRepository;
import com.example.architectureexample.database.GameViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GameRepository.GetLastGameListener, GameRepository.GetRandomGameListener, GameRepository.GetGameByNameListener, GameRepository.GetGamesByTypeListener, GameRepository.GetFavoriteGamesListener {

    private GameViewModel gameViewModel;
    private RecyclerView recyclerView;
    private GameAdapter adapter;

    private final ActivityResultLauncher<Intent> addGameActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                String title = result.getData().getStringExtra(AddGameActivity.EXTRA_TITLE);
                String description = result.getData().getStringExtra(AddGameActivity.EXTRA_DESCRIPTION);
                String yearReleased = result.getData().getStringExtra(AddGameActivity.EXTRA_YEAR_RELEASED);
                String logoID = result.getData().getStringExtra(AddGameActivity.EXTRA_LOGO);
                String type = result.getData().getStringExtra(AddGameActivity.EXTRA_TYPE);

                Game game = new Game(title, description, Integer.parseInt(yearReleased), logoID, Integer.parseInt(type), false);
                gameViewModel.insert(game);
            }
        }
    });

    private final ActivityResultLauncher<Intent> editGameActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {

                int id = result.getData().getIntExtra(AddGameActivity.EXTRA_ID, -1);
                if (id == -1) {
                    Toast.makeText(MainActivity.this, R.string.game_can_t_be_updated, Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = result.getData().getStringExtra(AddGameActivity.EXTRA_TITLE);
                String description = result.getData().getStringExtra(AddGameActivity.EXTRA_DESCRIPTION);
                String yearReleased = result.getData().getStringExtra(AddGameActivity.EXTRA_YEAR_RELEASED);
                String logoID = result.getData().getStringExtra(AddGameActivity.EXTRA_LOGO);
                String type = result.getData().getStringExtra(AddGameActivity.EXTRA_TYPE);

                Game game = new Game(title, description, Integer.parseInt(yearReleased), logoID, Integer.parseInt(type), true);
                game.setId(id);
                gameViewModel.update(game);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new GameAdapter(this.getApplicationContext());
        recyclerView.setAdapter(adapter);

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        gameViewModel.getAllGamesForPaging().observe(this, games -> adapter.setGames(games));

        FloatingActionButton FAB = findViewById(R.id.add_button);
        FAB.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
            addGameActivityLauncher.launch(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                gameViewModel.delete(adapter.getGameAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickedListener(game -> {
            Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
            intent.putExtra(AddGameActivity.EXTRA_ID, game.getId());
            intent.putExtra(AddGameActivity.EXTRA_TITLE, game.getTitle());
            intent.putExtra(AddGameActivity.EXTRA_DESCRIPTION, game.getDescription());
            intent.putExtra(AddGameActivity.EXTRA_YEAR_RELEASED, game.getYearReleased());
            intent.putExtra(AddGameActivity.EXTRA_LOGO, game.getLogo());
            intent.putExtra(AddGameActivity.EXTRA_TYPE, game.getType());
            editGameActivityLauncher.launch(intent);
        });

        adapter.setOnCheckedChangeListener((game, favorite) -> {
            Game updatedGame = new Game(game.getTitle(), game.getDescription(), game.getYearReleased(), game.getLogo(), game.getType(), favorite);
            updatedGame.setId(game.getId());
            gameViewModel.update(updatedGame);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                gameViewModel.deleteAllGames();
                return true;
            case R.id.last_game:
                openLastGameAdded();
                return true;
            case R.id.random_game:
                openRandomGameAdded();
                return true;
            case R.id.games_from_type:
                getGamesByType();
                return true;
            case R.id.favorite_games:
                getFavoritesGames();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openLastGameAdded() {
        if (gameViewModel.getAllGames() != null) {
            gameViewModel.getLastAddedGame(this);
        }
    }

    @Override
    public void getLastGameOnTaskCompleted(Game lastAddedGame) {
        if (gameViewModel.getAllGames() != null) {
            Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
            intent.putExtra(GameDetailActivity.EXTRA_TITLE, lastAddedGame.getTitle());
            intent.putExtra(GameDetailActivity.EXTRA_DESCRIPTION, lastAddedGame.getDescription());
            intent.putExtra(GameDetailActivity.EXTRA_YEAR_RELEASED, lastAddedGame.getYearReleased());
            intent.putExtra(GameDetailActivity.EXTRA_LOGO, lastAddedGame.getLogo());
            intent.putExtra(GameDetailActivity.EXTRA_TYPE, lastAddedGame.getType());
            intent.putExtra(GameDetailActivity.EXTRA_FAVORITE, lastAddedGame.isFavorite());
            startActivity(intent);
        }
    }

    private void openRandomGameAdded() {
        if (gameViewModel.getAllGames() != null) {
            gameViewModel.getRandomGame(this);
        }
    }

    private void getGamesByType() { // NOTE: 4 can be game type Role-playing games for example
        if (gameViewModel.getAllGames() != null) {
            gameViewModel.getGamesByTypes(this, 4);
        }
    }

    private void getFavoritesGames() {
        if (gameViewModel.getAllGames() != null) {
            gameViewModel.getFavoriteGames(this);
        }
    }

    @Override
    public void getRandomGameOnTaskCompleted(Game randomGame) {
        Intent intent = new Intent(MainActivity.this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_TITLE, randomGame.getTitle());
        intent.putExtra(GameDetailActivity.EXTRA_DESCRIPTION, randomGame.getDescription());
        intent.putExtra(GameDetailActivity.EXTRA_YEAR_RELEASED, randomGame.getYearReleased());
        intent.putExtra(GameDetailActivity.EXTRA_LOGO, randomGame.getLogo());
        intent.putExtra(GameDetailActivity.EXTRA_TYPE, randomGame.getType());
        intent.putExtra(GameDetailActivity.EXTRA_FAVORITE, randomGame.isFavorite());
        startActivity(intent);
    }

    @Override
    public void getGameByNameOnTaskCompleted(Game game) { // if there is no match it returns null
        if (game != null) {
            String gameInfo = String.format("\nTitle: %s,\nDescription: %s, \nYear Released: %d, \nType: %d, \nFavorite: %b",
                    game.getTitle(), game.getDescription(), game.getYearReleased(), game.getType(), game.isFavorite());
            Log.e("GameByName:", gameInfo);
        }
    }

    @Override
    public void getGamesByTypeOnTaskCompleted(List<Game> games) { // if there is no match it returns empty array
        if (games != null) {
            List<Game> sortedGamesByType = games;
            adapter.setGames(sortedGamesByType);
        }
    }

    @Override
    public void getFavoriteGamesOnTaskCompleted(List<Game> games) { // if there is no match it returns empty array
        if (games != null) {
            List<Game> favoriteGames = games;
            adapter.setGames(favoriteGames);
        }
    }

}
