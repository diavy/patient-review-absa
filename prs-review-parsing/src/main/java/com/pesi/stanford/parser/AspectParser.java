package com.pesi.stanford.parser;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.pesi.common.Aspect;
import com.pesi.util.StopWordUtil;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by U6026806 on 1/25/17.
 */
public class AspectParser {

    private Set<String> invalidNP = new HashSet<String>(Arrays.asList("PRP", "DT"));

    public Map<String, Pattern> aspectPattern = new HashMap<String, Pattern>();

    protected void initPattern() {
        aspectPattern.put(Aspect.Category.APPOINTMENT_SCHEDULING.name(),
                Pattern.compile("appointment|receptionist|phone|call|front desk|scheduling|voicemail"));
        aspectPattern.put(Aspect.Category.BILLING_INSURANCE.name(),
                Pattern.compile("insurance|billing|credit card|receipts|the check"));
        aspectPattern.put(Aspect.Category.CLINIC_ENVIRONMENT.name(),
                Pattern.compile("office|room|location|lobby|bed|floor|restroom|facilities"));
        aspectPattern.put(Aspect.Category.PRICE.name(),
                Pattern.compile("price|money|groupon|\\$|promotion|deal|refund|pocket"));
        aspectPattern.put(Aspect.Category.STAFF_HELPFULLNESS.name(),
                Pattern.compile("staff|receptionist|front desk|service|manager"));
        aspectPattern.put(Aspect.Category.WAITING_TIME.name(),
                Pattern.compile("minutes|mins|hour"));
        aspectPattern.put(Aspect.Category.DOCTOR_PERFORMANCE.name(),
                Pattern.compile("massage|neck|pain|dr.? |chiropractor|doctor|treatment|body|chiropractic|" +
                        "masseuse|feet|therapist|adjustment|ankle|acupuncture|relief|shoulder|head|leg|spine" +
                        "muscle|arm|therapy|foot|session|injuries|stretches|hip|knee|x-ray"));
    }
    //private Aspect aspect = new Aspect();
    //aspectPattern.put(aspect.Category.)


    public Map<String, Pattern> getAspectPattern() {
        return aspectPattern;
    }

    public List<String> getNounPhrases(CoreMap sentence) {
        // extract noun-phrases from a sentence
        Tree parseTree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        List<String> result = new ArrayList<String>();
        List<List<LabeledWord>> npLabels = new ArrayList<List<LabeledWord>>();
        TregexPattern pattern = TregexPattern.compile("@NP");
        TregexMatcher matcher = pattern.matcher(parseTree);
        while (matcher.find()) {
            Tree match = matcher.getMatch();
            List<Tree> leaves = match.getLeaves();
            //System.out.println(leaves);
            // Some Guava magic.
            String nounPhrase = Joiner.on(' ').join(Lists.transform(leaves, Functions.toStringFunction()));
            //System.out.println(nounPhrase);
            result.add(nounPhrase);
            List<LabeledWord> labeledYield = match.labeledYield();
            //System.out.println("labeledYield: " + labeledYield);
            npLabels.add(labeledYield);
        }
        return result;
        //return npLabels;
    }

    public List<LabeledWord> getPOSTagPairs(CoreMap sentence) {
        Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
        //System.out.println("parse tree:\n" + tree.labeledYield());
        List<LabeledWord> labels = tree.labeledYield();
        //System.out.println(labels.get(0).word() + ":" + labels.get(0).tag());
        return labels;
    }

    public boolean isValidAspect(String nounPhrase, List<LabeledWord> POSTagPairs) {
        // given a noun phrase, judge whether the noun phrases is a valid candidate for aspect

        //first from POS tag pairs, generate a map
        Map<String, String> wordTag = new HashMap<String, String>();
        for (LabeledWord pair : POSTagPairs) {
            wordTag.put(pair.word(), pair.tag().toString());
        }

        String[] phraseTokens = nounPhrase.split(" ");
        int tokenNums = phraseTokens.length;
        if (tokenNums > 4)  // remove too long noun phrases
            return false;

        if (tokenNums == 1) {
            if (invalidNP.contains(wordTag.get(phraseTokens[0])))
                return false;
        }

        Boolean allStopwords = true;
        for (String token : phraseTokens) {
            if (!StopWordUtil.isStopWord(token))
                allStopwords = false;
        }
        if (allStopwords)
            return false;


        return true;
    }

    public static void main(String[] args) {
        AspectParser ap = new AspectParser();
        ap.initPattern();

        for (String asp : ap.aspectPattern.keySet()) {
            System.out.println(asp + "\t" + ap.aspectPattern.get(asp));
        }

        String text = "this chiropractor";
        String target;
        Matcher matcher = ap.aspectPattern.get("DOCTOR_PERFORMANCE").matcher(text);
        while (matcher.find()) {
            System.out.println("Found: " + matcher.group(0));
        }


    }


}
