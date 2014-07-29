package br.com.altamira.bpm.purchase.request.delegate;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.template.ClasspathTemplateLoader;
import de.neuland.jade4j.template.JadeTemplate;
import de.neuland.jade4j.template.TemplateLoader;

import javax.annotation.Resource;

@Named("NotifyOrderConfirmationPending")
public class NotifyOrderConfirmationPending implements JavaDelegate {
	
	private static final String RESOURCE_NAME = "java:jboss/mail/Default";
	
	@Resource(mappedName = RESOURCE_NAME)
	private Session mailSession;
	
	private static final String FROM_USER = "Sistema Altamira <sistema@altamira.com.br>";
	
	private final static Logger LOGGER = Logger.getLogger(NotifyOrderConfirmationPending.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		//String assignee = "esli.gomes";
		String assignee = "demo";

		if (mailSession == null) {
			LOGGER.warning("Resource injection fail '" + RESOURCE_NAME + "', do it manually by context.lookup.");
			try {
				InitialContext ctx;
				ctx = new InitialContext();
				mailSession = (Session) ctx.lookup(RESOURCE_NAME);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (assignee != null) {

			// Get User Profile from User Management
			IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
			User user = identityService.createUserQuery().userId(assignee).singleResult();

			if (user != null) {

				// Get Email Address from User Profile
				String recipient = user.getEmail();

				if (recipient != null && !recipient.isEmpty()) {
					
		            try    {
		                MimeMessage m = new MimeMessage(mailSession);
		                Address from = new InternetAddress(FROM_USER);
		                Address[] to = new InternetAddress[] {new InternetAddress(recipient) };

		                m.setFrom(from);
		                m.setRecipients(Message.RecipientType.TO, to);
		                m.setSubject("Pedido de Compra " + execution.getVariable("orderId") + " Pendente de Confirmação");
		                m.setSentDate(new java.util.Date());
		                
		                Map<String, Object> processVariables = execution.getVariables();
		                processVariables.put("task", execution);
		                
		                JadeConfiguration config = new JadeConfiguration();
		                config.setPrettyPrint(true);
		                
		                TemplateLoader loader = new ClasspathTemplateLoader();
		                config.setTemplateLoader(loader);
		                
		                JadeTemplate template = config.getTemplate("NotifyConfirmationPending");

		                String html = config.renderTemplate(template, processVariables);
		                
		                //String html = Jade4J.render("./NotifyTaskAssigned.jade", processVariables);
		                
		                m.setContent(html, "text/html;charset=utf-8");
		                /*m.setContent("<html><head></head><body><h1>" + delegateTask.getName() + "</h1><p>Uma nova tarefa foi atribuida a você.<br><br>Para executa-la <a href=\"http://localhost:8080/camunda/app/tasklist/default/#/task/"
								+ taskId + "\">clique aqui</a>.</p></body></html>", "text/html;charset=utf-8");*/

		                Transport.send(m);
		                
		                LOGGER.info("A Order Confirmation Pending Mail Notification was sent for Order '" + execution.getVariable("orderId") + "' was successfully sent to user '"
								+ assignee
								+ "' with address '"
								+ recipient
								+ "'.");
		            }
		            catch (javax.mail.MessagingException e)
		            {
		                LOGGER.warning("Error in Sending Mail: " + e.getMessage());
		                e.printStackTrace();
		            } catch (JadeCompilerException e) {
		            	LOGGER.warning("Error in Sending Mail: " + e.getMessage());
		            	e.printStackTrace();
					} catch (IOException e) {
						LOGGER.warning("IO Exception: " + e.getMessage());
						e.printStackTrace();
					}					

				} else {
					LOGGER.warning("Not sending email to user " + assignee
							+ "', user has no email address.");
				}

			} else {
				LOGGER.warning("Not sending email to user " + assignee
						+ "', user is not enrolled with identity service.");
			}

		}

	}
		
}
