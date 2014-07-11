package br.com.altamira.data.serialize;

import java.io.IOException;

import javax.ejb.Stateless;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import br.com.altamira.data.model.RequestItem;

@Stateless
public class RequestItemSerializer extends JsonSerializer<RequestItem> {

	@Override
	public void serialize(RequestItem value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		// jgen.writeObjectField("createdDate", value.getCreatedDate());
		// jgen.writeStringField("createName", value.getCreatorName());
		jgen.writeEndObject();
	}

}