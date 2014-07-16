package br.com.altamira.data.service;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.data.dao.RequestItemDao;
import br.com.altamira.data.model.RequestItem;
import br.com.altamira.data.serialize.RequestItemSerializer;

@Stateless
@Path("request/{requestId:[0-9]*}/item")
public class RequestItemEndpoint {
	
	@Inject
	private RequestItemDao requestItemDao;

	@GET
	@Produces("application/json")
	public Response list(@PathParam("requestId") Long requestId,
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		List<RequestItem> list = requestItemDao.list(requestId, startPosition, maxResult);
		
		RequestItemSerializer serializer = new RequestItemSerializer();
		
		return Response.ok(serializer.serialize(list)).build();
	}
	
	@GET
	@Path("{id:[0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("requestId") Long requestId, @PathParam("id") long id)
			throws IOException {

		RequestItem entity = requestItemDao.find(id);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		RequestItemSerializer serializer = new RequestItemSerializer(new RequestItemSerializer.EntitySerializer());
		
		return Response.ok(serializer.serialize(entity)).build();
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response create(@PathParam("requestId") Long requestId, RequestItem entity) throws IllegalArgumentException, UriBuilderException, JsonProcessingException {
		
		requestItemDao.create(entity);

		RequestItemSerializer serializer = new RequestItemSerializer(new RequestItemSerializer.EntitySerializer());
		
		return Response
				.created(
						UriBuilder.fromResource(RequestItemEndpoint.class)
								.path(String.valueOf(entity.getId())).build(requestId))
				.entity(serializer.serialize(entity)).build();
	}

	@PUT
	@Path("{id:[0-9]*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response update(@PathParam("requestId") Long requestId, @PathParam("id") long id, RequestItem entity) throws IllegalArgumentException, UriBuilderException, JsonProcessingException
			 {

		if (entity.getId() != id) {
			return Response.status(Status.CONFLICT)
					.entity("entity id doesn't match with resource path id")
					.build();
		}

		entity = requestItemDao.update(entity);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		RequestItemSerializer serializer = new RequestItemSerializer(new RequestItemSerializer.EntitySerializer());
		
		return Response
				.ok(UriBuilder.fromResource(RequestItemEndpoint.class)
						.path(String.valueOf(entity.getId())).build(requestId))
				.entity(serializer.serialize(entity)).build();
	}

	@DELETE
	@Path("{id:[0-9]*}")
	public Response removeById(@PathParam("id") long id) {
		RequestItem entity = requestItemDao.remove(id);
		if (entity == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
	}

	/* 
	 * Custom methods 
	 */
	
}
