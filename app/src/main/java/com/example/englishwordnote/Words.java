package com.example.englishwordnote;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity (tableName = "words")
public class Words {

    @ColumnInfo(name = "_ID")
    @PrimaryKey(autoGenerate = true)
    private int wordId;

    public int bookCreateId;

    @ColumnInfo(name = "jpnWord")
    private String jpnWord;

    @ColumnInfo(name = "engWord")
    private String engWord;

    public Words(int bookCreateId,String jpnWord, String engWord) {
        this.bookCreateId = bookCreateId;
        this.jpnWord = jpnWord;
        this.engWord = engWord;
    }

    public void setWordId(int id) {
        this.wordId = id;
    }

    public int getWordId() {
        return wordId;
    }

    public void setJpnWord(String jpnWord) {
        this.jpnWord = jpnWord;
    }

    public String getJpnWord() {
        return jpnWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public String getEngWord() {
        return engWord;
    }

}

