package com.example.exercise01_part2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int mCounter = 0;
    private TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SABOTAGE: This will cause a NullPointerException if not handled.
        TextView counterDisplay = null;
        counterDisplay.setText("0");

        // Proper initialization (we'll move this after fixing the crash)
        tvCounter = findViewById(R.id.tvCounter);
        Button btnIncrement = findViewById(R.id.btnIncrement);

        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounter++;
                tvCounter.setText(String.valueOf(mCounter));
            }
        });
    }
}
