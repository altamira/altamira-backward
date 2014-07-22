package br.com.altamira.bpm.purchase.quotation;

import java.util.HashMap;
import org.camunda.bpm.engine.RuntimeService;
import br.com.altamira.data.model.Request;

import javax.inject.Inject;


public class RequestService {
	
	//private final static Logger LOGGER = Logger.getLogger(RequestService.class.getName());
	
	public static final String PROCESS_ID = "br.com.altamira.bpm.purchase.request.steel";

//	@Inject
//	private RequestDao requestDao;
	
	@Inject
	private RuntimeService runtimeService;

//	@Inject
//	private ProcessEngine processEngine;

	public void startProcess(Request request) {

	    HashMap<String, Object> variables = new HashMap<String, Object>();
	    variables.put("REQUEST_ID", request.getId());

	    runtimeService.startProcessInstanceByKey("br.com.altamira.bpm.purchase.request.steel", variables);
	    //return request.getId();
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
