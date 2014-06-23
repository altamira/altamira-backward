package test;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.pack.MyCase;

@RunWith(Arquillian.class)
public class MyTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "mytestcase.war")
				.addPackage(MyCase.class.getPackage())
				//.addAsResource("META-INF/persistence.xml")
				//.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				//.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
		
	}
	
//	@Inject
//	Logger log;
	
	@Inject 
	MyCase myCase;
	
	@Test
	public void doTest() {
//		log.info("***************** START TEST **********************");

		myCase.setA(5);
		myCase.setB(3);
		int result = myCase.sum();
		
		assertEquals(8, result);
		
//		log.info("***************** END TEST ***********************");
	}
}
