package com.example.projtf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);


        // Reference to the Defence CardView
        CardView defenceCardView = findViewById(R.id.defenceCardView);
        CardView extaffairsCardview = findViewById(R.id.extaffairsCardView);

        // Set OnClickListener to navigate to DefenceActivity
        defenceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start Defenceofficials
                Intent intent = new Intent(HomePageActivity.this, officials.class);
                startActivity(intent);
            }
        });

    }


}