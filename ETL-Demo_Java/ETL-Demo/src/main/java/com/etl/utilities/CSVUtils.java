package com.etl.utilities;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVUtils {

	private static final Logger logger = LoggerFactory.getLogger(CSVUtils.class);

	// Read the input CSV file
	public List<String[]> extractData(String inputFile) throws Exception {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(inputFile));
			List<String[]> data = reader.readAll();
			logger.info("\nExtracted {} records from input CSV file.", data.size());
			return data;
		} catch (Exception e) {
			logger.error("Error extracting data from input CSV file: ", e);
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	// Write data to a CSV file
	public void writeData(String outputFile, List<String[]> data) throws Exception {
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(outputFile));
			writer.writeAll(data);
			logger.info("Successfully wrote {} records to output CSV file.", data.size());
		} catch (Exception e) {
			logger.error("Error writing data to output CSV file: ", e);
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
