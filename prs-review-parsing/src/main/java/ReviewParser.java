/**
 * Created by U6026806 on 1/5/17.
 * This module is used to parse the NLP features of review content
 */

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class ReviewParser {

    private String reviewFile = "prs-review-parsing/data/review_labeling.xlsx";
    //private final Properties props = new Properties();
    //private final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    public SentimentParser sentimentParser = new SentimentParser(); // initialize a sentiment parser
    public AspectParser aspectParser = new AspectParser(); // initilize a aspect parser


    public List<String> extractReviewContent(String reviewFile) throws IOException {
        // extract review content from text file
        FileInputStream file = new FileInputStream(reviewFile);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        XSSFSheet sheet = workbook.getSheet("Sheet1");

        Iterator<Row> rowIterator = sheet.iterator();
        List<String> reviewList = new ArrayList<String>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell text = row.getCell(9);
            //System.out.println(text.toString());
            reviewList.add(text.toString());
        }

        reviewList.remove(0);// remove the header

        return reviewList;
    }

    public List<String> extractNounPhrases(String reviewText) {
        /* given a review, extract key noun phrases from it  */
        //System.out.println(reviewText);

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        Annotation document = new Annotation(reviewText);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(document);


        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        List<String> validNps = new ArrayList<String>();

        for (CoreMap sentence : sentences) {
            //System.out.println(sentence.toString());
            if (sentimentParser.containPolarity(sentence)) {
                List<LabeledWord> pairs = aspectParser.getPOSTagPairs(sentence);
                List<String> extractedNPs = aspectParser.getNounPhrases(sentence);
                for (String np : extractedNPs) {
                    np = np.trim().toLowerCase();
                    if (aspectParser.isValidAspect(np, pairs))
                        validNps.add(np);
                }
                System.out.println(sentence.toString());
                System.out.println(sentimentParser.getSentenceSentiment(sentence));
                System.out.println(extractedNPs);
            }
        }

        return validNps;
    }

    public void countNounPhrases(List<String> reviewTextList, String outputFile) throws IOException {
        /* count the frequency of noun phrases, given a set of review, and save to output file */
        BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
        Map<String, Integer> npFreq = new HashMap<String, Integer>();
        List<String> nounPhrases;


        int index = 0;
        System.out.println("begin to parse np from reviews, one by one....");
        for (String review : reviewTextList) {
            nounPhrases = extractNounPhrases(review);

            for (String np : nounPhrases) {
                np = np.toLowerCase();
                MapUtil.addCount(npFreq, np);
            }
            if (index % 100 == 0) {
                System.out.print(index);
            }
            index += 1;
        }

        Map<String, Integer> rankedNP = MapUtil.sortByValue(npFreq, false);

        out.write(String.format("%s\t%s\n", "Aspect", "Frequency"));
        for (String np : rankedNP.keySet()) {
            out.write(String.format("%s\t%d\n", np, npFreq.get(np)));
        }
        out.close();

    }

    public void parseSingleReview(String reviewText) {
        // Given a review text, parse its NLP features. may send to other follow up parsing tasks
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // add some text here
        //String text = "I went there yesterday. Dr. Gustin is very happy";

        //String[] sentimentText = {"Very Negative", "Negative", "Neutral", "Positive", "Very Positive"};

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(reviewText);

        // run all Annotators on this text
        pipeline.annotate(document);


        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        SentimentParser sp = new SentimentParser(); // initialize a sentiment parser
        AspectParser ap = new AspectParser(); // initilize a aspect parser

        for (CoreMap sentence : sentences) {
            //String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            //System.out.println(sentence);
            //System.out.println("sentiment:" + sentiment);
            //Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            //System.out.println("parse tree:\n" + tree.labeledYield());

            if (sp.containPolarity(sentence)) {
                List<LabeledWord> pairs = ap.getPOSTagPairs(sentence);
                //System.out.println("polary sentence: " + sentence.toString() + " :" + sp.getSentenceSentiment(sentence));
                List<String> extractedNPs = ap.getNounPhrases(sentence);
                //System.out.println("find NPs: " + ap.getNounPhrases(sentence));
                for (String np : extractedNPs) {

                    System.out.println(np + " : " + ap.isValidAspect(np, pairs));
                }

            } else {
                System.out.println("neutral sentence: " + sentence.toString());
            }

        }

    }

    public void parseMultipleReviews(List<String> reviewTextList, String output) {

    }


    public static void main(String[] args) throws IOException {
        ReviewParser rp = new ReviewParser();

        List<String> reviewItems = rp.extractReviewContent(rp.reviewFile);
        //rp.parseSingleReview(reviewItems.get(0));
        //System.out.println(rp.extractNounPhrases(reviewItems.get(1)));
        //System.out.println(reviewItems.size());
        //System.exit(1);

        String outFile = "prs-review-parsing/data/parsed_np.csv";
        rp.countNounPhrases(reviewItems, outFile);
    }

}
