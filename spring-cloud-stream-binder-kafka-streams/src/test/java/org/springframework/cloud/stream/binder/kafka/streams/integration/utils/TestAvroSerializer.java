/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.binder.kafka.streams.integration.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import org.springframework.cloud.function.context.converter.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.function.context.converter.avro.AvroSchemaServiceManagerImpl;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Custom avro serializer intended to be used for testing only.
 *
 * @param <S> Target type to serialize
 * @author Soby Chacko
 */
public class TestAvroSerializer<S> implements Serializer<S> {

	public TestAvroSerializer() {
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public byte[] serialize(String topic, S data) {
		AvroSchemaMessageConverter avroSchemaMessageConverter = new AvroSchemaMessageConverter(new AvroSchemaServiceManagerImpl());
		Message<?> message = MessageBuilder.withPayload(data).build();
		Map<String, Object> headers = new HashMap<>(message.getHeaders());
		headers.put(MessageHeaders.CONTENT_TYPE, "application/avro");
		MessageHeaders messageHeaders = new MessageHeaders(headers);
		final Object payload = avroSchemaMessageConverter
				.toMessage(message.getPayload(), messageHeaders).getPayload();
		return (byte[]) payload;
	}

	@Override
	public void close() {

	}

}
