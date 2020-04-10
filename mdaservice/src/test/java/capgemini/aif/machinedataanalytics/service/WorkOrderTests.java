package capgemini.aif.machinedataanalytics.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkOrderTests {

	private final Logger log = LoggerFactory.getLogger(WorkOrderTests.class);
	private long millis;
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}
	private String formatted_timestamp;
	
	@Autowired
	WorkOrderRepository woRepo;

	@Before
	public void before() throws Exception {
		setMillis();
		log.debug("\n\t"+"Test Run: "+millis+this.getClass().getSimpleName());
	}
	@After
	public void after() throws Exception {}
	
	@Test
	public void testInit() throws Exception {
		WorkOrder wo2 = woRepo.findByWorkorderidentifier("UT-WO-2");
		assertNotNull("UT-WO-2 doesn't exist", wo2);
		assertEquals("UT-WO-2 should be at ndx 2",2,wo2.getId().intValue());
		
		WorkOrder wo3 = woRepo.findByWorkorderidentifier("UT-WO-3");
		assertNotNull("UT-WO-3 doesn't exist", wo3);
		assertEquals("UT-WO-3 should be at ndx 3",3,wo3.getId().intValue());
		
		WorkOrder wo4 = woRepo.findByWorkorderidentifier("UT-WO-4");
		assertNotNull("UT-WO-4 doesn't exist", wo4);
		assertEquals("UT-WO-4 should be at ndx 4",4,wo4.getId().intValue());
		
		WorkOrder wo5 = woRepo.findByWorkorderidentifier("UT-WO-5");
		assertNotNull("UT-WO-5 doesn't exist", wo5);
		assertEquals("UT-WO-5 should be at ndx 5",5,wo5.getId().intValue());
	}


}
