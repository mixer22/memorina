package com.example.memor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TilesView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.view);
        Button btnRestart = findViewById(R.id.button1);
        View.OnClickListener oclBtnRestart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewGameClick();
            }
        };
        btnRestart.setOnClickListener(oclBtnRestart);
    }

    public void onNewGameClick()
    {
        super.recreate();
    }
}