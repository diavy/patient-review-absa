package com.pesi.nlp4j;

/**
 * Created by xshuai on 2/25/17.
 */

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.tokenizer.EnglishTokenizer;
import edu.emory.mathcs.nlp.component.tokenizer.Tokenizer;
import edu.emory.mathcs.nlp.component.tokenizer.token.Token;
import edu.emory.mathcs.nlp.decode.NLPDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class NLP4JExample {

    private void tokenizeRaw() throws IOException {
        Tokenizer tokenizer = new EnglishTokenizer();
        String inputFile = "nlp4j-module-test/src/main/resources/test.txt";
        InputStream in = IOUtils.createFileInputStream(inputFile);

        for (List<Token> tokens : tokenizer.segmentize(in))
            //System.out.println(Joiner.join(tokens, " "));
            System.out.println(tokens);

        in.close();

    }

    public void tokenizeLine() throws IOException {
        Tokenizer tokenizer = new EnglishTokenizer();
        String inputFile = "nlp4j-module-test/src/main/resources/test.txt";
        BufferedReader in = IOUtils.createBufferedReader(inputFile);
        List<Token> tokens;
        String line;

        while ((line = in.readLine()) != null) {
            tokens = tokenizer.tokenize(line);
            System.out.println(line);
            //System.out.println(Joiner.join(tokens, " "));
            System.out.println(tokens);
        }

        in.close();
    }

    public void NLPDecodeExample() throws IOException {
        String pathToConfig = "nlp4j-module-test/src/main/resources/configuration/config-decode-en.xml";
        NLPDecoder decoder = new NLPDecoder(IOUtils.getInputStream(pathToConfig));
        //NLPNode[] nodes = decoder.decode("I have a dream that one day I can fly.");

        final String inputFile = "nlp4j-module-test/src/main/resources/test.txt";
        final String outputFile = "nlp4j-module-test/src/main/resources/outputfile.tsv";

        InputStream in = IOUtils.createFileInputStream(inputFile);
        OutputStream out = IOUtils.createFileOutputStream(outputFile);

        decoder.decodeRaw(in, out);
        in.close();
        out.close();


    }

    public static void main(String[] args) throws IOException {
        NLP4JExample nlp4jExp = new NLP4JExample();
        //nlp4jExp.tokenizeRaw();
        //nlp4jExp.tokenizeLine();
        nlp4jExp.NLPDecodeExample();

    }

}
