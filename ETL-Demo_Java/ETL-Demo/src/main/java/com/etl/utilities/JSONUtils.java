package com.etl.utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

	private final String filePath = "./src/test/resource/testdata/filepath.json";

	public List<Map<String, Object>> readJSON() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> data = objectMapper.readValue(new File(filePath),
				new TypeReference<List<Map<String, Object>>>() {
				});
		return data;
	}

	public Map<String, String> getPaths() throws IOException {
		List<Map<String, Object>> jsonData = readJSON();
		Map<String, String> paths = new HashMap<>();
		for (Map<String, Object> map : jsonData) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				paths.put(entry.getKey(), entry.getValue().toString());
			}
		}
		return paths;
	}
}
