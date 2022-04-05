package com.example.englishwordnote;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kotlin.experimental.BitwiseOperationsKt;

public class MakeTestActivity extends AppCompatActivity {

    private int bookId;
    private String bookName;
    private long startWordNum;
    private long endWordNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeTestMenu();

    }

    private void makeTestMenu(){
        setContentView(R.layout.activity_make_test);

        Button bookSelectButton = findViewById(R.id.book_select);
        Button wordSelectButton1 = findViewById(R.id.start_select);
        Button wordSelectButton2 = findViewById(R.id.end_select);
        TextView bookText = findViewById(R.id.book_view);
        TextView startWordText = findViewById(R.id.start_word_view);
        TextView endWordText = findViewById(R.id.end_word_view);

        Spinner questionFormat = findViewById(R.id.spinner);
        String[] spinnerItems = {"左","右","ランダム"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        questionFormat.setAdapter(spinnerAdapter);

        if (bookName != null) bookText.setText(bookName);
        if (startWordNum != 0) startWordText.setText(String.valueOf(startWordNum));
        if (endWordNum != 0) endWordText.setText(String.valueOf(endWordNum));

        bookSelectButton.setOnClickListener(v -> bookSelect());

        wordSelectButton1.setOnClickListener(v -> {if(bookName != null)wordSelect(0);});
        wordSelectButton2.setOnClickListener(v -> {if(bookName != null)wordSelect(1);});



        Button returnButton = findViewById(R.id.return_main);

        returnButton.setOnClickListener(v -> finish());
    }

    private void bookSelect(){
        setContentView(R.layout.book_select);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        Button returnButton = findViewById(R.id.back);
        ListView lv = findViewById(R.id.book_list);

        new AsynkTasks.BookViewTask(db, this, lv).execute();

        lv.setOnItemClickListener((parent, view, position, id) -> {
            BookDao bd = db.bookDao();
            bookId = (int) id;
            bookName = bd.getBookWithId(bookId).book.getName();
            makeTestMenu();
        });

        returnButton.setOnClickListener(v -> makeTestMenu());
    }

    private void wordSelect(int SorE){
        setContentView(R.layout.word_select);

        Button returnButton = findViewById(R.id.back);

        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        ListView lv = findViewById(R.id.words_list);

        new AsynkTasks.WordsViewTask(db, this, lv, bookId).execute();

        lv.setOnItemClickListener((parent, view, position, id) -> {
            BookDao bd = db.bookDao();
            if(SorE == 0) {
                startWordNum = position + 1;
            }else if(SorE == 1){
                endWordNum = position + 1;
            }
            makeTestMenu();
        });

        returnButton.setOnClickListener(v -> makeTestMenu());


    }
}
