package com.asdbll.twitter;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.trees.Tree;
import java.util.List;
import java.util.Properties;

public class SentimentAnalyzer {
    static StanfordCoreNLP pipeline;

    public SentimentAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static int getSentimentClass(List<CoreMap> sentences) {

        int score = 0; //  0 = very negative, 1 = negative, 2 = neutral, 3 = positive, and 4 = very positive

        for (CoreMap sentence : sentences) {
           // String scoreStr = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            Tree sentiments = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            score = RNNCoreAnnotations.getPredictedClass(sentiments);
            }
        return score;
        }

    public List<CoreMap> getAnnotations(String text) {
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);
        return annotation.get(CoreAnnotations.SentencesAnnotation.class);
    }

}
