package br.com.altamira.bpm.purchase.request.listener;

import java.util.logging.Logger;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

@Named("MergeRequest")
public class MergeRequestListener implements ExecutionListener  {

	private final static Logger LOGGER = Logger.getLogger(MergeRequestListener.class.getName());

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LOGGER.info("A NEW REQUEST WAS MERGED TO CURRENT QUOTATION. Request ID: " + execution.getVariable("REQUEST_ID"));
	}

}
