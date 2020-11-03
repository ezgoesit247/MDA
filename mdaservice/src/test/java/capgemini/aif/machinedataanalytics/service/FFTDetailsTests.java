package capgemini.aif.machinedataanalytics.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class FFTDetailsTests {


	private final Logger log = LoggerFactory.getLogger(FFTDetailsTests.class);
	private long millis;
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}
//	private String formatted_timestamp;

	@Before
	public void before() throws Exception {
		setMillis();
		log.debug("\n\t"+"Test Run: "+millis+this.getClass().getSimpleName());
	}
	@After
	public void after() throws Exception {}
	
	@Test
	public void placeholder() throws Exception {
		assertTrue(true);
	}

/*
 * 
ccoldamplitude : Integer
ccoldfrequency : Long
d1amplitude : Integer
d1frequency : Long
d3amplitude : Integer
d3frequency : Long
endtime : Timestamp
starttime : Timestamp
timestamp : Timestamp
workorder : WorkOrder
 */

}
