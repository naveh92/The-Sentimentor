package sentiment;

import java.util.Properties;

import org.ejml.simple.SimpleMatrix;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.AnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentCalculator {
	private static SentimentCalculator instance;
	private StanfordCoreNLP pipeline;
	
	private SentimentCalculator() {
		init();
	}
	
	public static SentimentCalculator getInstance() {
		if (instance == null) {
			instance = new SentimentCalculator();
		}
		
		return instance;
	}

	public void init() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	public Double calculateSentiment(String tweet) {
		Double mainSentiment = 0d;
		int count = 0;
		
		if (tweet != null && tweet.length() > 0) {
			Annotation annotation = pipeline.process(tweet);
        
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence.get(AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				
				mainSentiment += sentiment;
				count++;
			}
			
			mainSentiment /= count;
		}
		
		return mainSentiment;
    }
}