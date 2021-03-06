# StockTickerCSV

## Purpose
Reads a CSV Stock Ticker file (located in target/resources/stock-trades.csv) and writes them to the log, 
pausing for the appropriate time between events.

It's defined as an Eclipse/Maven project

## Files 
* CSVReplay implements 'main'
* StockTick.java is nothing more than a Java Class representation of the Event's 'schema type' (see the notes in the file for more details)
* CSVProducer.java represents an event source and handles the event timings


It creates an instance of a CSVProducer object, then prepares it for multi-threading

## Logging
Logging is handled by SFJ (Simple Logging for Java) and will be directed to the Console
(Great article here:  http://www.captaindebug.com/2011/09/adding-slf4j-to-your-maven-project.html#.XFnpyFxKhhE )
The pom.xml contains two dependency artifacts; 
* slf4j-api (the *API*) 
* "logback-classic" [ch.qos.logback] (the logger *implementation*)
Note: that I stopped using the 'slf4j-simple' implementation because it only prints out INFO messages and above
(no DEBUG, no TRACE) 

## How to compile it
Maven 'install' goal will produce a jar, 
It produces a self-executing jar file (The pom.xml includes a reference to <mainClass> residing in the 'CSVReplay' class)


## How to run it
use "java jar - StockTickerCSV-0.0.1-SNAPSHOT.jar" to run

Or within Eclipse - run as an "External Tool"
Eclipse "External Tool Configuration":

Property | Value
-------- | -----
Location: | C:\Program Files\Java\jdk1.8.0_201\bin\java.exe
Working Directory: | ${container_loc}
Argument: | -jar ${resource_loc}

You may need to manually copy resources/stock-trades.csv into the 'target' directory!

