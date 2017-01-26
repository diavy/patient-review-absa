import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

//import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;

/**
 * Created by U6026806 on 1/12/17.
 */
public class CoreNLPDemo {
    public void textParser() {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // add some text here
        String text = "The food is delicious, but the service is terrible"; // conflict sentiment
        //String text = "I went to the airport yesterday"; // neutural sentiment

        String[] sentimentText = {"Very Negative", "Negative", "Neutral", "Positive", "Very Positive"};

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);


        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods

            // Print sentence's key
            System.out.println("Sentence's keys: " + sentence.keySet());
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);

                // this is the sentiment label of the token
                //String sentiment = token.get(SentimentA)

                System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeAnnotation.class);
            System.out.println("parse tree:\n" + tree);

            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println("dependency graph:\n" + dependencies);
            System.out.println("dependency graph:\n" + dependencies.toString(SemanticGraph.OutputFormat.LIST));
            tree.pennPrint();

            // this is the sentiiment of the current sentence
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println("sentiment:\n" + sentiment + ":\t" + sentence);
            Tree sentTree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            sentTree.pennPrint();
            //System.out.println(sentTree);
            //int score = RNNCoreAnnotations.getPredictedClass(sentTree);
            //System.out.println(sentimentText[score]);

            // GO through each node, and extract sentiment score
            /*Iterator<Tree> it = sentTree.iterator();
            while (it.hasNext()) {
                Tree t = it.next();
                System.out.println(t.yield());
                System.out.println("nodestring:" + t.nodeString());
                //System.out.println("children:" + t.getChildrenAsList().toString());
                if (((CoreLabel) t.label()).containsKey(RNNCoreAnnotations.PredictedClass.class)) {
                    System.out.println("Predicted Class: " + RNNCoreAnnotations.getPredictedClass(t));
                }
                //System.out.println(RNNCoreAnnotations.getNodeVector(t));
                //System.out.println(RNNCoreAnnotations.getPredictions(t));
            }*/

            // go through the children of the root node, and extract its sentiment class
            List<Tree> firstLevelChildren = sentTree.getChildrenAsList();
            for (Tree child : firstLevelChildren) {
                System.out.println("Node:" + child.yield());
                System.out.println("Predicted Class: " + RNNCoreAnnotations.getPredictedClass(child));
            }

            // Go through each node, and print out dependencies
            Tree depTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);


        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        //Map<Integer, CorefChain> graph =
        //        document.get(CorefChainAnnotation.class);

    }

    public static void main(String[] args) {
        CoreNLPDemo demo = new CoreNLPDemo();
        demo.textParser();
    }
}
