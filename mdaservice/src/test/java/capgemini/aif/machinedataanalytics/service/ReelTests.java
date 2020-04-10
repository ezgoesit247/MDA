package capgemini.aif.machinedataanalytics.service;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReelTests {

	private final Logger log = LoggerFactory.getLogger(ReelTests.class);
	private	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private LocalDateTime now = LocalDateTime.now();

	@Autowired
	private ReelRepository reel_r;

	@Autowired
	private WorkOrderRepository wo_r;
	
	@Test
	public void testCreateNew() {
		Reel reel = new Reel("ext-1", ReelType.EXTRUDER, wo_r.save(new WorkOrder("WO"+now.toString(), Timestamp.valueOf(now.format(formatter)))));
		reel_r.save(reel);
		reel_r.delete(reel);
		WorkOrder wo = wo_r.findByWorkorderidentifier("WO"+now.toString());
		wo_r.delete(wo);
	}

	@Test
	public void testFindByReelidentifier() {
		Reel r1 = reel_r.findByReelidentifier("UT-EXT-REEL-3");
		assertNotNull("Reel is null",r1);
		assertEquals(ReelType.EXTRUDER, r1.getType());

		Reel r3 = reel_r.findByReelidentifier("UT-TPR-REEL-4");
		assertNotNull("Reel is null",r3);
		assertEquals(ReelType.TAPER, r3.getType());
	}
	
	@Test
	public void testFindByWorkorder() {
		assertTrue(true);
		//TODO: Code this method
	}

	@Test
	public void testFindAllByTypeExt() {
		List<Reel> reels = new ArrayList<Reel>(reel_r.findAllByType(ReelType.EXTRUDER));
		assertFalse(reels.isEmpty());
	}

	@Test
	public void testFindAllByTypeTpr() {
		List<Reel> reels = new ArrayList<Reel>(reel_r.findAllByType(ReelType.TAPER));
		assertThat(reels, isA(List.class));
		assertFalse(reels.isEmpty());
	}

	 
	 
}
