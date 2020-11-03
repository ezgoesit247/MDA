package capgemini.aif.machinedataanalytics.service;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestingTests {

	@SuppressWarnings("unused")
	private long millis;
	private void setMillis() {
		millis = System.currentTimeMillis();
	}
	@SuppressWarnings("unused")
	private	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("unused")
	private final LocalDateTime now = LocalDateTime.now();
	private final Logger log = LoggerFactory.getLogger(TestingTests.class);

	Set<CrudRepository<? extends DataObject,? extends Number>> s =
			new HashSet<CrudRepository<? extends DataObject,? extends Number>>();

	@SuppressWarnings("unused")
	@Autowired
	private FFTDetailsRepository fft_r;

	@SuppressWarnings("unused")
	@Autowired
	private MetadataRepository metadata_r;
	
	@SuppressWarnings("unused")
	@Autowired
	private ReelRepository reel_r;

	@SuppressWarnings("unused")
	@Autowired
	private TelemetryRepository tele_r;

	@SuppressWarnings("unused")
	@Autowired
	private TelemetryValueRepository tele_v_r;
	
	@SuppressWarnings("unused")
	@Autowired
	private WorkOrderRepository wo_r;
	
	@BeforeClass
	public static void setup() throws Exception {
		LoggerFactory.getLogger(TestingTests.class).info("setup");
	}
	@AfterClass
	public static void teardown() throws Exception {
		LoggerFactory.getLogger(TestingTests.class).info("teardown");
	}
	
	@Before
	public void before() throws Exception {
//		log.info("@Before Test");
		setMillis();
		
		// THIS DIDN'T WORK THE WAY I INTEDED...
//		for(CrudRepository<? extends DataObject,? extends Number> repo : s) {
//			if(repo.count() > 0)
//				log.info("Repository not empty");
//		}
	}
	@After
	public void after() throws Exception {
//		log.info("@After Test");
		// THIS DIDN'T WORK THE WAY I INTEDED...
//		for(CrudRepository<? extends DataObject,? extends Number> repo : s) {
//			repo.deleteAll();
//			if(repo.count() > 0)
//				log.info("Repository not empty");
//		}

	}
	
	@SuppressWarnings("unused")
	private void clearAllObjects() {
		for(CrudRepository<? extends DataObject,? extends Number> repo : s) {
		repo.deleteAll();
		if(repo.count() > 0)
			log.info("Repository not empty");
		}
	}
	
	@Test
	public void testBeforeAndAfter() throws Exception {
		assertTrue(true);
	}

}
