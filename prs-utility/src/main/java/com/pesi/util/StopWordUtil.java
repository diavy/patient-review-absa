package com.pesi.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shawn on 3/1/16.
 */
public class StopWordUtil {
    private static Set<String> stopWords = loadStopWords();

    public static boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    private static Set<String> loadStopWords() {
        Set<String> stopWords = new HashSet<String>();

        try {
            BufferedReader in;

            String path = "resources/stopwords/";
            String file = null;
            String line = null;

            file = "FoxStoplist.txt";
            in = new BufferedReader(new FileReader(path + file));
            in.readLine();
            while ((line = in.readLine()) != null) {
                stopWords.add(line.trim());
            }
            in.close();

            file = "SmartStoplist.txt";
            in = new BufferedReader(new FileReader(path + file));
            in.readLine();
            while ((line = in.readLine()) != null) {
                stopWords.add(line.trim());
            }
            in.close();

            file = "StanfordStoplist.txt";
            in = new BufferedReader(new FileReader(path + file));
            in.readLine();
            while ((line = in.readLine()) != null) {
                stopWords.add(line.trim());
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stopWords;
    }
}
