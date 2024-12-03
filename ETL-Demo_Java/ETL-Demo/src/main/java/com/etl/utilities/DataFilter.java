package com.etl.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(DataFilter.class);

	public List<String[]> removeInvalidRows(List<String[]> rows) {
		List<String[]> validRows = new ArrayList<>();
		for (String[] row : rows) {
			boolean isValid = true;
			for (String field : row) {
				if (field == null || field.trim().isEmpty()) {
					isValid = false;
					break;
				}
			}
			if (isValid) {
				validRows.add(row);
			}
		}
		logger.info("Removed invalid rows. {} valid rows remaining.", validRows.size());
		return validRows;
	}

	public String getRegion(String country) {
		Map<String, String> regionMap = Map.of(
				"USA", "North America", 
				"Canada", "North America", 
				"Germany", "Europe",
				"India", "Asia", 
				"China", "Asia");
		if (regionMap.containsKey(country)) {
			return regionMap.get(country);
		} else {
			return "Other";
		}
	}

	public List<String[]> transformData(List<String[]> rows) {
		List<String[]> transformedRows = new ArrayList<>();

		String[] header = Arrays.copyOf(rows.get(0), rows.get(0).length + 1);
		header[header.length - 1] = "Region";
		transformedRows.add(header);

		for (int i = 1; i < rows.size(); i++) {
			String[] row = rows.get(i);
			if (Integer.parseInt(row[2]) < 18) {
				continue;
			}

			String[] transformedRow = Arrays.copyOf(row, row.length + 1);
			transformedRow[0] = nameTitleCase(row[0]);
			transformedRow[transformedRow.length - 1] = getRegion(row[3]);
			transformedRows.add(transformedRow);
		}
		logger.info("Transformed data. {} rows after transformation.", transformedRows.size());
		return transformedRows;
	}

	public String nameTitleCase(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		String[] nameWords = input.split(" ");
		for (int i = 0; i < nameWords.length; i++) {
			String nameWord = nameWords[i];
			if (!nameWord.isEmpty()) {
				nameWords[i] = nameWord.substring(0, 1).toUpperCase() + nameWord.substring(1).toLowerCase();
			}
		}
		return String.join(" ", nameWords);
	}

}
