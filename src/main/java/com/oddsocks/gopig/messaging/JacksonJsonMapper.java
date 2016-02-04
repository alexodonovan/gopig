package com.oddsocks.gopig.messaging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JacksonJsonMapper implements JsonMapper {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public <T> T fromJson(String json, Class<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (IOException e) {
			logger.error("Error parsing json string {}", json);
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T fromJson(byte[] bytes, Class<T> clz) {
		return fromJson(new String(bytes), clz);
	}

	void setLogger(Logger logger) {
		this.logger = logger;
	}

	void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

}
