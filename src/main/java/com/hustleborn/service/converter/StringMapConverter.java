package com.hustleborn.service.converter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringMapConverter implements AttributeConverter<Map<String, Object>, String> {

	private Logger logger = LoggerFactory.getLogger(StringMapConverter.class);

	@Override
	public String convertToDatabaseColumn(Map<String, Object> data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			logger.error("Cannot convert map to json string.", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return new LinkedHashMap<>();
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(dbData, LinkedHashMap.class);
		} catch (IOException e) {
			logger.error("Error while trying to convert string(JSON) to map data structure.");
		}
		return new LinkedHashMap<>();
	}

}
