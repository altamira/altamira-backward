package br.com.altamira.bpm.purchase.request.steel;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.soap.AddressingFeature.Responses;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.cdi.ProcessVariables;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.model.Request;

@RunWith(Arquillian.class)
public class ArquillianTest {
  
  private static final String PROCESS_DEFINITION_KEY = "bpm.purchase.request.steel";

  @Deployment
  public static WebArchive createDeployment() {
    // resolve given dependencies from Maven POM
    File[] libs = Maven.resolver()
      .offline(false)
      .loadPomFromFile("pom.xml")
      .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile();

    return ShrinkWrap
            .create(WebArchive.class, "SteelTest.war")
            // add needed dependencies
            .addAsLibraries(libs)
            // prepare as process application archive for camunda BPM Platform
            .addAsWebResource("META-INF/test-processes.xml", "WEB-INF/classes/META-INF/processes.xml")
            // enable CDI
            .addAsWebResource("WEB-INF/beans.xml", "WEB-INF/beans.xml")
            // boot JPA persistence unit
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            // add your own classes (could be done one by one as well)
            .addPackages(false, "br.com.altamira.bpm.purchase.request.steel") // not recursive to skip package 'nonarquillian'
            .addPackages(true, "br.com.altamira.data")
            .addPackage("br.com.altamira.data.model")
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
    
    ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

    //assertEquals(1, processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).finished().count());
    assertEquals(1, processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId()).count());
  }

  /**
   * Helper to delete all running process instances, which might disturb our Arquillian Test case
   * Better run test cases in a clean environment, but this is pretty handy for demo purposes
   */
  private void cleanUpRunningProcessInstances() {
    List<ProcessInstance> runningInstances = processEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionKey(PROCESS_DEFINITION_KEY).list();
    for (ProcessInstance processInstance : runningInstances) {
      processEngine.getRuntimeService().deleteProcessInstance(processInstance.getId(), "deleted to have a clean environment for Arquillian");
    }
  }  
  
  @Test
  public void GetCurrentTest() throws Exception {
	  ClientRequest request = new ClientRequest("http://localhost:8080/bpm-purchase-request-steel/rest/request/current");
	  request.accept(MediaType.APPLICATION_JSON);
      request.header("Content-Type", MediaType.APPLICATION_JSON);
	  ClientResponse<Request> response = request.get(Request.class);
	  
	  assertEquals(200, response.getStatus());
	  
	  Request r = response.getEntity();
	  Assert.assertNotNull(r);
  }
  
}
