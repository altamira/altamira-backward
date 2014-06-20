package br.com.altamira.data.serialize;

import java.io.IOException;

import javax.ejb.Stateless;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import br.com.altamira.data.model.Request;

@Stateless
public class RequestEntitySerializer extends JsonSerializer<Request> {

	@Override
	public void serialize(Request value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeObjectField("created", value.getCreated());
		jgen.writeStringField("creator", value.getCreator());
		jgen.writeObjectField("sent", value.getSent() != null ? value.getSent() : "");
		if (value.getItems() == null) {
			jgen.writeArrayFieldStart("items");
			jgen.writeEndArray();
		} else {
			jgen.writeObjectField("items", value.getItems());
		}
		jgen.writeEndObject();
	}

}