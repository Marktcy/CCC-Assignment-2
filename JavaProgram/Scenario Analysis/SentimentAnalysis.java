/****************************************************
 * COMP90024 Cluster and Cloud Computing Project 2	*
 * File : Sentiment Analysis.java					*
 * Author : CCC2017 - Team26						*
 * City : Melbourne									*
 * Member : Shixun Liu, 766799						*
 * Member : Yuan Bing, 350274						*
 * Member : Renyi Hou, 764696						*
 * Member : Mark Chun Yong Ting, 805780				*
 * Member : Kaiqing Wang, 700275					*
 * Date : 30/4/2017									*
****************************************************/
package Cloud_Computing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream; 
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
 
import edu.stanford.nlp.ling.CoreAnnotations; 
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations; 
import edu.stanford.nlp.pipeline.Annotation; 
import edu.stanford.nlp.pipeline.StanfordCoreNLP; 
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree; 
import edu.stanford.nlp.util.CoreMap;
import mix_final.test1.SentimentResult;

/*
	in this part, the stanford coreNLP library is used to judge the sentiment in this case
	As the discussion in the paper "Elastic distributed computing System for tweet sentiment analysis"
	the classifier testing accuracy is 46.4% with stanford corpus
*/
public class SentimentAnalysis 
{
	public void run() throws IOException
	{
		BufferedReader br = null;
		BufferedWriter bw = null;
		File file1 = new File(fetchDataFile);
		File file2 = new File(outputSentimentFile);
		
		br = new BufferedReader(new FileReader(file1));
		bw = new BufferedWriter(new FileWriter(file2));
		
		String le1 = br.readLine();
		while(le1 != null)
		{
			String[] l = le1.split(",,,");
			/*l[0] : id
			 * l[1] : source
			 * l[2]: date
			 * l[3]: longitude
			 * l[4]: latitude
			 * l[5]: text
			 *  the l is string list which contains the json information
			 * 
			 */
			bw.write(l[0] + "," + "," + l[1] + "," + l[2] + ",(" + l[3] + "," + l[4] + ")," + getSentiment(l[5]) + "\n");
			bw.flush();
			le1 = br.readLine();
		}
		bw.close();
		br.close();
	}
	public static void main(String[] args) throws IOException
	{
		SentimentAnalysis a = new SentimentAnalysis();
				a.run();
		System.out.println("end");
	}
	
	
	
	// This part is about sentiment analysis
	private static final Logger logger = LoggerFactory.getLogger(test1.class); 
    private StanfordCoreNLP pipeline = new StanfordCoreNLP(getProperties());
	private Properties getProperties() { 
        Properties props = new Properties(); 
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment"); 
        return props; 
    } 
    
    public void init() { 
        // because scientists can't code, and write debug messages to System.err 
        PrintStream err = new PrintStream(System.err) { 
            @Override 
            public void println(String x) { 
                if (!x.startsWith("Adding annotator")) { 
                    super.println(x); 
                } 
            } 
        }; 
        System.setErr(err); 
    } 
    
	public String retrieveSourceFromTweet(String source)
	{
		String pattern = ">.+</a>";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(source);
		if (m.find())
		{	
			if (m.group(0).contains(" "))
			{
				String[] sp = m.group(0).split("\\s+");
				sp[sp.length - 1] = sp[sp.length - 1].replace("</a>", "");
				sp[sp.length - 1] = sp[sp.length - 1].replace(">", "");
				return sp[sp.length - 1];
			}
			else
			{
				String sourceString = m.group(0);
				sourceString = sourceString.replace("</a>", "");
				sourceString = sourceString.replace(">", "");
				return sourceString;
			}
		}
		return null;
	}
	
	public static enum SentimentResult 
	{ 
        POSITIVE, NEGATIVE, NEUTRAL; 
    }
	
	public SentimentResult getSentiment(String text)
    {
    	double sentimentSum = 0;
    	int mainSentiment = 0;
    	
    	int longest = 0;
    	try
    	{
    		Annotation annotation = pipeline.process(text);
    		for (CoreMap sentence: annotation.get(CoreAnnotations.SentencesAnnotation.class))
    		{
    			Tree tree = sentence.get(SentimentAnnotatedTree.class); 
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree); 
                String partText = sentence.toString();
                if (partText.length() > longest) { 
                    mainSentiment = sentiment; 
                    longest = partText.length(); 
                }
    		}
    	}
    	catch (Exception ex) { 
            logger.error("Problem analyzing document sentiment. " + text, ex);
    	}
    	sentimentSum += mainSentiment;
    	
		// the configuration that judge the sentiment
    	double average = sentimentSum;
    	if (average >= 2.25) { 
            return SentimentResult.POSITIVE; 
        } else if (average <= 1.75) { 
            return SentimentResult.NEGATIVE; 
        } 
        return SentimentResult.NEUTRAL;
    }
}
