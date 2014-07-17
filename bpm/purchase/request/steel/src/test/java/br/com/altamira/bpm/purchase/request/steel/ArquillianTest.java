package br.com.altamira.bpm.purchase.request.steel;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import br.com.altamira.data.dao.MaterialDao;
import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.dao.RequestItemDao;
import br.com.altamira.data.model.Material;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.model.RequestItem;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullValueSerializer;

@RunWith(Arquillian.class)
public class ArquillianTest {

	private static final String PROCESS_DEFINITION_KEY = "bpm.purchase.request.steel";

	@Deployment
	public static WebArchive createDeployment() {
		// resolve given dependencies from Maven POM
		File[] libs = Maven.resolver().offline(false)
				.loadPomFromFile("pom.xml").importRuntimeAndTestDependencies()
				.resolve().withTransitivity().asFile();

		return ShrinkWrap
				.create(WebArchive.class, "SteelTest.war")
				// add needed dependencies
				.addAsLibraries(libs)
				// prepare as process application archive for camunda BPM
				// Platform
				.addAsWebResource("META-INF/test-processes.xml",
						"WEB-INF/classes/META-INF/processes.xml")
				// enable CDI
				.addAsWebResource("WEB-INF/beans.xml", "WEB-INF/beans.xml")
				// boot JPA persistence unit
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				// add your own classes (could be done one by one as well)
				.addPackages(false,
						"br.com.altamira.bpm.purchase.request.steel")
				// not recursive to skip package 'nonarquillian'
				.addPackages(true, "br.com.altamira.data")
				.addPackage("br.com.altamira.data.model")
				.addPackage("br.com.altamira.data.serialize")
				// add process definition
				.addAsResource("process.bpmn")
				// add process image for visualizations
				.addAsResource("process.png")
		// now you can add additional stuff required for your test case
		;
	}

	@Inject
	private ProcessEngine processEngine;

	@Inject
	private RequestDao requestDao;
	
	@Inject 
	private MaterialDao materialDao;
	
	@Inject
	private RequestItemDao requestItemDao;

	/**
	 * Tests that the process is executable and reaches its end.
	 */
	@Test
	public void testProcessExecution() throws Exception {
		cleanUpRunningProcessInstances();

		Request entity = new Request();

		entity.setCreated(new Date());
		entity.setCreator("Arquillian");
		entity.setSent(null);

		requestDao.create(entity);

		HashMap<String, Object> variables = new HashMap<String, Object>();

		variables.put("REQUEST_ID", entity.getId());

		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

		// assertEquals(1,
		// processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).finished().count());
		assertEquals(1,
				processEngine.getRuntimeService().createProcessInstanceQuery()
						.processInstanceId(processInstance.getId()).count());
	}

	/**
	 * Helper to delete all running process instances, which might disturb our
	 * Arquillian Test case Better run test cases in a clean environment, but
	 * this is pretty handy for demo purposes
	 */
	private void cleanUpRunningProcessInstances() {
		List<ProcessInstance> runningInstances = processEngine
				.getRuntimeService().createProcessInstanceQuery()
				.processDefinitionKey(PROCESS_DEFINITION_KEY).list();
		for (ProcessInstance processInstance : runningInstances) {
			processEngine.getRuntimeService().deleteProcessInstance(
					processInstance.getId(),
					"deleted to have a clean environment for Arquillian");
		}
	}

	public final static String url = "http://localhost:8080/bpm-purchase-request-steel/rest/request";
	
	@Test
	@InSequence(10)
	public void MaterialDaoTest() {
		Material material = new Material();
		
		material.setLamination("FT");
		material.setTreatment("PT");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(1000.0));
		material.setTax(new BigDecimal(4.5));
		
		Material entity = materialDao.create(material);
		
		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertEquals(entity.getLamination(), material.getLamination());
		assertEquals(entity.getTreatment(), material.getTreatment());
		assertEquals(entity.getThickness(), material.getThickness());
		assertEquals(entity.getWidth(), material.getWidth());
		assertEquals(entity.getLength(), material.getLength());
		assertEquals(entity.getTax(), material.getTax());
		
		material = entity;
		
		material.setLamination("TX");
		material.setTreatment("TX");
		material.setThickness(new BigDecimal(3.5));
		material.setWidth(new BigDecimal(100.5));
		material.setLength(new BigDecimal(2000.5));
		material.setTax(new BigDecimal(1.5));
		
		Material updated = materialDao.update(material);
		
		Material found = materialDao.find(entity);
		
		assertNotNull(found);
		assertTrue(found.equals(updated));
		
		Material find = materialDao.find(entity.getLamination(), entity.getTreatment(), entity.getThickness(), entity.getWidth(), entity.getLength());
		
		assertTrue(found.equals(find));
		
		Material removed = materialDao.remove(entity);
		
		//assertNull(remove.getId()); // Id is not changed to null after remove
		
		Material notfound = materialDao.find(removed);
		
		assertNull(notfound);
		
	}
	
	@Test
	@InSequence(20)
	public void RequestDaoTest() throws Exception {
		Request request = new Request();
		
		request.setCreated(new Date());
		request.setCreator("RequestDaoTest");
		request.setSent(null);
		
		requestDao.create(request);
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("FQ");
		material.setTreatment("PR");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(0));
		material.setTax(new BigDecimal(4.5));

		materialDao.create(material);
		
		RequestItem item = new RequestItem();
		
		item.setRequest(request);
		item.setArrival(new Date());
		item.setWeight(new BigDecimal(1234.0));
		item.setMaterial(material);
		
		Set<RequestItem> items = new HashSet<RequestItem>();
		items.add(item);
		
		request.setItems(items);
		
		// Insert Item
		Request entity = requestDao.update(request);
		
		// be sure that the item was stored correctly
		for (RequestItem r : entity.getItems()) {
			assertNotNull(r.getId());
			assertNotNull(r.getMaterial().getId());
		}
		
		Request found = requestDao.find(entity.getId());
		
		assertTrue(entity.equals(found));
		assertFalse(entity.getItems().isEmpty());
		assertEquals(1, entity.getItems().size());
		assertNotNull(entity.getItems().contains(item));
		
		// be sure that the item was stored correctly
		for (RequestItem r : found.getItems()) {
			assertNotNull(r.getId());
			assertNotNull(r.getMaterial().getId());
		}
		
		List<Request> list = requestDao.list(0, 10);
		
		assertNotNull(list);
		assertNotEquals(0, list.size());
		
		Long id = entity.getId();
		
		requestDao.remove(entity);
		
		Request exist = requestDao.find(id);
		
		assertNull(exist);
	}

	@Test
	@InSequence(30)
	public void RequestItemDaoTest() throws Exception {
		
		Request request = requestDao.current();
		
		if (request == null) {
			request = new Request();
			
			request.setCreated(new Date());
			request.setCreator("RequestDaoTest");
			request.setSent(null);
			
			requestDao.create(request);
		}
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("FQ");
		material.setTreatment("PR");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(0));
		material.setTax(new BigDecimal(4.5));
		
		RequestItem entity = new RequestItem();
		
		entity.setRequest(request);
		entity.setMaterial(material);
		entity.setArrival(new Date());
		entity.setWeight(new BigDecimal(1234.0));

		requestItemDao.create(entity);
		
		// be sure that the item was stored correctly
		assertNotNull(entity.getId());
		assertNotNull(entity.getMaterial().getId());
		
		RequestItem found = requestItemDao.find(entity.getId());
		
		assertTrue(found.equals(entity));
		assertTrue(found.getMaterial().equals(entity.getMaterial()));
		
		material.setLamination("TT");
		material.setTreatment("TT");
		material.setThickness(new BigDecimal(3.5));
		material.setWidth(new BigDecimal(100.5));
		material.setLength(new BigDecimal(2000.5));
		material.setTax(new BigDecimal(1.5));
		
		entity.setArrival(new Date());
		entity.setWeight(new BigDecimal(9999.09));
		entity.setMaterial(material);
		
		requestItemDao.update(entity);
		
		RequestItem find = requestItemDao.find(entity.getId());
		
		assertTrue(find.equals(entity));
		assertTrue(find.getMaterial().equals(entity.getMaterial()));
		
		List<RequestItem> list = requestItemDao.list(request.getId(), 0, 10);
		
		assertNotNull(list);
		assertNotEquals(0, list.size());
		
		Long id = entity.getId();
		
		requestItemDao.remove(entity);
		
		RequestItem exist = requestItemDao.find(id);
		
		assertNull(exist);
		
	}
	
	@Test
	@InSequence(40)
	public void RequestEndpointGetCurrentTest() throws Exception {

		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/current").build().toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.get(Request.class);

		assertEquals(200, response.getStatus());

		Request entity = response.getEntity();
		Assert.assertNotNull(entity);
		
	}
	
	// Test another way to get the current request, pass ZERO as id
	@Test
	@InSequence(41)
	public void RequestEndpointFindByIdZeroTest() throws Exception {
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(0).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.get(Request.class);

		assertEquals(200, response.getStatus());

		Request entity = response.getEntity();
		Assert.assertNotNull(entity);
	}
	
	@Test
	@InSequence(42)
	public void RequestEndpointFindByIdTest() throws Exception {
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(requestDao.current().getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.get(Request.class);

		assertEquals(200, response.getStatus());

		Request entity = response.getEntity();
		Assert.assertNotNull(entity);
	}
	
	@Test
	@InSequence(50)
	public void RequestEndpointUpdateTest() throws Exception {

		Request request = requestDao.current();
		
		assertNotNull(request);
		
		RequestItem item = new RequestItem();
		
		assertNotNull(item);
		
		Material material = new Material();
		
		material.setLamination("FQ");
		material.setTreatment("UZ");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(12.3));
		material.setTax(new BigDecimal(4.5));
		
		materialDao.create(material);
		
		item.setArrival(new Date());
		item.setWeight(new BigDecimal(8899.0));
		item.setMaterial(material);
		
		request.getItems().add(item);
		
		request.setCreated(new Date());
		request.setCreator("XXXX");
		request.setSent(new Date());
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);

		client.body(MediaType.APPLICATION_JSON, writer.writeValueAsString(request));
		
		ClientResponse<Request> response = client.put(Request.class);
		
		assertEquals(200, response.getStatus());
		
		Request entity = response.getEntity();

		assertNotNull(entity);
		
		// be sure that the fields deserialize are correct
		assertNotNull(entity.getId());
		assertEquals(entity.getCreated(), request.getCreated());
		assertEquals(entity.getCreator(), request.getCreator());

		for(RequestItem i : entity.getItems()) {
			assertTrue(entity.getItems().contains(i));
		}
	}
	
	@Test
	@InSequence(51)
	public void RequestEndpointListTest() throws Exception {
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.build().toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<List<Request>> response = client.get(new GenericType<List<Request>>(){});
		
		assertEquals(200, response.getStatus());
		
		List<Request> list = response.getEntity();
		
		Assert.assertNotNull(list);
		
	}

	@Test
	@InSequence(60)
	public void RequestItemEndpointCreateTest() throws Exception {
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item").build(1).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		Material material = new Material();
		
		material.setLamination("FQ");
		material.setTreatment("PR");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(12.3));
		material.setTax(new BigDecimal(4.5));
		
		RequestItem item = new RequestItem();
		
		item.setArrival(new Date());
		item.setWeight(new BigDecimal(1234.0));
		item.setMaterial(material);
		
		client.body(MediaType.APPLICATION_JSON, item);
		
		ClientResponse<RequestItem> response = client.post(RequestItem.class);
		
		assertEquals(201, response.getStatus());
		
		RequestItem entity = response.getEntity();
		
		// Be sure that the fields deserialize are correct
		assertNotNull(entity);
		assertNotNull(entity.getId());
		assertEquals(item.getArrival(), entity.getArrival());
		assertEquals(item.getWeight(), entity.getWeight());
		assertNotNull(entity.getMaterial());
		assertNotNull(entity.getMaterial().getId());
		assertEquals(entity.getMaterial().getLamination(), material.getLamination());
		assertEquals(entity.getMaterial().getTreatment(), material.getTreatment());
		assertEquals(entity.getMaterial().getThickness(), material.getThickness());
		assertEquals(entity.getMaterial().getWidth(), material.getWidth());
		assertEquals(entity.getMaterial().getLength(), material.getLength());
		assertEquals(entity.getMaterial().getTax(), material.getTax());
		
	}

	@Test
	@InSequence(61)
	public void RequestItemEndpointFindByIdTest() throws Exception {
		
		RequestItem item = requestItemDao.list(requestDao.current().getId(), 0, 10).get(0);
		
		assertNotNull(item);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item/{item}").build(requestDao.current().getId(), item.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<RequestItem> response = client.get(RequestItem.class);

		assertEquals(200, response.getStatus());

		RequestItem entity = response.getEntity();
		Assert.assertNotNull(entity);
		
	}
	
	@Test
	@InSequence(62)
	public void RequestItemEndpointUpdateTest() throws Exception {

		Request request = requestDao.current();
		
		assertNotNull(request);
		
		RequestItem item = requestItemDao.list(request.getId(), 0, 10).get(0);
		
		assertNotNull(item);
		
		Material material = new Material();
		
		material.setLamination("FQ");
		material.setTreatment("PZ");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(12.3));
		material.setTax(new BigDecimal(4.5));
		
		item.setArrival(new Date());
		item.setWeight(new BigDecimal(8899.0));
		item.setMaterial(material);

		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item/{item}").build(request.getId(), item.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());

		client.body(MediaType.APPLICATION_JSON, mapper.writeValueAsString(item));
		
		ClientResponse<RequestItem> response = client.put(RequestItem.class);
		
		assertEquals(200, response.getStatus());
		
		RequestItem entity = response.getEntity();

		assertNotNull(entity);
		
		// be sure that the fields deserialize are correct
		assertNotNull(entity.getId());
		assertEquals(item.getArrival(), entity.getArrival());
		assertEquals(item.getWeight(), entity.getWeight());
		assertNotNull(entity.getMaterial());
		assertNotNull(entity.getMaterial().getId());
		assertEquals(entity.getMaterial().getLamination(), material.getLamination());
		assertEquals(entity.getMaterial().getTreatment(), material.getTreatment());
		assertEquals(entity.getMaterial().getThickness(), material.getThickness());
		assertEquals(entity.getMaterial().getWidth(), material.getWidth());
		assertEquals(entity.getMaterial().getLength(), material.getLength());
		assertEquals(entity.getMaterial().getTax(), material.getTax());
	}
	
	@Test
	@InSequence(63)
	public void RequestItemEndpointListTest() throws Exception {
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		Request request = requestDao.current();
		
		if (request == null) {
			request = new Request();
			
			request.setCreated(new Date());
			request.setCreator("ItemEndpointListTest");
			request.setSent(null);
			
			requestDao.create(request);
		}
		
		assertNotNull(request.getId());
		
		Material material = new Material();
		
		material.setLamination("QQ");
		material.setTreatment("PQ");
		material.setThickness(new BigDecimal(2.0));
		material.setWidth(new BigDecimal(200.0));
		material.setLength(new BigDecimal(0));
		material.setTax(new BigDecimal(4.5));
		
		RequestItem entity = new RequestItem();
		
		entity.setRequest(request);
		entity.setMaterial(material);
		entity.setArrival(new Date());
		entity.setWeight(new BigDecimal(1234.0));

		requestItemDao.create(entity);

		ClientRequest client = new ClientRequest(context.path("/{id}/item").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);
		
		ClientResponse<List<RequestItem>> response = client.get(new GenericType<List<RequestItem>>(){});
		
		assertEquals(200, response.getStatus());

		List<RequestItem> list = response.getEntity();
		
		Assert.assertNotNull(list);
		
		assertFalse(list.isEmpty());
		
	}
	
	@Test
	@InSequence(64)
	public void RequestItemEndpointDeleteByIdTest() throws Exception {
		
		RequestItem item = requestItemDao.list(requestDao.current().getId(), 0, 10).get(0);
		
		assertNotNull(item);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}/item/{item}").build(requestDao.current().getId(), item.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<RequestItem> response = client.delete(RequestItem.class);

		assertEquals(204, response.getStatus());

		RequestItem entity = requestItemDao.find(item.getId());
		assertNull(entity);
		
	}
	
	@Test
	@InSequence(70)
	public void RequestEndpointDeleteByIdTest() throws Exception {
		
		Request request = requestDao.current();
		
		assertNotNull(request);
		
		UriBuilder context = UriBuilder.fromUri(url);
		
		ClientRequest client = new ClientRequest(context.path("/{id}").build(request.getId()).toString());
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Content-Type", MediaType.APPLICATION_JSON);

		ClientResponse<Request> response = client.delete(Request.class);

		assertEquals(204, response.getStatus());

		Request entity = requestDao.find(request.getId());
		assertNull(entity);
		
	}	
}
