package com.example.projtf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class officials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officials);

        // Set up buttons and listeners for each official
        setupOfficialButtons(R.id.minister1ProfileButton, R.id.minister1ReviewButton, "https://en.wikipedia.org/wiki/Rajnath_Singh");
        setupOfficialButtons(R.id.minister2ProfileButton, R.id.minister2ReviewButton, "https://en.wikipedia.org/wiki/Chief_of_Defence_Staff_(India)");
        setupOfficialButtons(R.id.minister3ProfileButton, R.id.minister3ReviewButton, "https://en.wikipedia.org/wiki/G._C._Murmu");
        setupOfficialButtons(R.id.minister4ProfileButton, R.id.minister4ReviewButton, "https://en.wikipedia.org/wiki/Manoj_Pande");
        setupOfficialButtons(R.id.minister5ProfileButton, R.id.minister5ReviewButton, "https://en.wikipedia.org/wiki/R._Hari_Kumar");
        setupOfficialButtons(R.id.minister6ProfileButton, R.id.minister6ReviewButton, "https://en.wikipedia.org/wiki/Vivek_Ram_Chaudhari");
        // Add more officials here as needed
    }

    private void setupOfficialButtons(int profileButtonId, int reviewButtonId, String profileUrl) {
        // Set up profile button to open a web link
        Button profileButton = findViewById(profileButtonId);
        profileButton.setOnClickListener(v -> openProfile(profileUrl));

        // Set up review button to show rating dialog
        Button reviewButton = findViewById(reviewButtonId);
        reviewButton.setOnClickListener(v -> showRatingDialog(profileUrl));
    }

    private void openProfile(String profileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
        startActivity(browserIntent);
    }

    private void showRatingDialog(String profileUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rating, null);
        builder.setView(dialogView);

        // Existing fields
        RatingBar efficiencyRatingBar = dialogView.findViewById(R.id.efficiencyRatingBar);
        RatingBar transparencyRatingBar = dialogView.findViewById(R.id.transparencyRatingBar);
        RatingBar responsivenessRatingBar = dialogView.findViewById(R.id.responsivenessRatingBar);

        // New fields
        RatingBar integrityRatingBar = dialogView.findViewById(R.id.integrityRatingBar);
        RatingBar leadershipRatingBar = dialogView.findViewById(R.id.leadershipRatingBar);
        RatingBar decisionMakingRatingBar = dialogView.findViewById(R.id.decisionMakingRatingBar);
        EditText reviewText = dialogView.findViewById(R.id.reviewText);
        Button submitButton = dialogView.findViewById(R.id.submitButton);

        AlertDialog dialog = builder.create();

        submitButton.setOnClickListener(v -> {
            // Get ratings
            float efficiencyRating = efficiencyRatingBar.getRating();
            float transparencyRating = transparencyRatingBar.getRating();
            float responsivenessRating = responsivenessRatingBar.getRating();

            // Get new ratings
            float integrityRating = integrityRatingBar.getRating();
            float leadershipRating = leadershipRatingBar.getRating();
            float decisionMakingRating = decisionMakingRatingBar.getRating();

            String review = reviewText.getText().toString();

            if (review.isEmpty()) {
                Toast.makeText(this, "Please enter a review.", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                openReviewDisplayActivity(profileUrl, efficiencyRating, transparencyRating, responsivenessRating,
                        integrityRating, leadershipRating, decisionMakingRating, review);
            }
        });

        dialog.show();
    }

    private void openReviewDisplayActivity(String profileUrl, float efficiencyRating, float transparencyRating,
                                           float responsivenessRating, float integrityRating, float leadershipRating,
                                           float decisionMakingRating, String review) {
        Intent intent = new Intent(this, ReviewDisplayActivity.class);
        intent.putExtra("profileUrl", profileUrl);
        intent.putExtra("efficiencyRating", efficiencyRating);
        intent.putExtra("transparencyRating", transparencyRating);
        intent.putExtra("responsivenessRating", responsivenessRating);
        intent.putExtra("integrityRating", integrityRating);
        intent.putExtra("leadershipRating", leadershipRating);
        intent.putExtra("decisionMakingRating", decisionMakingRating);
        intent.putExtra("review", review);
        startActivity(intent);
    }

}