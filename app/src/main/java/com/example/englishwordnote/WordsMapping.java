package com.example.englishwordnote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsMapping {

    public static List<Map<String, String>> getdata(List<Words> wordsList) {
        List<Map<String, String>> wordMap = new ArrayList<>();

        for (Words li : wordsList){
            Map<String, String> data = new HashMap<>();
            data.put("jpnWord", li.getJpnWord());
            data.put("engWord", li.getEngWord());

            wordMap.add(data);
        }
        return wordMap;
    }
}
