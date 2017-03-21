package com.pesi.opennlp;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
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

    private void Chunk(String text) throws IOException {
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

    private void Parse(String text) throws IOException {
        InputStream is = new FileInputStream("resources/opennlp/en-parser-chunking.bin");
        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);

        Parse topParses[] = ParserTool.parseLine(text, parser, 1);
        for (Parse p : topParses) {
            p.show();
        }


    }


    public static void main(String[] args) throws IOException {
        String text = "A misunderstanding between both parties led us to act out of hand. I chose to ignore Dr. Biegel's initial request to discuss my posting. In hindsight, I should have remained open to his concerns. Both Dr. Biegel and I strongly believe in a person's right to express their opinions in a public forum. We both encourage the internet community to act responsibly.";
        OpenNLPExample openNLP = new OpenNLPExample();
        openNLP.SentenceDetect(text);
        openNLP.Tokenize(text);
        openNLP.POSTag(text);
        openNLP.Chunk(text);
        openNLP.Parse(text);

    }
}
