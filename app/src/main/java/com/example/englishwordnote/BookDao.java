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

    @Transaction
    @Query("SELECT * FROM book WHERE _ID LIKE :id")
    BookWithWords getBookWithId(int id);

    @Query("SELECT * FROM words")
    List<Words> getAllWords();

    @Query("SELECT * FROM words WHERE _ID LIKE :id")
    Words getWordWithId(int id);

    @Query("SELECT * FROM words LIMIT :limit OFFSET :start - 1")
    List<Words> getSomeWords(int start, int limit);

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
