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

import br.com.altamira.data.dao.MaterialDao;
import br.com.altamira.data.model.Material;

/**
 *
 */
@Stateless
@Path("material")
public class MaterialEndpoint {

    @Inject 
    private MaterialDao materialDao;

	@GET
	@Produces("application/json")
	public Response list(
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		List<Material> list;
		
		try {
			list = materialDao.list(startPosition, maxResult);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		if (list.size() == 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		return Response.ok(list).build();
	}
	
    @GET
    @Path("/{id:[1-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") long id) {
    	Material entity = null;
    	
    	try {
    		entity = materialDao.find(id);
    	} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
        return Response.ok(entity).build();
    }

    @POST
    @Consumes("application/json")
    public Response create(Material entity) {
    	
    	try {
    		entity = materialDao.create(entity);
    	} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		return Response.created(
		        UriBuilder.fromResource(MaterialEndpoint.class)
		        .path(String.valueOf(entity.getId())).build())
		        .entity(entity)
		        .build();
    }

    @PUT
    @Path("/{id:[1-9]*}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") long id, Material entity) {
    	
    	if (entity.getId() != id) {
			return Response.status(Status.CONFLICT)
					.entity("entity id doesn't match with resource path id")
					.build();
		}
    	
    	try {
    		entity = materialDao.update(entity);
    	} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response
				.ok(UriBuilder.fromResource(RequestEndpoint.class)
						.path(String.valueOf(entity.getId())).build())
				.entity(entity).build();
    }

    @DELETE
    @Path("/{id:[1-9]*}")
    public Response deleteById(@PathParam("id") long id) {
    	Material entity = null;
    	try {
    		entity = materialDao.remove(id);
    	} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    	
		if (entity == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
    }

}
