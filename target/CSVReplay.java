import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

// CSVReplay implements 'main'
// It creates an instance of a CSVProducer object, then prepares it for multi-threading

// >>> Logging <<<
// Logging is handled by SFJ (Simple Logging for Java) and will be directed to the Console
// Great article here:  http://www.captaindebug.com/2011/09/adding-slf4j-to-your-maven-project.html#.XFnpyFxKhhE
// The pom.xml contains two dependency artifacts; slf4j-api (the API) and slf4j-simple (the logger *implementation*)

// >>> Running <<<
// Maven 'install' goal will produce a jar, use "java jar - StockTickerCSV-0.0.1-SNAPSHOT.jar" to run
// Eclipse "External Tool Configuration":
//		Location:			C:\Program Files\Java\jdk1.8.0_201\bin\java.exe
//		Working Directory:	${container_loc}
//		Argument:  			-jar ${resource_loc}
// The pom.xml includes a reference to <mainClass> residing in the 'CSVReplay' class
// You may need to manually copy resources/stock-trades.csv into the 'target' directory!

public class CSVReplay {

    private static final String CSVFILE1 = "resources/stock-trades.csv";

    static Logger logger = LoggerFactory.getLogger( CSVReplay.class );

	public static void main(String[] args) throws IOException {

		//logger.info( "Hello workld");

		CSVProducer csvProducer1 = new CSVProducer();
		Thread tProducer1 = new Thread( csvProducer1  );

		StockTick dummyTick = new StockTick();	//all I need is the 'setTimestamp' method to parse the Timestamp string

		Integer iReplayRate = 1;

		try {
        	CSVReader reader = new CSVReader(new FileReader(CSVFILE1));
	        String[] sFirstLine;
	        Date dFirstTs = null;

	        reader.readNext(); //skip first line (Header row)
	        sFirstLine = reader.readNext();
        	dFirstTs = dummyTick.setTimestamp( sFirstLine[0], "First line" );
        	logger.debug( "First Ts: [" + dFirstTs + "]");

        	csvProducer1.setFile( reader );
        	csvProducer1.setReplayRate( iReplayRate );
        	tProducer1.start();
        }
        catch (Exception fileNotFoundException) {
    		logger.error( "File not found: " + CSVFILE1 );
        }
	}

}
