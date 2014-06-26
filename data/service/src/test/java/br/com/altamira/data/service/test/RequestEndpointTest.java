/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.altamira.data.service.test;

import java.io.File;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.util.Resources;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RequestEndpointTest {
	
	  @Deployment
	  public static WebArchive createDeployment() {
	    // resolve given dependencies from Maven POM
	    File[] libs = Maven.resolver()
	      .offline(false)
	      .loadPomFromFile("pom.xml")
	      .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile();

	    return ShrinkWrap
	            .create(WebArchive.class, "steel.war")
	            // add needed dependencies
	            .addAsLibraries(libs)
	            // prepare as process application archive for camunda BPM Platform
	            .addAsWebResource("META-INF/processes.xml", "WEB-INF/classes/META-INF/processes.xml")
	            // enable CDI
	            .addAsWebResource("WEB-INF/beans.xml", "WEB-INF/beans.xml")
	            // boot JPA persistence unit
	            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
	            // add your own classes (could be done one by one as well)
	            .addPackages(false, "br.com.altamira.bpm.purchase.request.steel") // not recursive to skip package 'nonarquillian'
	            // add process definition
	            .addAsResource("process.bpmn")
	            // add process image for visualizations
	            .addAsResource("process.png")
	            // now you can add additional stuff required for your test case
	    ;
	  }

    @Inject
    RequestDao requestDao;

    @Inject
    Logger log;

    @Test
    public void testRegister() throws Exception {
        /*Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getName() + " was persisted with id " + newMember.getId());*/
    }

}
