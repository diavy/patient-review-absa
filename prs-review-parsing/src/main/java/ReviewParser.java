/**
 * Created by U6026806 on 1/5/17.
 * This module is used to parse the NLP features of review content
 */

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class ReviewParser {

    private String reviewFile = "prs-review-parsing/data/review_labeling.xlsx";

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

        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentence);
            System.out.println("sentiment:" + sentiment);

        }


    }

    public void parseMultipleReviews(List<String> reviewTextList) {

    }


    public static void main(String[] args) throws IOException {
        ReviewParser rp = new ReviewParser();

        List<String> reviewItems = rp.extractReviewContent(rp.reviewFile);
        rp.parseSingleReview(reviewItems.get(1));
    }

}
