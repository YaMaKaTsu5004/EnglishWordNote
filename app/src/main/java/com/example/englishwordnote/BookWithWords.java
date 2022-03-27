package com.example.englishwordnote;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BookWithWords {
    @Embedded public Book book;
    @Relation(
            parentColumn = "bookId", entityColumn = "bookCreateId", entity = Words.class
    ) public List<Words> words;
}
