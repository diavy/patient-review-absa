package com.pesi.stanford.parser; /**
 * Created by U6026806 on 1/25/17.
 * This class deals with sentiment analysis
 */

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

public class SentimentParser {
    String[] sentimentText = {"Very Negative", "Negative", "Neutral", "Positive", "Very Positive"};

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

}
