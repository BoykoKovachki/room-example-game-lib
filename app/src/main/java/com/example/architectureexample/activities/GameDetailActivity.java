package com.example.architectureexample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

public class GameDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.example.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_YEAR_RELEASED = "com.example.architectureexample.YEAR_RELEASED";
    public static final String EXTRA_TYPE = "com.example.architectureexample.EXTRA_TYPE";
    public static final String EXTRA_LOGO = "com.example.architectureexample.EXTRA_LOGO";
    public static final String EXTRA_FAVORITE = "com.example.architectureexample.EXTRA_FAVORITE";

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView yearReleasedTextView;
    private TextView typeTextView;
    private ImageView logoImageView;
    private Switch favorite_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);
        setTitle(getString(R.string.game_details));

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        yearReleasedTextView = findViewById(R.id.yearReleasedTextView);
        typeTextView = findViewById(R.id.typeTextView);
        logoImageView = findViewById(R.id.logo_imageview);
        favorite_switch = findViewById(R.id.favorite_switch);

        Intent intent = getIntent();
        titleTextView.setText(intent.getStringExtra(EXTRA_TITLE));
        descriptionTextView.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        yearReleasedTextView.setText(String.valueOf(intent.getIntExtra(EXTRA_YEAR_RELEASED, 1)));
        typeTextView.setText(intent.getStringExtra(EXTRA_TYPE));
        String imageURL = intent.getStringExtra(EXTRA_LOGO);
        Glide.with(this)
                .load(imageURL)
                .fitCenter()
                .transform(new RoundedCorners(20))
                .into(logoImageView);
        favorite_switch.setChecked(intent.getBooleanExtra(EXTRA_FAVORITE, false));
    }

}