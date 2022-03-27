package com.example.englishwordnote;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface BookDao {
    @Transaction
    @Query("SELECT * FROM book")
    List<BookWithWords> getAllBooks();

    @Transaction
    @Query("SELECT * FROM book WHERE name LIKE :bookName")
    BookWithWords getBookWithWords(String bookName);

    @Query("SELECT * FROM words")
    List<Words> getAll();

    @Insert
    void insertBook(Book book);

    @Insert
    void insertAllWords(Words... words);

    @Insert
    void insertWord(Words words);

    @Delete
    void deleteAllWords(Words... words);

    @Delete
    void deleteWord(Words words);

    @Delete
    void deleteBook(Book book);
}
