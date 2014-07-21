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

import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.dao.RequestItemDao;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.model.RequestItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Stateless
@Path("request/{requestId:[0-9]*}/item")
public class RequestItemEndpoint {
	
	@Inject
	private RequestDao requestDao;
	
	@Inject
	private RequestItemDao requestItemDao;

	@GET
	@Produces("application/json")
	public Response list(@PathParam("requestId") long requestId,
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		List<RequestItem> list = requestItemDao.list(requestId, startPosition, maxResult);
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		
		return Response.ok(mapper.writeValueAsString(list)).build();
	}
	
	@GET
	@Path("{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("requestId") long requestId, @PathParam("id") long id)
			throws IOException {

		RequestItem entity = requestItemDao.find(id);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		
		return Response.ok(mapper.writeValueAsString(entity)).build();
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response create(@PathParam("requestId") long requestId, RequestItem entity) throws IllegalArgumentException, UriBuilderException, JsonProcessingException {
		
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		Request request = requestDao.current();
		
		if (Long.compare(request.getId().longValue(), requestId) != 0) {
			return Response.status(Status.CONFLICT)
					.entity("Request id doesn't match with resource path id")
					.build();
		}
		
		requestItemDao.create(entity);

		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		
		return Response
				.created(
						UriBuilder.fromResource(RequestItemEndpoint.class)
								.path(String.valueOf(entity.getId())).build(requestId))
				.entity(mapper.writeValueAsString(entity)).build();
	}

	@PUT
	@Path("{id:[0-9][0-9]*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response update(@PathParam("requestId") long requestId, @PathParam("id") long id, RequestItem entity) throws IllegalArgumentException, UriBuilderException, JsonProcessingException
			 {

		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (entity.getId() == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if (entity.getId().longValue() != id) {
			return Response.status(Status.CONFLICT)
					.entity("Entity id doesn't match with resource path id")
					.build();
		}

		Request request = requestDao.current();
		
		if (Long.compare(request.getId().longValue(), requestId) != 0) {
			return Response.status(Status.CONFLICT)
					.entity("Request id doesn't match with resource path id")
					.build();
		}
		
		entity = requestItemDao.update(entity);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		
		return Response
				.ok(UriBuilder.fromResource(RequestItemEndpoint.class)
						.path(String.valueOf(entity.getId())).build(requestId))
				.entity(mapper.writeValueAsString(entity)).build();
	}

	@DELETE
	@Path("{id:[0-9][0-9]*}")
	public Response removeById(@PathParam("requestId") long requestId, @PathParam("id") long id) {
		
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
