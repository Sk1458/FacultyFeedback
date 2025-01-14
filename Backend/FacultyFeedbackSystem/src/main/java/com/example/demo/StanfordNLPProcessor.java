package com.example.demo;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

public class StanfordNLPProcessor {
	
	private static StanfordCoreNLP pipeline;

    static {
        // Set up the pipeline properties for CoreNLP
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,lemma,ner,depparse,sentiment");

        // Initialize the pipeline with the properties
        pipeline = new StanfordCoreNLP(props);
    }
    
    public static String processText(String text) {
        // Create an annotation object and process the text
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        
        // Extract sentiment from the first sentence (or overall if needed)
        String sentiment = document.get(CoreAnnotations.SentencesAnnotation.class)
            .stream()
            .map(sentence -> sentence.get(SentimentCoreAnnotations.SentimentClass.class))
            .findFirst()
            .orElse("Unknown");

        return sentiment;
    }

}
