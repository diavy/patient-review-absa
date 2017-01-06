import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U6026806 on 1/5/17.
 */
public class SimpleCoreNLPDemo {

    public static List<Tree> GetNounPhrases(Tree parseTree) {

//        List<Tree> phraseList=new ArrayList<Tree>();
//        for (Tree subtree: parse)
//        {

        if (parseTree.label().value().equals("NP")) {

            //phraseList.add(subtree);
            System.out.println();

        }


//        return phraseList;
        return null;

    }

    public static void main(String[] args) {
        // Create a document. No computation is done yet.
//        Document doc = new Document("add your text here! It can contain multiple sentences.");
//        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
//            // We're only asking for words -- no need to load any models yet
//            System.out.println("The second word of the sentence '" + sent + "' is " + sent.word(1));
//            // When we ask for the lemma, it will load and run the part of speech tagger
//            System.out.println("The third lemma of the sentence '" + sent + "' is " + sent.lemma(2));
//            // When we ask for the parse, it will load and run the parser
//            System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
//            // ...
//            System.out.println("The dependency of the second word '" + sent + "' is " + sent.incomingDependencyLabel(1));
//        }
        SimpleCoreNLPDemo demo = new SimpleCoreNLPDemo();
        Tree tree = demo.testParse();
        demo.getNounPhrases(tree);
    }

    public Tree testParse() {
        Sentence sent = new Sentence("It can contain multiple sentences.");
        Tree parseTree = sent.parse();
        System.out.println(parseTree);
        System.out.println(parseTree.constituents());

        return parseTree;


    }

    private List<String> getNounPhrases(Tree parse) {
        List<String> result = new ArrayList<String>();
        TregexPattern pattern = TregexPattern.compile("@NP");
        TregexMatcher matcher = pattern.matcher(parse);
        while (matcher.find()) {
            Tree match = matcher.getMatch();
            List<Tree> leaves = match.getLeaves();
            System.out.println(leaves);
            // Some Guava magic.
            String nounPhrase = Joiner.on(' ').join(Lists.transform(leaves, Functions.toStringFunction()));
            result.add(nounPhrase);
            List<LabeledWord> labeledYield = match.labeledYield();
            System.out.println("labeledYield: " + labeledYield);
        }
        return result;
    }
}
