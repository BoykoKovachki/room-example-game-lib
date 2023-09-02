package com.example.architectureexample.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddGameActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_YEAR_RELEASED = "com.example.architectureexample.YEAR_RELEASED";
    public static final String EXTRA_TYPE = "com.example.architectureexample.EXTRA_TYPE";
    public static final String EXTRA_LOGO = "com.example.architectureexample.EXTRA_LOGO";

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText yearReleasedEditText;
    private NumberPicker typePicker;
    private EditText logoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);

        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        yearReleasedEditText = findViewById(R.id.year_released_edit_text);
        typePicker = findViewById(R.id.typePicker);
        logoEditText = findViewById(R.id.logo_edit_text);

        typePicker.setMinValue(1);
        typePicker.setMaxValue(10);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(getString(R.string.edit_game));
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            descriptionEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            yearReleasedEditText.setText(String.valueOf(intent.getIntExtra(EXTRA_YEAR_RELEASED, 1)));
            typePicker.setValue(intent.getIntExtra(EXTRA_TYPE, 1));
            logoEditText.setText(intent.getStringExtra(EXTRA_LOGO));
        } else {
            setTitle(getString(R.string.add_game));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_game:
                saveGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveGame() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String yearReleased = yearReleasedEditText.getText().toString().trim();
        String type = String.valueOf(typePicker.getValue());
        String logoID = logoEditText.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || yearReleased.isEmpty() || type.isEmpty() || logoID.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.please_enter_game_data), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_YEAR_RELEASED, yearReleased);
        data.putExtra(EXTRA_TYPE, type);
        data.putExtra(EXTRA_LOGO, logoID);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

}