package com.example.englishwordnote;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity(tableName = "book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    private int bookId;

    @ColumnInfo(name = "name")
    private String name;

    Book(String name){
        this.name = name;
    }

    public void setName(String name){this.name = name;}

    public String getName(){return name;}

    public void setBookId(int id){this.bookId = id;}

    public int getBookId(){return bookId;}
}
