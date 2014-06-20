package br.com.altamira.data.model.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.data.model.Request;

@RunWith(Arquillian.class)
public class RequestTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "RequestTest.war")
            .addPackage(Request.class.getPackage())
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    UserTransaction utx;
    
    Request request;

    @Before
    public void preparePersistenceTest() throws Exception {
    	clearData();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        em.createQuery("delete from Request").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
    	utx.begin();
        em.joinTransaction();
        request = new Request(null, new Date(), "John Doe");
        em.persist(request);
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }
    
    @Test
    public void createTest() throws Exception {
    	System.out.println("********* Start Request Insert Test ***************");
    	Request entity = new Request(null, new Date(), "Jane Doe");
    	
    	utx.begin();
        em.joinTransaction();
    	em.persist(entity);
    	utx.commit();
    	
    	assertNotNull(entity);
    	assertNotNull(entity.getId());
    	assertNotEquals(0l, entity.getId().longValue());
    	System.out.println("********* End Request Insert Test ***************");
    }

    @Test
    public void findTest() throws Exception {
    	insertData();
    	
    	System.out.println("********* Start Request Find Test ***************");
    	
    	Request entity = em.find(Request.class, request.getId());
    	
    	assertNotNull(entity);
    	assertNotNull(entity.getId());
    	assertEquals(request.getId(), entity.getId());
    	System.out.println("********* End Request Find Test ***************");
    }

    @Test
    public void updateTest() throws Exception {
    	
    	insertData();
    	
    	System.out.println("********* Start Request Update Test ***************");
    	
    	Request entity = em.find(Request.class, request.getId());
    	
    	Timestamp dt = new Timestamp(new Date().getTime());
    	
    	entity.setCreated(dt);
    	entity.setCreator("Jane Doe");
    	entity.setSent(dt);
    	
    	utx.begin();
        em.joinTransaction();
    	em.merge(entity);
    	utx.commit();
    	
    	Request expected = em.find(Request.class, entity.getId());
    	
    	assertNotNull(entity);
    	assertNotNull(entity.getId());
    	assertEquals(expected.getId(), entity.getId());
    	assertEquals(expected.getCreated(), dt);
    	assertEquals(expected.getCreator(), entity.getCreator());
    	assertEquals(expected.getSent(), dt);
    	System.out.println("********* End Request Update Test ***************");
    }
    
    @Test
    public void deleteTest() throws Exception {
    	
    	insertData();
    	
    	System.out.println("********* Start Request Delete Test ***************");
    	
    	Request entity = em.find(Request.class, request.getId());
    	
    	assertNotNull(entity);
    	assertNotNull(entity.getId());
    	
    	utx.begin();
        em.joinTransaction();
    	em.remove(em.contains(entity) ? entity : em.merge(entity));
    	utx.commit();
    	
    	Request find = em.find(Request.class, request.getId());
    	
    	assertNull(find);
    	
    	System.out.println("********* End Request Delete Test ***************");
    }
    
    @After
    public void commitTransaction() throws Exception {
        //clearData();
    }
}
