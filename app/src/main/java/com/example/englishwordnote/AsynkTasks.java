package com.example.englishwordnote;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AsynkTasks {

    public static class BookViewTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private ListView lv;
        private Activity activity;
        private ArrayAdapter adapter;

        public BookViewTask(AppDatabase db, Activity activity, ListView lv) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.lv = lv;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao bookDao = db.bookDao();

            List<BookWithWords> bwwlist = bookDao.getAllBooks();

            ArrayList<String> books = new ArrayList<>();

            for (BookWithWords bww:bwwlist){
                books.add(bww.book.getName());
            }

            ArrayAdapter adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, books);

            this.adapter = adapter;

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

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

    public static class WordsViewTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private ListView lv;
        private Activity activity;
        private SimpleAdapter wordsListAdapter;
        private String bookName;

        public WordsViewTask(AppDatabase db, Activity activity, ListView lv, String bookName) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.lv = lv;
            this.bookName = bookName;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();

            List<Words> atList = wordsDao.getBookWithWords(bookName).words;


            List<Map<String, String>> wordsList = WordsMapping.getdata(atList);

            SimpleAdapter wordsListAdapter = new SimpleAdapter(
                    activity,
                    wordsList,
                    R.layout.words_list,
                    new String[]{"jpnWord", "engWord"},
                    new int[]{R.id.jpnWord, R.id.engWord}
            );

            this.wordsListAdapter = wordsListAdapter;


            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            lv.setAdapter(wordsListAdapter);

        }
    }

    public static class WordsDeleteTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Activity> weakActivity;
        private AppDatabase db;
        private Activity activity;
        private SimpleAdapter wordsListAdapter;
        private String bookName;

        public WordsDeleteTask(AppDatabase db, Activity activity, String bookName) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.activity = activity;
            this.bookName = bookName;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();

            List<Words> atList = wordsDao.getBookWithWords(bookName).words;

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

        public WordDataStoreAsyncTask(AppDatabase db, Activity activity, String jpnWord, String engWord, String bookName) {
            this.db = db;
            weakActivity = new WeakReference<>(activity);
            this.jpnWord = jpnWord;
            this.engWord = engWord;
            this.bookName = bookName;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            BookDao wordsDao = db.bookDao();
            int id = wordsDao.getBookWithWords(bookName).book.getBookId();
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
