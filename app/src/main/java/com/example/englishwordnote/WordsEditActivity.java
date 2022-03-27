package com.example.englishwordnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class WordsEditActivity extends AppCompatActivity {

    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        bookName = intent.getStringExtra(BookEditActivity.EXTRA_BOOKNAME);

        setWordsEditActivity();
    }

    private void setWordsEditActivity(){
        setContentView(R.layout.words_edit);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        Button wordAddButton = findViewById(R.id.add_word);
        Button returnButton = findViewById(R.id.return_main);
        Button allClearButton = findViewById(R.id.delete_all);

        ListView listView = findViewById(R.id.words_list);

        new AsynkTasks.WordsViewTask(db, this, listView, bookName).execute();

        wordAddButton.setOnClickListener(v -> setWordsEditActivitySub());

        allClearButton.setOnClickListener(v -> {
            new AsynkTasks.WordsDeleteTask(db, this, bookName).execute();
            new AsynkTasks.WordsViewTask(db, this, listView, bookName).execute();
        });



        returnButton.setOnClickListener(v -> finish());
    }

    private void setWordsEditActivitySub(){
        setContentView(R.layout.words_edit_add);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        EditText jpnWord = findViewById(R.id.jpnWord);
        EditText engWord = findViewById(R.id.engWord);

        Button returnButton = findViewById(R.id.return_main);
        Button AddConfirm = findViewById((R.id.add_word_confirm));

        AddConfirm.setOnClickListener(v -> {
            new AsynkTasks.WordDataStoreAsyncTask(db, this, jpnWord.getText().toString(), engWord.getText().toString(), bookName).execute();
            setWordsEditActivity();
        });

        returnButton.setOnClickListener(v -> setWordsEditActivity());

    }
}
