package com.oddsocks.gopig.messaging;

public interface JsonMapper {

	<T> T fromJson(String json, Class<T> type);

	<T> T fromJson(byte[] payload, Class<T> clz);

}
