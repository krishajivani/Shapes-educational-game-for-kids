package com.example.rc211.educational_shape_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Instruct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruct);
    }

    public void back(View view){ //activity switches back to Main Menu
        Intent intent = new Intent(Instruct.this, MainActivity.class);
        startActivity(intent);
    }
}
