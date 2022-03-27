package com.example.englishwordnote;

import java.io.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WordsIOActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData.getData() != null) {



                Uri uri = resultData.getData();

                setBookEditActivitySub(uri);


            }
        }
    }

    private void readTextFromUri(Uri uri, String bookName) throws IOException {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String words[] = line.split(",");
                AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());
                new AsynkTasks.WordDataStoreAsyncTask(db,this,words[0],words[1],bookName).execute();
            }
        }
    }

    private void setMenu(){
        setContentView(R.layout.words_io);

        Button returnButton = findViewById(R.id.return_main);
        Button importButton = findViewById(R.id.data_import);
        Button exportButton = findViewById(R.id.data_export);

        importButton.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        exportButton.setOnClickListener(v -> {
            Intent exportActivity = new Intent(getApplication(),WordsOutputActivity.class);
            startActivity(exportActivity);
        });

        returnButton.setOnClickListener(v -> finish());
    }

    private void setBookEditActivitySub(Uri uri) {
        setContentView(R.layout.book_edit_add);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        EditText bookName = findViewById(R.id.bookName);

        Button returnButton = findViewById(R.id.return_main);
        Button AddConfirm = findViewById((R.id.add_book_comfirm));

        AddConfirm.setOnClickListener(v -> {
            new AsynkTasks.BookDataStoreAsyncTask(db, this, bookName.getText().toString()).execute();
            try {

                readTextFromUri(uri, bookName.getText().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
            setMenu();
        });

        returnButton.setOnClickListener(v -> setMenu());

    }

}
