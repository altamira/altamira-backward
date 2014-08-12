package br.com.altamira.data.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
//import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.hibernate.exception.ConstraintViolationException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.model.RequestItem;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullValueSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Stateless
@Path("request")
public class RequestEndpoint {

	@Inject
	private RequestDao requestDao;

	@GET
	@Produces("application/json")
	public Response list(
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		List<Request> list;
		
		try {
			list = requestDao.list(startPosition, maxResult);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.ListView.class);

		return Response.ok(writer.writeValueAsString(list)).build();
		//return Response.ok(list).build();
	}

	@GET
	@Path("{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") long id)
			throws IOException {
		Request entity = null;
		
		try {
			entity = requestDao.find(id);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);
		
		return Response.ok(writer.writeValueAsString(entity)).build();
	}
	
	/*@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response create(Request entity) throws IllegalArgumentException,
			UriBuilderException, IOException {
		
		try {
			requestDao.create(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		return Response
				.created(
						UriBuilder.fromResource(RequestEndpoint.class)
								.path(String.valueOf(entity.getId())).build())
				.entity(serialize(entity)).build();
	}*/

	@PUT
	@Path("{id:[0-9][0-9]*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response update(@PathParam("id") long id, Request entity)
			throws IllegalArgumentException, UriBuilderException,
			IOException {

		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (entity.getId() == null) {
			return Response.status(Status.CONFLICT)
					.entity("Entity id can't be null")
					.build();
		}
		
		if (entity.getId().longValue() != id) {
			return Response.status(Status.CONFLICT)
					.entity("Entity id doesn't match with resource path id. The result of compare is " + entity.getId().compareTo(id))
					.build();
		}
		
		if (entity.getId().compareTo(requestDao.current().getId()) != 0) {
			return Response.status(Status.CONFLICT)
					.entity("Entity id doesn't match with current Request")
					.build();
		}

		for (RequestItem item : entity.getItems()) {
			item.setRequest(entity);
		}
		
		try {
			entity = requestDao.update(entity);
		} catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            //return createViolationResponse(ce.getConstraintViolations()).build();
            return Response.status(Response.Status.BAD_REQUEST).entity(ce.getMessage()).build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            //Map<String, String> responseObj = new HashMap<String, String>();
            //responseObj.put("email", "Email taken");
            return Response.status(Response.Status.CONFLICT).entity(entity).build();
        } catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);
		
		return Response
				.ok(UriBuilder.fromResource(RequestEndpoint.class)
						.path(String.valueOf(entity.getId())).build())
				.entity(writer.writeValueAsString(entity)).build();
	}

	@DELETE
	@Path("{id:[0-9][0-9]*}")
	public Response removeById(@PathParam("id") long id) {
		Request entity = null;
		
		try {
			entity = requestDao.remove(id);
		} catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            //return createViolationResponse(ce.getConstraintViolations()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(ce.getMessage()).build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            //Map<String, String> responseObj = new HashMap<String, String>();
            //responseObj.put("email", "Email taken");
            return Response.status(Response.Status.CONFLICT).entity(entity).build();
        } catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		if (entity == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		
		return Response.noContent().build();
	}

	/* 
	 * Custom methods 
	 */
	
	@GET
	@Path("/current")
	@Produces("application/json")
	public Response current()
			throws IOException {

		Request entity = null;

		try {
			entity = requestDao.current();
		} catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            //return createViolationResponse(ce.getConstraintViolations()).build();
			return Response.status(Response.Status.BAD_REQUEST).entity(ce.getMessage()).build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            //Map<String, String> responseObj = new HashMap<String, String>();
            //responseObj.put("email", "Email taken");
            return Response.status(Response.Status.CONFLICT).entity(entity).build();
        } catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);
		
		//return Response.ok(serialize(entity)).build();
		return Response.ok(UriBuilder.fromResource(RequestEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(writer.writeValueAsString(entity))
                .build();
	}

    @GET
    @Path("{id:[0-9][0-9]*}/report")
    @Produces("application/pdf")
    public Response reportInPdf(@PathParam("id") long id) {
    	Request entity = null;
    	
        // generate report
        JasperPrint jasperPrint = null;

        try {
        	entity = requestDao.find(id);
        } catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
        
        if (entity == null) {
			return Response.status(Status.NOT_FOUND).entity("A Requisição não foi encontrada.").build();
		}
        
        if (entity.getItems().size() == 0) {
        	return Response.status(Status.BAD_REQUEST).entity("A Requisição não tem itens.").build();
        }
        
        try {
            //byte[] requestReportJasper = requestDao.getRequestReportJasperFile();
            //byte[] requestReportAltamiraimage = requestDao.getRequestReportAltamiraImage();
            byte[] pdf = null;

            //ByteArrayInputStream reportStream = new ByteArrayInputStream(requestReportJasper);
            InputStream reportStream = Request.class.getResourceAsStream("/request.jasper");
            
            if (reportStream == null) {
            	reportStream = Request.class.getResourceAsStream("request.jasper");
            }
            
            if (reportStream == null) {
            	reportStream = this.getClass().getResourceAsStream("/request.jasper");
            }
            
            if (reportStream == null) {
            	reportStream = this.getClass().getResourceAsStream("request.jasper");
            }
            
            if (reportStream == null) {
            	reportStream = Thread.currentThread().getClass().getResourceAsStream("/request.jasper");
            }
            
            if (reportStream == null) {
            	reportStream = Thread.currentThread().getClass().getResourceAsStream("request.jasper");
            }
           
            if (reportStream == null) {
            	return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Não foi possivel carregar o relatorio !").build();
            }
            
            Map<String, Object> parameters = new HashMap<String, Object>();

            //List<Object[]> list = requestDao.selectRequestReportDataById(id);

            //Vector requestReportList = new Vector();
            ArrayList<RequestReportData> requestReportList = new ArrayList<RequestReportData>();
            List<Date> dateList = new ArrayList<Date>();

            Long lastMaterialId = new Long(0);
            int count = 0;
            BigDecimal sumRequestWeight = new BigDecimal(0);
            BigDecimal totalWeight = new BigDecimal(0);

            RequestReportData r = new RequestReportData();
            r.setId(null);
            r.setLamination(null);
            r.setLength(null);
            r.setThickness(null);
            r.setTreatment(null);
            r.setWidth(null);
            r.setArrivalDate(null);
            r.setWeight(null);

            requestReportList.add(r);

            /* 
				0 = M.ID
	            1 = M.LAMINATION
	            2 = M.TREATMENT
	            3 = M.THICKNESS
	            4 = M.WIDTH
	            5 = M.LENGTH
	            6 = RT.WEIGHT
	            7 = RT.ARRIVAL_DATE
            */
            for (RequestItem item : entity.getItems()) {
                RequestReportData rr = new RequestReportData();

                Long currentMaterialId = item.getMaterial().getId();

                if (lastMaterialId.compareTo(currentMaterialId) == 0) {
                    rr.setWeight(item.getWeight());
                    rr.setArrivalDate(item.getArrival());

                    // copy REQUEST_DATE into dateList
                    dateList.add(item.getArrival());

                    totalWeight = totalWeight.add(item.getWeight());
                    sumRequestWeight = sumRequestWeight.add(item.getWeight());
                    count++;
                } else {
                    rr.setId(entity.getId());
                    rr.setLamination(item.getMaterial().getLamination());
                    rr.setTreatment(item.getMaterial().getTreatment());
                    rr.setThickness(item.getMaterial().getThickness());
                    rr.setWidth(item.getMaterial().getWidth());

                    if (item.getMaterial().getLength() != null) {
                        rr.setLength(item.getMaterial().getLength());
                    }

                    rr.setWeight(item.getWeight());
                    rr.setArrivalDate(item.getArrival());

                    // copy ARRIVAL_DATE into dateList
                    dateList.add(item.getArrival());

                    totalWeight = totalWeight.add(item.getWeight());
                    lastMaterialId = currentMaterialId;

                    if (count != 0) {
                        RequestReportData addition = new RequestReportData();
                        addition.setWeight(sumRequestWeight);

                        requestReportList.add(addition);
                    }

                    sumRequestWeight = item.getWeight();
                    count = 0;
                }

                requestReportList.add(rr);
            }

            if (count > 0) {
                RequestReportData addition = new RequestReportData();
                addition.setWeight(sumRequestWeight);

                requestReportList.add(addition);
            }

            InputStream reportLogo = RequestEndpoint.class.getResourceAsStream("/report-logo.png");
            
            BufferedImage imfg = null;
            try {
                //InputStream in = new ByteArrayInputStream(requestReportAltamiraimage);
                imfg = ImageIO.read(reportLogo);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Collections.sort(dateList);

            parameters.put("REQUEST_START_DATE", dateList.get(0));
            parameters.put("REQUEST_END_DATE", dateList.get(dateList.size() - 1));
            parameters.put("REQUEST_ID", id);
            parameters.put("TOTAL_WEIGHT", totalWeight);
            parameters.put("altamira_logo", imfg);
            //parameters.put("USERNAME", httpRequest.getUserPrincipal() == null ? "" : httpRequest.getUserPrincipal().getName());

            Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
            parameters.put("REPORT_LOCALE", locale);

            JRDataSource dataSource = new JRBeanCollectionDataSource(requestReportList, false);

            jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

            pdf = JasperExportManager.exportReportToPdf(jasperPrint);

            ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);

            Response.ResponseBuilder response = Response.ok(pdfStream);
            response.header("Content-Disposition", "inline; filename=Request Report.pdf");

            return response.build();

        } catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } finally {
            try {
                /*if (jasperPrint != null) {
                    // store generated report in database
                    requestDao.insertGeneratedRequestReport(jasperPrint);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not insert generated report in database.");
            }
        }
    }
    
    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    /*private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        //log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }*/

}
