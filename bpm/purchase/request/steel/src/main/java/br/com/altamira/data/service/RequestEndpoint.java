package br.com.altamira.data.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
import br.com.altamira.data.model.Request;
//import br.com.altamira.data.serialize.NullValueSerializer;
import br.com.altamira.data.serialize.RequestListSerializer;
import br.com.altamira.data.serialize.JSonViews.JsonEntityView;
//import br.com.altamira.data.serialize.JSonViews.JsonListView;






import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Path("request")
public class RequestEndpoint {

	@Inject
	private RequestDao requestDao;

	private String serialize(List<Request> values)
			throws IOException {
		StringBuilder str = new StringBuilder();
		
		for (Request r : values) {
			str.append(serialize(r));
		}
		
		return str.toString();
	}

	private String serialize(Request value) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		Version version = new Version(1, 0, 0, "SNAPSHOT", "br.com.altamira", "data.service.serializer"); // maven/OSGi style version
		SimpleModule module = new SimpleModule("CustomSerializer", version);
		module.addSerializer(Request.class, new RequestListSerializer());
		objectMapper.registerModule(module);

		ObjectWriter objectWriter = objectMapper
				.writerWithView(JsonEntityView.class);

		return objectWriter.writeValueAsString(value);
	}

	@GET
	@Produces("application/json")
	public Response listAll(
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		return Response.ok(
				serialize(requestDao.getAll(startPosition, maxResult))).build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") long id)
			throws IOException {

		Request entity = requestDao.find(id);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(serialize(entity)).build();
	}

	@POST
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
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response update(@PathParam("id") long id, Request entity)
			throws IllegalArgumentException, UriBuilderException,
			IOException {

		if (entity.getId() != id) {
			return Response.status(Status.CONFLICT)
					.entity("entity id doesn't match with resource path id")
					.build();
		}

		entity = requestDao.update(entity);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response
				.ok(UriBuilder.fromResource(RequestEndpoint.class)
						.path(String.valueOf(entity.getId())).build())
				.entity(serialize(entity)).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") long id) {
		Request entity = requestDao.remove(id);
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
	public Response getCurrent()
			throws IOException {

		Request entity;
		try {
			entity = requestDao.getCurrent();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

		if (entity == null) {
			entity = new Request();
			
			entity.setCreated(new Date());
			entity.setCreator("me"); // TODO get current user
			entity.setSent(null);
			
			try {
				requestDao.create(entity);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}

		return Response.ok(serialize(entity)).build();
	}

	/*
	 * @GET
	 * 
	 * @Path("/{id:[0-9][0-9]*}/report")
	 * 
	 * @Produces("application/pdf") public Response
	 * getRequestReportInPdf(@PathParam("id") long requestId) {
	 * 
	 * // generate report JasperPrint jasperPrint = null;
	 * 
	 * try { byte[] requestReportJasper =
	 * requestDao.getRequestReportJasperFile(); byte[]
	 * requestReportAltamiraimage = requestDao.getRequestReportAltamiraImage();
	 * byte[] pdf = null;
	 * 
	 * ByteArrayInputStream reportStream = new
	 * ByteArrayInputStream(requestReportJasper); Map<String, Object> parameters
	 * = new HashMap<String, Object>();
	 * 
	 * List<Object[]> list = requestDao.selectRequestReportDataById(requestId);
	 * 
	 * //Vector requestReportList = new Vector(); ArrayList requestReportList =
	 * new ArrayList(); List<Date> dateList = new ArrayList<Date>();
	 * 
	 * BigDecimal lastMaterialId = new BigDecimal(0); int count = 0; BigDecimal
	 * sumRequestWeight = new BigDecimal(0); BigDecimal totalWeight = new
	 * BigDecimal(0);
	 * 
	 * RequestReportData r = new RequestReportData(); r.setId(null);
	 * r.setLamination(null); r.setLength(null); r.setThickness(null);
	 * r.setTreatment(null); r.setWidth(null); r.setArrivalDate(null);
	 * r.setWeight(null);
	 * 
	 * requestReportList.add(r);
	 * 
	 * for (Object[] rs : list) { RequestReportData rr = new
	 * RequestReportData();
	 * 
	 * BigDecimal currentMaterialId = new BigDecimal(rs[0].toString());
	 * 
	 * if (lastMaterialId.compareTo(currentMaterialId) == 0) { rr.setWeight(new
	 * BigDecimal(rs[6].toString())); rr.setArrivalDate((Date) rs[7]);
	 * 
	 * // copy REQUEST_DATE into dateList dateList.add((Date) rs[7]);
	 * 
	 * System.out.println(new BigDecimal(rs[6].toString())); totalWeight =
	 * totalWeight.add(new BigDecimal(rs[6].toString())); sumRequestWeight =
	 * sumRequestWeight.add(new BigDecimal(rs[6].toString())); count++; } else {
	 * rr.setId(new BigDecimal(rs[0].toString())); rr.setLamination((String)
	 * rs[1]); rr.setTreatment((String) rs[2]); rr.setThickness(new
	 * BigDecimal(rs[3].toString())); rr.setWidth(new
	 * BigDecimal(rs[4].toString()));
	 * 
	 * if (rs[5] != null) { rr.setLength(new BigDecimal(rs[5].toString())); }
	 * 
	 * rr.setWeight(new BigDecimal(rs[6].toString())); rr.setArrivalDate((Date)
	 * rs[7]);
	 * 
	 * // copy ARRIVAL_DATE into dateList dateList.add((Date) rs[7]);
	 * 
	 * totalWeight = totalWeight.add(new BigDecimal(rs[6].toString()));
	 * lastMaterialId = currentMaterialId;
	 * 
	 * if (count != 0) { RequestReportData addition = new RequestReportData();
	 * addition.setWeight(sumRequestWeight);
	 * 
	 * requestReportList.add(addition); }
	 * 
	 * sumRequestWeight = new BigDecimal(rs[6].toString()); count = 0; }
	 * 
	 * requestReportList.add(rr); }
	 * 
	 * if (count > 0) { RequestReportData addition = new RequestReportData();
	 * addition.setWeight(sumRequestWeight);
	 * 
	 * requestReportList.add(addition); }
	 * 
	 * BufferedImage imfg = null; try { InputStream in = new
	 * ByteArrayInputStream(requestReportAltamiraimage); imfg =
	 * ImageIO.read(in); } catch (Exception e1) { e1.printStackTrace(); }
	 * 
	 * Collections.sort(dateList);
	 * 
	 * parameters.put("REQUEST_START_DATE", dateList.get(0));
	 * parameters.put("REQUEST_END_DATE", dateList.get(dateList.size() - 1));
	 * parameters.put("REQUEST_ID", requestId); parameters.put("TOTAL_WEIGHT",
	 * totalWeight); parameters.put("altamira_logo", imfg);
	 * //parameters.put("USERNAME", httpRequest.getUserPrincipal() == null ? ""
	 * : httpRequest.getUserPrincipal().getName());
	 * 
	 * Locale locale = new
	 * Locale.Builder().setLanguage("pt").setRegion("BR").build();
	 * parameters.put("REPORT_LOCALE", locale);
	 * 
	 * JRDataSource dataSource = new
	 * JRBeanCollectionDataSource(requestReportList, false);
	 * 
	 * jasperPrint = JasperFillManager.fillReport(reportStream, parameters,
	 * dataSource);
	 * 
	 * pdf = JasperExportManager.exportReportToPdf(jasperPrint);
	 * 
	 * ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);
	 * 
	 * Response.ResponseBuilder response = Response.ok(pdfStream);
	 * response.header("Content-Disposition",
	 * "inline; filename=Request Report.pdf");
	 * 
	 * return response.build();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return null; } finally { try
	 * { if (jasperPrint != null) { // store generated report in database
	 * requestDao.insertGeneratedRequestReport(jasperPrint); } } catch
	 * (Exception e) { e.printStackTrace();
	 * System.out.println("Could not insert generated report in database."); } }
	 * }
	 */

}
