package br.com.altamira.bpm.purchase.request.steel;

import java.util.logging.Logger;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * This is an empty service implementation illustrating how to use a plain Java 
 * class as a BPMN 2.0 Service Task delegate.
 */
@Named("Logger")
public class LoggerDelegate implements JavaDelegate {
 
  private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
  
  public void execute(DelegateExecution execution) throws Exception {
    
    LOGGER.info("\n\nLOGGER: "
            + "\n		processDefinitionId = " + execution.getProcessDefinitionId()
            + "\n		activtyId = " + execution.getCurrentActivityId()
            + "\n		activtyName = '" + execution.getCurrentActivityName() + "'"
            + "\n		processInstanceId = " + execution.getProcessInstanceId()
            + "\n		businessKey = " + execution.getProcessBusinessKey()
            + "\n		executionId = " + execution.getId()
            + " \n\n");
    
  }

}
