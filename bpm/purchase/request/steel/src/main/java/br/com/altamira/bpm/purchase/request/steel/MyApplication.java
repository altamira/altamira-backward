package br.com.altamira.bpm.purchase.request.steel;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.camunda.bpm.engine.rest.impl.CamundaRestResources;

@ApplicationPath("/")
public class MyApplication extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    // add your own classes 

    // add all camunda engine rest resources (Or just add those that you actually need).
    classes.addAll(CamundaRestResources.getResourceClasses());

    // mandatory
    classes.addAll(CamundaRestResources.getConfigurationClasses());

    return classes;
  }
}