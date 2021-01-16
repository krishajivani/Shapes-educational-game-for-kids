package com.example.rc211.educational_shape_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void learner(View view){ //goes to learner mode on click
        Intent intent = new Intent(MainActivity.this, LearnerModeActivity.class);
        startActivity(intent);
    }
    public void hard(View view){ //goes to hard mode on click
        Intent intent = new Intent(MainActivity.this, HardActivity.class);
        startActivity(intent);
    }
    public void instructions(View view){ //goes to instructions on click
        Intent intent = new Intent(MainActivity.this, Instruct.class);
        startActivity(intent);
    }
}
