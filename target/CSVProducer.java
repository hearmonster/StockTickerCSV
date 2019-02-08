import java.util.Date;

import com.opencsv.CSVReader;
//import com.opencsv.CSVWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 'CVSProducer' is a multi-threaded Object (i.e. has a 'run' method) that represents an event source
// It has two private attributes
//	- a pointer to a CSV file object
//	- a Replay Rate

// It is responsible for iterating through the CSF file and firing events at the appropriate times
//	(taken from a specific column within record in the file)

// It was originally designed to be generic so that each CSV file would get its own instance, but that hasn't worked out  :o(
// It maps the schema of the CSV file onto a Class.
// That means it's tied to the StockTicker class, so really it should be named, "StockTickerProducer")

public class CSVProducer implements Runnable {

	CSVReader csvFile;
	Integer iReplayRate;

    static Logger logger = LoggerFactory.getLogger( CSVReplay.class );

	public void setFile (CSVReader csvFileParam ) {
		csvFile = csvFileParam;
    }

	public void setReplayRate(Integer iReplayRateParam) {
		iReplayRate = iReplayRateParam;
	}



	private Integer calcAndSleep(Date dLastTs, Date dTs ) {
		if ( dLastTs == null) dLastTs = dTs;

		Integer iSleepOriginalMS, iSleepAdjustedMS;

		iSleepOriginalMS = (int) ( dTs.getTime() - dLastTs.getTime() );
		iSleepAdjustedMS =  iSleepOriginalMS / iReplayRate;
		logger.debug("Sleep (Original): [" + iSleepOriginalMS + " milliseconds]\t Sleep (Replay Adjusted): [" + iSleepAdjustedMS + " milliseconds]" );
		try {
			//if ( iSleep ) > 0
			Thread.sleep( iSleepAdjustedMS );
		}
		catch (Exception interuppException) {
			logger.error( "Sleep exception");
		}
		return iSleepAdjustedMS;

	}

	public void run() {
        try {
        	//CSVReader reader = new CSVReader(new FileReader(ADDRESS_FILE));
	        String[] nextLine;
	        Date dLastTs = null;

	        Integer iRowNum = 2;

	        csvFile.readNext(); //skip first line
	        while ((nextLine = csvFile.readNext()) != null) {
	        	Date dTs;
	        	Double dPrice;
	        	Integer iVolume, iSleep;
	        	String sTicker;

	        	StockTick tick = new StockTick();

	        	dTs = tick.setTimestamp( nextLine[0], iRowNum.toString() );
	        	iSleep = calcAndSleep( dLastTs, dTs );
	        	dPrice = tick.setPrice( nextLine[2], iRowNum.toString() );
	        	iVolume = tick.setVolume( nextLine[3], iRowNum.toString() );
	        	sTicker = tick.setSymbol( nextLine[1] );

	        	dLastTs = dTs;
	        	iRowNum++;

	        	//Firing off the event will go here...


	            logger.info("Ts: [" + dTs + "]\tTicker: [" + sTicker + "]\tPrice: [" + dPrice + "]\tVolume: [" + iVolume + "]");
	        }

        }
        catch (Exception IOException) {
        	logger.error( "File IOException");
        }

	}


}

