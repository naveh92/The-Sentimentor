package dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import main.Constants;
import sentiment.AreaSentiment;

public class FileManager {
	/**
	 * This function edits the html map file
	 * NOTE: No need for thread safety, because we send the values and not the whole map.
	 * @param areaSentiments
	 */
	public void editHtmlFile(Collection<AreaSentiment> areaSentiments, int numberOfTweets) {
		try
	    {
		 	String line;
		 	StringBuffer fileContent = new StringBuffer();
	        File originalMapFile = new File(Constants.ORIGINAL_MAP_URL);
	        FileReader fr = new FileReader(originalMapFile);
	        BufferedReader br = new BufferedReader(fr);
	        line = br.readLine();
	        
	        while (line != null)
	        {
	        	fileContent.append(" ").append(line);
	        	
	            if (line.contains("Average Happiness")) {
	            	synchronized (areaSentiments) {
	            		for (AreaSentiment sentiment : areaSentiments) {
	            			Double percentOfAllTweets = (double)sentiment.getNumberOfTweets()*100 / numberOfTweets;
	            			percentOfAllTweets = Constants.decimalTrim(percentOfAllTweets);
	            			
	            			fileContent
	            			.append("[")
	            			// {v:"RU",f:"RUSSIA (RU)"}
	            			.append("{v:\"")
	            			.append(sentiment.getCountry().getCode())
	            			.append("\", f:\"")
	            			.append(sentiment.getCountry().toString())
	            			.append(" - ")
	            			.append(percentOfAllTweets)
	            			.append("%\"")
	            			.append("}")
	            			
	            			.append(", ")
	            			.append(sentiment.getAvgHappiness())
	            			.append(", ")
	            			.append(sentiment.getNumberOfTweets())
	            			.append("],");
	            		}
	            	}
	            }
	            
	            line = br.readLine();
	        }

	        Path newMapPath = Paths.get(Constants.NEW_MAP_URL);
	        BufferedWriter writer = Files.newBufferedWriter(newMapPath, StandardCharsets.UTF_8);
	        writer.write(fileContent.toString());
	        
	        writer.close();
	        br.close();
	        fr.close();
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
	}
}
