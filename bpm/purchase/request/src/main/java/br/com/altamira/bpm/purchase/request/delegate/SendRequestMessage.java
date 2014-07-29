package br.com.altamira.bpm.purchase.request.delegate;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;

@Named("SendRequest")
public class SendRequestMessage implements JavaDelegate {
	
	@Inject
	private RuntimeService runtimeService;
	
	private final static Logger LOGGER = Logger.getLogger(SendRequestMessage.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		HashMap<String, Object> correlationKeys = new HashMap<String, Object>();
		
		correlationKeys.put("REQUEST_ID", execution.getVariable("REQUEST_ID"));

		Execution orderProcessInstance = runtimeService.createExecutionQuery()
				.messageEventSubscriptionName("REQUEST_INCLUDE_MESSAGE")
				.singleResult();
		
		if (orderProcessInstance == null) {
			
			if (runtimeService.createEventSubscriptionQuery().eventName("REQUEST_CREATED_MESSAGE").count() > 0) {
				runtimeService.startProcessInstanceByMessage("REQUEST_CREATED_MESSAGE", correlationKeys);
				
				LOGGER.log(Level.INFO, "A REQUEST_CREATED_MESSAGE was sent.");
			} else {
				LOGGER.log(Level.INFO, "No process was waiting for a REQUEST_CREATED_MESSAGE message.");
			}
			
		} else {
			
			//runtimeService.correlateMessage("REQUEST_INCLUDE_MESSAGE", correlationKeys);
			//runtimeService.createMessageCorrelation("REQUEST_INCLUDE_MESSAGE").processInstanceId(orderProcessInstance.getId()).correlate();
			/*Execution exec = runtimeService.createExecutionQuery()
				      .messageEventSubscriptionName("REQUEST_INCLUDE_MESSAGE")
				      .singleResult();*/
				 
		    // 1. case: message received before completing the task
		 
		    runtimeService.messageEventReceived("REQUEST_INCLUDE_MESSAGE", orderProcessInstance.getId(), correlationKeys);
				    
			LOGGER.log(Level.INFO, "A REQUEST_INCLUDE_MESSAGE was sent.");
		}
		
	}

}
