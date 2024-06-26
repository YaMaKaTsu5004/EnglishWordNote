package com.example.englishwordnote;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Words.class, Book.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
}
