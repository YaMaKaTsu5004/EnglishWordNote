package com.example.englishwordnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.util.List;

public class WordsOutputActivity extends AppCompatActivity {

    private static final int CREATE_FILE = 1002;
    private String bookName;
    private AsynkTasks.WriteTask wt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setExportBook();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            if (resultData.getData() != null) {

                Uri uri = resultData.getData();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

                wt = new AsynkTasks.WriteTask(db,this,bookName,context);
                wt.execute();

                while (wt.getTaskComp() == false){
                    try {
                        Thread.sleep(1000); // 1秒(1千ミリ秒)間だけ処理を止める
                    } catch (InterruptedException e) {
                    }
                }

                List<Words> wordsList = wt.getWordsList();

                String str = "";

                for(Words w:wordsList){
                    str += w.getJpnWord() + "," + w.getEngWord() + "\n";
                }

                try (OutputStream outputStream =
                             getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        outputStream.write(str.getBytes());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(context,this.bookName + ".csv が作成されました",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setExportBook(){
        setContentView(R.layout.book_export);

        ListView listView = findViewById(R.id.book_list);
        AppDatabase db = AppDatabaseSingleton.getInstance(getApplicationContext());

        Button returnButton = findViewById(R.id.back);

        new AsynkTasks.BookViewTask(db,this, listView).execute();

        returnButton.setOnClickListener(v -> finish());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            BookDao dao = db.bookDao();
            this.bookName = dao.getBookWithId((int) id).book.getName();
            export(bookName);
        });

    }

    private void export(String bookName){

        String fileName = bookName + ".csv";

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, CREATE_FILE);


    }
}
