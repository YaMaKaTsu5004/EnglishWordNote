package com.example.englishwordnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookEditActivity extends AppCompatActivity {

    public static final String EXTRA_BOOKNAME = "com.example.englishwordnote.BOOKNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBookEditActivity();
    }

    private void setBookEditActivity(){
        setContentView(R.layout.book_edit);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        Button bookAddButton = findViewById(R.id.add_book);
        Button returnButton = findViewById(R.id.return_main);
        Button allDeleteButton = findViewById(R.id.delete_all);

        ListView listView = findViewById(R.id.book_list);

        new AsynkTasks.BookViewTask(db, this, listView).execute();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplication(), WordsEditActivity.class);
            intent.putExtra(EXTRA_BOOKNAME,(String) listView.getItemAtPosition(position));
            startActivity(intent);
        });


        bookAddButton.setOnClickListener(v -> setBookEditActivitySub());

        returnButton.setOnClickListener(v -> finish());

        allDeleteButton.setOnClickListener(v -> {
            new AsynkTasks.BookDeleteTask(db, this).execute();
            new AsynkTasks.BookViewTask(db, this, listView).execute();
        });
    }

    private void setBookEditActivitySub(){
        setContentView(R.layout.book_edit_add);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        EditText bookName = findViewById(R.id.bookName);

        Button returnButton = findViewById(R.id.return_main);
        Button AddConfirm = findViewById((R.id.add_book_comfirm));

        AddConfirm.setOnClickListener(v -> {
            new AsynkTasks.BookDataStoreAsyncTask(db, this, bookName.getText().toString()).execute();
            setBookEditActivity();
        });

        returnButton.setOnClickListener(v -> setBookEditActivity());

    }
}
