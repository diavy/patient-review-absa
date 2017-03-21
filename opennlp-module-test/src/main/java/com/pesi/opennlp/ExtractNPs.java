package com.pesi.opennlp;

/**
 * Created by xshuai on 3/21/17.
 */

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class ExtractNPs {
    static String sentence = "A misunderstanding between both parties led us to act out of hand. I chose to ignore Dr. Biegel's initial request to discuss my posting. In hindsight, I should have remained open to his concerns. Both Dr. Biegel and I strongly believe in a person's right to express their opinions in a public forum. We both encourage the internet community to act responsibly.";

    static Set<String> nounPhrases = new HashSet<>();

    public static void main(String[] args) {

        InputStream modelInParse = null;
        try {
            //load chunking model
            modelInParse = new FileInputStream("resources/opennlp/en-parser-chunking.bin"); //from http://opennlp.sourceforge.net/models-1.5/
            ParserModel model = new ParserModel(modelInParse);

            //create parse tree
            Parser parser = ParserFactory.create(model);
            Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

            //call subroutine to extract noun phrases
            for (Parse p : topParses)
                getNounPhrases(p);

            //print noun phrases
            for (String s : nounPhrases)
                System.out.println(s);

            //The Call
            //the Wild?
            //The Call of the Wild? //punctuation remains on the end of sentence
            //the author of The Call of the Wild?
            //the author
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelInParse != null) {
                try {
                    modelInParse.close();
                } catch (IOException e) {
                }
            }
        }
    }

    //recursively loop through tree, extracting noun phrases
    public static void getNounPhrases(Parse p) {

        if (p.getType().equals("NP")) { //NP=noun phrase
            nounPhrases.add(p.getCoveredText());
        }
        for (Parse child : p.getChildren())
            getNounPhrases(child);
    }
}
