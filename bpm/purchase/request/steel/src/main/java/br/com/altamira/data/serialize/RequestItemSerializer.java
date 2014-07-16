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

import br.com.altamira.data.model.RequestItem;
//import br.com.altamira.data.serialize.JSonViews.JsonEntityView;

public class RequestItemSerializer extends JsonSerializer<RequestItem> {

	ObjectMapper objectMapper = new ObjectMapper();
	Version version = new Version(1, 0, 0, "SNAPSHOT", "br.com.altamira", "data.serializer.RequestItem"); // maven/OSGi style version
	private SimpleModule module = new SimpleModule("CustomSerializer", version);
	//ObjectWriter objectWriter = objectMapper.writerWithView(JsonEntityView.class);
	
	BaseSerializer<RequestItem> serializer = new ListSerializer();
	
	public RequestItemSerializer() {
		objectMapper.registerModule(module);
	
		module.addSerializer(RequestItem.class, this);
	}

	public RequestItemSerializer(BaseSerializer<RequestItem> serializer) {
		objectMapper.registerModule(module);
		
		module.addSerializer(RequestItem.class, this);
		
		this.serializer = serializer;
	}
	
	public String serialize(List<RequestItem> list)
			throws IOException {
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		for (RequestItem r : list) {
			str.append(serialize(r));
		}
		str.append("]");
		
		return str.toString();
	}

	public String serialize(RequestItem entity) throws JsonProcessingException {
		//return objectWriter.writeValueAsString(value);
		return objectMapper.writeValueAsString(entity);
	}
	
	@Override
	public void serialize(RequestItem entity, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		serializer.serialize(entity, jgen, provider);
		
	}
	
	public static class EntitySerializer implements BaseSerializer<RequestItem> {
		public void serialize(RequestItem entity, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			jgen.writeStartObject();
			jgen.writeNumberField("id", entity.getId());
			jgen.writeObjectField("arrival", entity.getArrival());
			jgen.writeObjectField("weight", entity.getWeight());
			jgen.writeObjectField("material", entity.getMaterial());
			jgen.writeEndObject();
			
		}
	}
	
	public static class ListSerializer implements BaseSerializer<RequestItem> {
		public void serialize(RequestItem entity, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			jgen.writeStartObject();
			jgen.writeNumberField("id", entity.getId());
			jgen.writeObjectField("arrival", entity.getArrival());
			jgen.writeObjectField("weight", entity.getWeight());
			jgen.writeEndObject();
			
		}
	}

}