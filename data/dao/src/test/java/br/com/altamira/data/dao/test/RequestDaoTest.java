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
package br.com.altamira.data.dao.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;

import br.com.altamira.data.dao.RequestDao;
import br.com.altamira.data.model.Material;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.model.RequestItem;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RequestDaoTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "RequestDaoTest.war")
                .addClasses(Request.class, RequestItem.class, Material.class)
                .addClasses(RequestDao.class)
                //.addClasses(Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
                //.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
                // Deploy our test datasource
                //.addAsWebInfResource("test-ds.xml");
    }

    @Inject
    RequestDao requestDao;
    
    Request entity;

//    @Inject
//    Logger log;

    @Before
    public void before() {
    	entity = new Request();
    	entity.setCreated(new Date());
    	entity.setCreator("Jane Doe");
    	entity.setSent(null);
    }
    
    @Test
    @InSequence(1)
    public void createTest() throws Exception {

        requestDao.create(entity);
        
        assertNotNull(entity.getId());
        assertNotEquals(0l, entity.getId().longValue());
//        log.info(entity.getCreator() + " was persisted with id " + entity.getId());
    }

    @Test
    @InSequence(2)
    public void listTest() throws Exception {
    	List<Request> list = requestDao.getAll(0, 1);
    	
    	assertNotNull(list);
    	assertFalse(list.isEmpty());
    	assertEquals(1, list.size());
    	assertEquals(entity.getCreator(), list.get(0).getCreator());
    }
    
    @Test
    @InSequence(3)
    public void findTest() throws Exception {
    	
    	Request find = requestDao.find(entity.getId());
    	
    	assertNotNull(find);
    	assertNotNull(find.getId());
    	assertEquals(entity.getId(), find.getId());
    }
    
    @Test
    @InSequence(4)
    public void updateTest() throws Exception {
    	
    	entity.setCreated(new Date());
    	entity.setCreator("John Doe");
    	entity.setSent(new Date());
        
    	requestDao.update(entity);
    	
    	Request find = requestDao.find(entity.getId());
    	
    	assertNotNull(find);
    	assertNotNull(find.getId());
    	assertEquals(entity.getId(), find.getId());
    	assertEquals(entity.getCreator(), find.getCreator());
    }
    
    @Test
    @InSequence(5)
    public void deleteTest() throws Exception {
    	
    	requestDao.remove(entity.getId());
    	
    	assertNull(entity.getId());
    	
    }
}
