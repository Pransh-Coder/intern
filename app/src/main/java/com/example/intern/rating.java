package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class rating extends AppCompatActivity {
    ImageView iv1,iv2,iv3,iv4,iv5;
    TextView rating;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().hide();
        iv1 = findViewById(R.id.iv_star1);
        iv2 = findViewById(R.id.iv_star2);
        iv3 = findViewById(R.id.iv_star3);
        iv4 = findViewById(R.id.iv_star4);
        iv5 = findViewById(R.id.iv_star5);
        rating = findViewById(R.id.tv_rating);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setImageResource(R.drawable.ic_star_orange_24dp);
                iv2.setImageResource(R.drawable.ic_star_black_24dp);
                iv3.setImageResource(R.drawable.ic_star_black_24dp);
                iv4.setImageResource(R.drawable.ic_star_black_24dp);
                iv5.setImageResource(R.drawable.ic_star_black_24dp);
                rating.setText("Bad");
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setImageResource(R.drawable.ic_star_orange_24dp);
                iv2.setImageResource(R.drawable.ic_star_orange_24dp);
                iv3.setImageResource(R.drawable.ic_star_black_24dp);
                iv4.setImageResource(R.drawable.ic_star_black_24dp);
                iv5.setImageResource(R.drawable.ic_star_black_24dp);
                rating.setText("Fair");
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setImageResource(R.drawable.ic_star_orange_24dp);
                iv2.setImageResource(R.drawable.ic_star_orange_24dp);
                iv3.setImageResource(R.drawable.ic_star_orange_24dp);
                iv4.setImageResource(R.drawable.ic_star_black_24dp);
                iv5.setImageResource(R.drawable.ic_star_black_24dp);
                rating.setText("Good");
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setImageResource(R.drawable.ic_star_orange_24dp);
                iv2.setImageResource(R.drawable.ic_star_orange_24dp);
                iv3.setImageResource(R.drawable.ic_star_orange_24dp);
                iv4.setImageResource(R.drawable.ic_star_orange_24dp);
                iv5.setImageResource(R.drawable.ic_star_black_24dp);
                rating.setText("Very Good");
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setImageResource(R.drawable.ic_star_orange_24dp);
                iv2.setImageResource(R.drawable.ic_star_orange_24dp);
                iv3.setImageResource(R.drawable.ic_star_orange_24dp);
                iv4.setImageResource(R.drawable.ic_star_orange_24dp);
                iv5.setImageResource(R.drawable.ic_star_orange_24dp);
                rating.setText("Excellent");
            }
        });
    }
}
