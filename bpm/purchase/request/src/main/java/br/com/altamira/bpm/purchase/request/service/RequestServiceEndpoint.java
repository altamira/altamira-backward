package br.com.altamira.bpm.purchase.request.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.camunda.bpm.engine.RuntimeService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullValueSerializer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Stateless
@Path("send")
public class RequestServiceEndpoint {
	
	private final static Logger LOGGER = Logger.getLogger(RequestServiceEndpoint.class.getName());
	
	public static final String PROCESS_ID = "br.com.altamira.bpm.purchase.request.steel";

	@Inject
	private RequestDao requestDao;
	
	@Inject
	private RuntimeService runtimeService;

//	@Inject
//	private ProcessEngine processEngine;

	@GET
	@Path("{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response startProcess(@PathParam("id") long id) throws IOException {
		Request entity = null;
		
		try {
			entity = requestDao.find(id);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		Request request = null;
		try {
			entity.setSent(new Date());
			request = requestDao.update(entity);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		if (request == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("The Request entity not found !").build();
		}
		
		LOGGER.info("The request " + request.getId() + " was send sucessfull");
		
	    HashMap<String, Object> variables = new HashMap<String, Object>();
	    variables.put("REQUEST_ID", request.getId());

	    try {
	    	runtimeService.startProcessInstanceByKey("br.com.altamira.bpm.purchase.request", variables);
	    } catch(Exception e) {
	    	entity.setSent(null);
	    	requestDao.update(entity);
	    	return Response.status(Status.INTERNAL_SERVER_ERROR).entity("The process can't be started !").build();
	    }

	    ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);
		
		return Response
				.created(
						UriBuilder.fromResource(RequestServiceEndpoint.class)
								.path(String.valueOf(request.getId())).build())
				.entity(writer.writeValueAsString(request)).build();
	}

	/*@Produces
	@Named("currentRequest")
	public Request getCurrent() {
		return new Request(null, DateTime.now().toDate(), processEngine.getIdentityService().createUserQuery().userId(identityService.getCurrentAuthentication().getUserId() "demo").singleResult().getFirstName());
		
	}*/

	/*public Request save(final Request request) {
		entityManager.persist(request);
		return request;
	}

	public Request findById(final long id) {
		return entityManager.find(Request.class, id);
	}

	public void update(final Request request) {
		entityManager.merge(request);
	}*/
	
	/*public List<Request> list() {
		return requestDao.getAll(0, 10);
	}*/

}
