package br.com.altamira.data.serialize;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

@RequestScoped
public interface BaseSerializer<T> {
	
	public abstract void serialize(T value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException, JsonProcessingException;
}
