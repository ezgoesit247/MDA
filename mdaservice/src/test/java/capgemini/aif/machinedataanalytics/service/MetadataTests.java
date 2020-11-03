package capgemini.aif.machinedataanalytics.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import capgemini.aif.machinedataanalytics.service.Metadata.VariableType;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MetadataTests {

	private long millis;
	private void setMillis() {
		millis = System.currentTimeMillis();
	}
	@SuppressWarnings("unused")
	private	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("unused")
	private final LocalDateTime now = LocalDateTime.now();
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(MetadataTests.class);

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
		setMillis();
	}
	@After
	public void after() throws Exception {

	}
	


	
	@Test
	public void testCreate() throws Exception {
		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		String metaidentifier5 = "METAVAR-5"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp", "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss", "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp", "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension", "Machine4","EQUIP4")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier5,VariableType.SPEED,"oven_load", "Machine5","EQUIP5")));

	}
	

}
