package com.example.englishwordnote;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AsynkTasks {

    public static class BookAccessTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private int id;
        private BookWithWords book;
        public boolean check = false;

        public BookAccessTask(AppDatabase db, int id) {
            this.db = db;
            this.id = id;

        }


        @Override
        protected Integer doInBackground(Void... params) {

            BookDao bookDao = db.bookDao();
            BookWithWords book = bookDao.getBookWithId(id);
            this.book = book;
            this.check = true;
            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
        }

        public BookWithWords getBook(){
            return book;
        }
    }

    public static class BookViewTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private ListView lv;
        private Activity activity;
        private SimpleCursorAdapter adapter;
        private Cursor c;

        private SimpleCursorAdapter sca;

        public BookViewTask(AppDatabase db, Activity activity, ListView lv) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.lv = lv;



        }


        @Override
        protected Integer doInBackground(Void... params) {

            BookDao bookDao = db.bookDao();
            List<BookWithWords> books = bookDao.getAllBooks();
        /* NOTE that as Cursor Adapters required a column named _ID (as per BaseColumns._ID)
            the first column has been renamed accordingly

         */
            MatrixCursor mxcsr = new MatrixCursor(new String[]{
                    BaseColumns._ID,
                    "name"}
            );
            for (BookWithWords b: bookDao.getAllBooks()) {
                mxcsr.addRow(new Object[]{b.book.getBookId(),b.book.getName()});
            }
            this.c = mxcsr;
            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            SimpleCursorAdapter sca = new SimpleCursorAdapter(activity,android.R.layout.simple_list_item_1,c,new String[]{"name"}, new int[]{android.R.id.text1});
            this.adapter = sca;

            lv.setAdapter(adapter);

        }
    }

    public static class BookDeleteTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private Activity activity;
        private SimpleAdapter wordsListAdapter;

        public BookDeleteTask(AppDatabase db, Activity activity) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();

            List<BookWithWords> atList = wordsDao.getAllBooks();

            for (BookWithWords at : atList){
                wordsDao.deleteBook(at.book);
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
        }
    }

    public static class BookDataStoreAsyncTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private String bookName;
        private Book book;
        private int id;

        public BookDataStoreAsyncTask(AppDatabase db, Activity activity, String bookName) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.bookName = bookName;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao bookDao = db.bookDao();
            Book book = new Book(bookName);
            bookDao.insertBook(book);
            this.book = book;

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
            this.id = book.getBookId();

        }

        public int getId(){
            return this.id;
        }
    }

    public static class WordsViewTask extends AsyncTask<Void, Void, Integer> {
        private int id;
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private ListView lv;
        private Activity activity;
        private SimpleCursorAdapter adapter;
        private Cursor c;

        public WordsViewTask(AppDatabase db, Activity activity, ListView lv, int id) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.lv = lv;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();

            List<Words> atList = wordsDao.getBookWithId(id).words;


            MatrixCursor mxcsr = new MatrixCursor(new String[]{
                    BaseColumns._ID,
                    "jpnWord",
                    "engWord"}
            );
            for (Words b: wordsDao.getBookWithId(id).words) {
                mxcsr.addRow(new Object[]{b.getWordId(),b.getJpnWord(),b.getEngWord()});
            }
            this.c = mxcsr;


            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            SimpleCursorAdapter sca = new SimpleCursorAdapter(activity,R.layout.words_list,c,new String[]{"jpnWord", "engWord"}, new int[]{R.id.jpnWord, R.id.engWord});
            this.adapter = sca;

            lv.setAdapter(adapter);

        }
    }

    public static class WordsDeleteTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private Activity activity;
        private SimpleAdapter wordsListAdapter;
        private String bookName;
        private int id;

        public WordsDeleteTask(AppDatabase db, Activity activity, int id) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();

            List<Words> atList = wordsDao.getBookWithId(id).words;

            for (Words at : atList){
                wordsDao.deleteWord(at);
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
        }
    }

    public static class WordDataStoreAsyncTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private String jpnWord;
        private String engWord;
        private String bookName;
        private int id;

        public WordDataStoreAsyncTask(AppDatabase db, Activity activity, String jpnWord, String engWord, int id) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.jpnWord = jpnWord;
            this.engWord = engWord;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();
            Words word = new Words(id, jpnWord, engWord);
            wordsDao.insertWord(word);

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
        }
    }

    public static class WriteTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private String bookName;
        private int id;
        private String fileName;
        private Activity activity;
        private ArrayAdapter adapter;
        private Context context;
        private List<Words> wordsList;
        private boolean taskComp = false;

        public WriteTask(AppDatabase db, Activity activity, String bookName, Context context) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.bookName = bookName;
            this.fileName = bookName + ".csv";
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao bookDao = db.bookDao();

            List<Words> bwwlist = bookDao.getBookWithWords(bookName).words;

            this.wordsList = bwwlist;

            this.taskComp = true;

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
        }

        public String getFileName(){
            return this.fileName;
        }

        public boolean getTaskComp(){ return this.taskComp;}

        public List<Words> getWordsList(){
            return this.wordsList;
        }
    }
}
