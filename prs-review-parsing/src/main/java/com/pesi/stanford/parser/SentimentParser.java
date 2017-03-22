package com.pesi.stanford.parser; /**
 * Created by U6026806 on 1/25/17.
 * This class deals with sentiment analysis
 */

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentimentParser {
    String[] sentimentText = {"Very Negative", "Negative", "Neutral", "Positive", "Very Positive"};
    final String posOpinionLexFile = "resources/opinion-lexicon/positive-words.txt";
    final String negOpinionLexFile = "resources/opinion-lexicon/negative-words.txt";
    Pattern posPat;
    Pattern negPat;

    Boolean containPolarity(CoreMap sentence) {
        Tree sentTree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        List<Tree> firstLevelChildren = sentTree.getChildrenAsList();
        for (Tree child : firstLevelChildren) {
            //System.out.println("Node:" + child.yield());
            //System.out.println("Predicted Class: " + RNNCoreAnnotations.getPredictedClass(child));
            int pol = RNNCoreAnnotations.getPredictedClass(child);
            if (pol != 2) { // as long as we find one non-neutrual sub-sentence. the whole sentence definitely contains polarity
                //System.out.println(child.yield() + ": " + sentimentText[pol]);
                return true;
            }
        }
        return false;
    }

    String getSentenceSentiment(CoreMap sentence) {
        return sentence.get(SentimentCoreAnnotations.SentimentClass.class);
    }

    public void loadOpinionWords() throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(posOpinionLexFile));

        String word;
        while ((word = br.readLine()) != null) {
            word = word.replace("*", "\\*").trim();
            word = word.replace("+", "\\+");
            words.add(word);
        }
        br.close();
        System.out.println(words.toString());
        posPat = Pattern.compile(String.join("|", words));

        words.clear();
        br = new BufferedReader(new FileReader(negOpinionLexFile));

        while ((word = br.readLine()) != null) {
            word = word.replace("*", "\\*").trim();
            words.add(word);
        }
        br.close();
        System.out.println(words.toString());
        negPat = Pattern.compile(String.join("|", words));

//        Matcher matcher = negPat.matcher("f**k you!");
//        while (matcher.find()) {
//            System.out.println("Found: " + matcher.group(0));
//        }

    }

    public String getSentencePolarity(String sent) {
        Matcher posMatcher = posPat.matcher(sent);
        while (posMatcher.find()) {
            System.out.println("POS: " + posMatcher.group(0));
        }

        Matcher negMatcher = negPat.matcher(sent);
        while (negMatcher.find()) {
            System.out.println("NEG: " + negMatcher.group(0));
        }

        return null;

    }

    public static void main(String[] args) throws IOException {
        SentimentParser sp = new SentimentParser();
        sp.loadOpinionWords();
        String sent = "fuck you! You're a bullshit. I regret that I loved you before!";
        sp.getSentencePolarity(sent);
    }



}
