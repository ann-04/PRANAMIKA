package com.example.projtf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);

        // Existing fields
        RatingBar displayEfficiencyRatingBar = findViewById(R.id.displayEfficiencyRatingBar);
        RatingBar displayTransparencyRatingBar = findViewById(R.id.displayTransparencyRatingBar);
        RatingBar displayResponsivenessRatingBar = findViewById(R.id.displayResponsivenessRatingBar);

        // New fields
        RatingBar displayIntegrityRatingBar = findViewById(R.id.displayIntegrityRatingBar);
        RatingBar displayLeadershipRatingBar = findViewById(R.id.displayLeadershipRatingBar);
        RatingBar displayDecisionMakingRatingBar = findViewById(R.id.displayDecisionMakingRatingBar);
        TextView reviewTextView = findViewById(R.id.reviewTextView);
        Button openProfileButton = findViewById(R.id.openProfileButton);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("profileUrl");
        float efficiencyRating = intent.getFloatExtra("efficiencyRating", 0);
        float transparencyRating = intent.getFloatExtra("transparencyRating", 0);
        float responsivenessRating = intent.getFloatExtra("responsivenessRating", 0);

        // New ratings
        float integrityRating = intent.getFloatExtra("integrityRating", 0);
        float leadershipRating = intent.getFloatExtra("leadershipRating", 0);
        float decisionMakingRating = intent.getFloatExtra("decisionMakingRating", 0);
        String review = intent.getStringExtra("review");

        displayEfficiencyRatingBar.setRating(efficiencyRating);
        displayTransparencyRatingBar.setRating(transparencyRating);
        displayResponsivenessRatingBar.setRating(responsivenessRating);
        displayIntegrityRatingBar.setRating(integrityRating);
        displayLeadershipRatingBar.setRating(leadershipRating);
        displayDecisionMakingRatingBar.setRating(decisionMakingRating);
        reviewTextView.setText(review);

        openProfileButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
            startActivity(browserIntent);
        });
    }

}