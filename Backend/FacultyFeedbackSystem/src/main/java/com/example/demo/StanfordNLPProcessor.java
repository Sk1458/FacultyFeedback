package com.example.demo;

import java.util.Properties;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class StanfordNLPProcessor {
	
	private static StanfordCoreNLP pipeline;

    static {
        // Set up the pipeline properties for CoreNLP
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,sentiment");

        // Initialize the pipeline with the properties
        pipeline = new StanfordCoreNLP(props);
    }
    
    public static Annotation processText(String text) {
        // Create an annotation object and process the text
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        return document;
    }

}
