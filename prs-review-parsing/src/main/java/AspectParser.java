import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U6026806 on 1/25/17.
 */
public class AspectParser {

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
        System.out.println(labels.get(0).word() + ":" + labels.get(0).tag());
        return labels;
    }

    public boolean isValidAspect(String nounPhrase) {
        return false;
    }


}
