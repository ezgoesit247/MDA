package capgemini.aif.machinedataanalytics.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkOrderTests {

	private final Logger log = LoggerFactory.getLogger(WorkOrderTests.class);
	private long millis;
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}
	
	@Autowired
	WorkOrderRepository wo_r;

	@Before
	public void before() throws Exception {
		setMillis();
		log.debug("\n\t"+"Test Run: "+millis+this.getClass().getSimpleName());
	}
	@After
	public void after() throws Exception {}

	
	@Test
	public void testCreate() throws Exception {
		String identifier1 = "UT-WO-1"+millis;
		String identifier2 = "UT-WO-2"+millis;
		String identifier3 = "UT-WO-3"+millis;
		String identifier4 = "UT-WO-4"+millis;
		String identifier5 = "UT-WO-5"+millis;
		assertNotNull(wo_r.save(
				new WorkOrder(identifier1,new Timestamp(new Date().getTime()))));
		assertNotNull(wo_r.save(
				new WorkOrder(identifier2,new Timestamp(new Date().getTime()))));
		assertNotNull(wo_r.save(
				new WorkOrder(identifier3,new Timestamp(new Date().getTime()))));
		assertNotNull(wo_r.save(
				new WorkOrder(identifier4,new Timestamp(new Date().getTime()))));
		assertNotNull(wo_r.save(
				new WorkOrder(identifier5,new Timestamp(new Date().getTime()))));

	}
	
	@Test
	public void testGet() throws Exception {
		testCreate();

		String identifier1 = "UT-WO-1"+millis;
		String identifier2 = "UT-WO-2"+millis;
		String identifier3 = "UT-WO-3"+millis;
		String identifier4 = "UT-WO-4"+millis;
		String identifier5 = "UT-WO-5"+millis;
		
		WorkOrder wo1 = wo_r.findByWorkorderidentifier(identifier1);
		assertNotNull("UT-WO-1 doesn't exist", wo1);
		assertEquals("UT-WO-1 not found by work order identifier",identifier1,wo1.getWorkorderidentifier());
		
		WorkOrder wo2 = wo_r.findByWorkorderidentifier(identifier2);
		assertNotNull("UT-WO-2 doesn't exist", wo2);
		assertEquals("UT-WO-2 not found by work order identifier",identifier2,wo2.getWorkorderidentifier());
		
		WorkOrder wo3 = wo_r.findByWorkorderidentifier(identifier3);
		assertNotNull("UT-WO-3 doesn't exist", wo3);
		assertEquals("UT-WO-3 not found by work order identifier",identifier1,wo1.getWorkorderidentifier());
		
		WorkOrder wo4 = wo_r.findByWorkorderidentifier(identifier4);
		assertNotNull("UT-WO-4 doesn't exist", wo4);
		assertEquals("UT-WO-4 not found by work order identifier",identifier4,wo4.getWorkorderidentifier());
		
		WorkOrder wo5 = wo_r.findByWorkorderidentifier(identifier5);
		assertNotNull("UT-WO-5 doesn't exist", wo5);
		assertEquals("UT-WO-5 not found by work order identifier",identifier5,wo5.getWorkorderidentifier());
	}


}
