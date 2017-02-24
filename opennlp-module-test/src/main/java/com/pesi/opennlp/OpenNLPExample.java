package com.pesi.opennlp;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xshuai on 2/23/17.
 */
public class OpenNLPExample {

    private void SentenceDetect(String text) throws IOException {
        InputStream is = new FileInputStream("resources/opennlp/en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sd = new SentenceDetectorME(model);

        String sentences[] = sd.sentDetect(text);
        for (String sent : sentences) {
            System.out.println(sent);
        }
        is.close();
    }

    private String[] Tokenize(String text) throws IOException {
        InputStream is = new FileInputStream("resources/opennlp/en-token.bin");
        TokenizerModel model = new TokenizerModel(is);

        Tokenizer tokenizer = new TokenizerME(model);

        String tokens[] = tokenizer.tokenize(text);
        for (String a : tokens) {
            System.out.println(a);
        }

        is.close();
        return tokens;

    }

    private String[] POSTag(String text) throws IOException {
        InputStream is = new FileInputStream("resources/opennlp/en-pos-maxent.bin");
        POSModel model = new POSModel(is);

        POSTaggerME tagger = new POSTaggerME(model);

        String[] tokens = Tokenize(text);
        String[] tags = tagger.tag(tokens);

        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i] + "_" + tags[i]);
        }

        is.close();
        return tags;
    }

    private void chunk(String text) throws IOException {
        InputStream is = new FileInputStream("resources/opennlp/en-chunker.bin");
        ChunkerModel model = new ChunkerModel(is);

        ChunkerME chunker = new ChunkerME(model);

        String[] tokens = Tokenize(text);
        String[] tags = POSTag(text);

        String[] chunks = chunker.chunk(tokens, tags);
        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i] + "_" + tags[i] + "_" + chunks[i]);
        }

    }

    public static void main(String[] args) throws IOException {
        String text = "Loving you. It is easy because you do!";
        OpenNLPExample openNLP = new OpenNLPExample();
        openNLP.SentenceDetect(text);
        openNLP.Tokenize(text);
        openNLP.POSTag(text);
        openNLP.chunk(text);

    }
}
