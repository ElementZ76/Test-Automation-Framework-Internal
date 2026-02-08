package com.automation.utils;

import java.io.*;
import java.util.List;

import com.automation.models.SauceData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {
	public static List<SauceData> getSauceData(String jsonFileName) throws IOException {
		String filepPath = System.getProperty("user.dir") + "/src/test/resources/testdata/" + jsonFileName;
		ObjectMapper objMapper = new ObjectMapper();
		return objMapper.readValue(new File(filepPath), new TypeReference<List<SauceData>>(){});
	}
}
