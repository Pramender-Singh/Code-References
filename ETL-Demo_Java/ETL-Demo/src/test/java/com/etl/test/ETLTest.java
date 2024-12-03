package com.etl.test;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.etl.utilities.CSVUtils;
import com.etl.utilities.DataFilter;
import com.etl.utilities.JSONUtils;

public class ETLTest {

	private static final Logger logger = LoggerFactory.getLogger(ETLTest.class);

	@Test
	public void CSVDataTest() throws Exception {

		CSVUtils csvUtils = new CSVUtils();
		JSONUtils jsonUtils = new JSONUtils();

		String inputPath = jsonUtils.getPaths().get("INPUT_PATH");
		String outputPath = jsonUtils.getPaths().get("OUTPUT_PATH");

		// Extract data from input CSV
		List<String[]> extractedData = csvUtils.extractData(inputPath);
		logger.info("Extracted Data: {} rows", extractedData.size());
		printData(extractedData);

		DataFilter dataFilter = new DataFilter();
		// Remove invalid rows
		List<String[]> validData = dataFilter.removeInvalidRows(extractedData);
		logger.info("Valid Data: {} rows", validData.size());
		printData(validData);

		// Transform data
		List<String[]> transformedData = dataFilter.transformData(validData);
		logger.info("Transformed Data: {} rows", transformedData.size());
		printData(transformedData); 
		
		// Write data to output CSV file
		try {
			csvUtils.writeData(outputPath, transformedData);
			logger.info("Data written to output CSV file successfully.");
		} catch (Exception e) {
			logger.error("Error writing data to CSV file: ", e);
		}
		
		List<String[]> dataWithoutHeader = transformedData.subList(1, transformedData.size());
		
		// The expected value is taken for assertion purposes - This is 
        List<String[]> expectedData = List.of(
                new String[]{"Kumar Amit", "amit@yopmail.com", "28", "India", "Asia"},
                new String[]{"Akash Grover", "akash@yopmail.com", "30", "USA", "North America"},
                new String[]{"Arun Kumar", "arun@yopmail.com", "18", "Russia", "Other"},
                new String[]{"John", "john@yopmail.com", "24", "Germany", "Europe"}
            );
        
        Assert.assertEquals(dataWithoutHeader.size(), expectedData.size(), "The number of rows should match.");
        
        for (int i = 0; i < dataWithoutHeader.size(); i++) {
            Assert.assertEquals(dataWithoutHeader.get(i), expectedData.get(i), "Row " + i + " is incorrect.");
        }
	}

	private void printData(List<String[]> data) {
		for (String[] row : data) {
			System.out.println(String.join(", ", row));
		}
		System.out.println("\n=======================================\n ");
	}

}
