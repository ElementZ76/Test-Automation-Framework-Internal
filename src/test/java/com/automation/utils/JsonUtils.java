package com.automation.utils;

import java.io.*;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {
	public static <T> List<T> getTestData(String jsonFileName, TypeReference<List<T>> type) throws IOException {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + jsonFileName;
		return new ObjectMapper().readValue(new File(filePath), type);
	}
}
