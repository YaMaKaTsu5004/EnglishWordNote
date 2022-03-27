package com.example.englishwordnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button wEdit = findViewById(R.id.WordsEdit);
        Button wIO = findViewById(R.id.WordsIO);
        Button makeTest = findViewById(R.id.makeTest);

        wEdit.setOnClickListener(v -> {
            Intent bEditInt = new Intent(getApplication(), BookEditActivity.class);
            startActivity(bEditInt);
        });

        wIO.setOnClickListener(v -> {
            Intent wIOInt = new Intent(getApplication(), WordsIOActivity.class);
            startActivity(wIOInt);
        });

        makeTest.setOnClickListener(v -> {

        });


    }

}