package br.com.altamira.bpm.purchase.request.steel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.altamira.data.model.Request;

@Named
@SessionScoped
public class RequestController implements Serializable {

	private static final long serialVersionUID = 2460376329687679210L;

	@Inject
	private RequestService requestService;

	@Inject
	@Named("currentRequest")
	private Request request;

	public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void Submit() {
		requestService.startProcess(request);
	}
	
	public Map<String, String> getHtml5Attributes() {
		return html5Attributes;
	}

	public List<Request> getAll() {
		return requestService.list();
	}
	
	private Map<String, String> html5Attributes = new HashMap<String, String>();
	
	public RequestController() {
	   html5Attributes.put("autofocus", "true");
	   html5Attributes.put("placeholder", "Enter text");
	}
	

}
