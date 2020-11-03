package capgemini.aif.machinedataanalytics.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReelTests {

	private long millis;
	private void setMillis() {
		millis = System.currentTimeMillis();
	}

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(ReelTests.class);
	private	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private LocalDateTime now = LocalDateTime.now();

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
	
	@Before
	public void before() throws Exception {
		setMillis();
	}
	@After
	public void after() throws Exception {}
	
	@Test
	public void testCreateNew() throws Exception {
		Reel reel = new Reel("ext-1"+millis, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder("WO"+millis, Timestamp.valueOf(now.format(formatter)))));
		reel_r.save(reel);
//		reel_r.delete(reel);
		@SuppressWarnings("unused")
		WorkOrder wo = wo_r.findByWorkorderidentifier("WO"+now.toString());
//		wo_r.delete(wo);
	}

	@Test
	public void testFindByReelidentifier() throws Exception {
		String id = "UT-EXT-REEL-3";
		Reel r = reel_r.save(new Reel(id, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder("WO"+now.toString(), Timestamp.valueOf(now.format(formatter))))));
		assertEquals("bad save on reel "+id,id, r.getReelidentifier());

		
		Reel r3 = reel_r.findByReelidentifier(id);
		assertNotNull("Reel is null",r3);
		assertEquals(ReelType.EXTRUDER, r3.getType());
		r = null;

		id = "UT-TPR-REEL-4";
		r = reel_r.save(new Reel(id, ReelType.TAPER, wo_r.findByWorkorderidentifier("WO"+now.toString())));
		assertEquals("bad save on reel "+id,id, r.getReelidentifier());
		Reel r4 = reel_r.findByReelidentifier(id);
		assertNotNull("Reel is null",r4);
		assertEquals(ReelType.TAPER, r4.getType());
	}
	
	@Test
	public void testFind() throws Exception {
		testCreateReel();
		String reelidentifier1 = "ext-reel-1"+millis;
		String workorderidentifier1 = "UT-WO-1"+millis;
		
		Reel reel1 = reel_r.findByWorkorder(wo_r.findByWorkorderidentifier(workorderidentifier1));
		assertNotNull("ext-reel-1 is null",reel1);
		assertEquals("ext-reel-1 not found by work order",reelidentifier1,reel1.getReelidentifier());
		reel1 = null;
		
		reel1 = reel_r.findByReelidentifier(reelidentifier1);
		assertNotNull("ext-reel-1 is null",reel1);
		assertEquals("ext-reel-1 not found reel identifier",reelidentifier1,reel1.getReelidentifier());
	}

	@Test
	public void testFindAllByTypeExt() throws Exception{
		List<Reel> reels = new ArrayList<Reel>(reel_r.findAllByType(ReelType.EXTRUDER));
		assertFalse(reels.isEmpty());
	}

	@Test
	public void testFindAllByTypeTpr() throws Exception {
		List<Reel> reels = new ArrayList<Reel>(reel_r.findAllByType(ReelType.TAPER));
		assertFalse(reels.isEmpty());
	}


	
	@Test
	public void testCreateReel() throws Exception {
		String reelidentifier1 = "ext-reel-1"+millis;
		String workorderidentifier1 = "UT-WO-1"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier1, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier1, Timestamp.valueOf(now.format(formatter)))))));
	}
	 
}
