package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Sentence;

public class StanfordNLPProcessor {
	
	private static StanfordCoreNLP pipeline;

    // List of abusive words
    private static final List<String> ABUSIVE_WORDS = Arrays.asList(
        "idiot", "stupid", "useless", "garbage", "waste", "fire", "dumb", "moron", "fool", "disgusting", "trash", "worthless", "rascal", 
        "terrible", "horrible", "pathetic", "hateful", "worst", "foolish", "idiotic", "hates"
    );

    private static final String DEFAULT_MESSAGE = "This feedback was flagged as inappropriate.";

    static {
        // Set up the NLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,lemma,ner,depparse,sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static SentimentResult processText(String text) {
        boolean isAbusive = containsAbusiveWords(text);

        if (isAbusive) {
            return new SentimentResult(DEFAULT_MESSAGE, "Very Negative"); 
        }

        // Process the text with Stanford NLP
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        // Extract sentiment
        String sentiment = document.get(CoreAnnotations.SentencesAnnotation.class)
                .stream()
                .map(sentence -> sentence.get(SentimentCoreAnnotations.SentimentClass.class))
                .findFirst()
                .orElse("Unknown");

        return new SentimentResult(text, mapSentiment(sentiment));
    }

    // Method to check for abusive words
    private static boolean containsAbusiveWords(String text) {
    	
    	List<String> words = new Sentence(text.toLowerCase()).lemmas(); // Extract lemmas (root words)
    	
        for (String word : words) {
            if (ABUSIVE_WORDS.contains(word)) {
                return true;
            }
        }
        return false;
    }

    // Map Stanford NLP sentiment to custom categories
    private static String mapSentiment(String sentiment) {
        switch (sentiment) {
            case "Very Negative": return "Very Negative";
            case "Negative": return "Negative";
            case "Neutral": return "Neutral";
            case "Positive": return "Positive";
            case "Very Positive": return "Very positive";
            default: return "Unkown";
        }
    }

}
