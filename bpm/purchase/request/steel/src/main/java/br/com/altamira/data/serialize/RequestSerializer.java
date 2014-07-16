package br.com.altamira.data.serialize;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import br.com.altamira.data.model.Request;

public class RequestSerializer extends JsonSerializer<Request> {

	ObjectMapper objectMapper = new ObjectMapper();
	Version version = new Version(1, 0, 0, "SNAPSHOT", "br.com.altamira", "data.serializer"); // maven/OSGi style version
	private SimpleModule module = new SimpleModule("CustomSerializer", version);
	//ObjectWriter objectWriter = objectMapper.writerWithView(JsonEntityView.class);
	
	BaseSerializer<Request> serializer = new ListSerializer();
	
	public RequestSerializer() {
		module.addSerializer(Request.class, this);
		objectMapper.registerModule(module);
	}
	
	public RequestSerializer(BaseSerializer<Request> serializer) {
		module.addSerializer(Request.class, this);
		
		objectMapper.registerModule(module);
		
		this.serializer = serializer;
	}
	
	public String serialize(List<Request> list)
			throws IOException {
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		for (Request r : list) {
			str.append(serialize(r));
		}
		str.append("]");
		
		return str.toString();
	}

	public String serialize(Request entity) throws IOException {
		//return objectWriter.writeValueAsString(value);
		return objectMapper.writeValueAsString(entity);
	}
	
	@Override
	public void serialize(Request entity, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		serializer.serialize(entity, jgen, provider);
		
	}
	
	public static class EntitySerializer implements BaseSerializer<Request> {
		public void serialize(Request entity, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			jgen.writeStartObject();
			jgen.writeNumberField("id", entity.getId());
			jgen.writeObjectField("created", entity.getCreated());
			jgen.writeStringField("creator", entity.getCreator());
			jgen.writeObjectField("sent", entity.getSent() != null ? entity.getSent() : "");
			if (entity.getItems() == null) {
				jgen.writeArrayFieldStart("items");
				jgen.writeEndArray();
			} else {
				jgen.writeObjectField("items", entity.getItems());
			}
			jgen.writeEndObject();
			
		}
	}
	
	public static class ListSerializer implements BaseSerializer<Request> {
		public void serialize(Request entity, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			jgen.writeStartObject();
			jgen.writeNumberField("id", entity.getId());
			jgen.writeObjectField("created", entity.getCreated());
			jgen.writeStringField("creator", entity.getCreator());
			jgen.writeObjectField("sent", entity.getSent() != null ? entity.getSent() : "");
			jgen.writeEndObject();
			
		}
	}

}