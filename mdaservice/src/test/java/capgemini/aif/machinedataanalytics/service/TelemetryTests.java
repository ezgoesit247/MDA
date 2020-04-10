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

import capgemini.aif.machinedataanalytics.service.Metadata;
import capgemini.aif.machinedataanalytics.service.MetadataRepository;
import capgemini.aif.machinedataanalytics.service.Reel;
import capgemini.aif.machinedataanalytics.service.ReelRepository;
import capgemini.aif.machinedataanalytics.service.Telemetry;
import capgemini.aif.machinedataanalytics.service.Telemetry.SendStatus;
import capgemini.aif.machinedataanalytics.service.TelemetryRepository;
import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TelemetryTests {

	private final Logger log = LoggerFactory.getLogger(TelemetryTests.class);
	private	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private LocalDateTime now = LocalDateTime.now();

	@Autowired
	private TelemetryRepository tele_r;
	
	@Autowired
	private ReelRepository reel_r;

	@Autowired
	private MetadataRepository metadata_r;
	
	@Autowired
	WorkOrderRepository wo_r;

	@Before
	public void before() throws Exception {}
	@After
	public void after() throws Exception {}

	@Test
	public void test_findByReelidentifierAndTimestamp() throws Exception {
		String reelidentifier = "UT-REEL-1";
		Reel r = reel_r.findByReelidentifier(reelidentifier);
		assertNotNull("Reel is null", r);

		Metadata  m1 = metadata_r.findByVariablename("UT-Variable1");
		 Metadata  m2 = metadata_r.findByVariablename("UT-Variable2");
		 Metadata  m3 = metadata_r.findByVariablename("UT-Variable3");
		 
		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(m1, 3.123457d));
		variables.add(new TelemetryValue(m2, 10.33345d));
		variables.add(new TelemetryValue(m3, 12.6645d));
		Telemetry t = new Telemetry(variables,r,Timestamp.valueOf(now.format(formatter)),SendStatus.NOSEND);

		tele_r.save(t);

		Telemetry telemetry = tele_r.findByReelidentifierAndTimestamp(reelidentifier,Timestamp.valueOf(now.format(formatter)));
		assertNotNull("Telemetry is null", telemetry);
		Set<TelemetryValue> vars = telemetry.getVariables();
		assertEquals(3, vars.size());
		
		
		tele_r.delete(t);
	}
	
	@Test
	public void testCreateNew() throws Exception {
		String reelidentifier = "UT-REEL-1";
		Reel reel = reel_r.findByReelidentifier(reelidentifier);
		assertNotNull("Reel is null", reel);

		Metadata  m3 = metadata_r.findByVariablename("UT-Variable3");
		Metadata  m4 = metadata_r.findByVariablename("UT-Variable4");
		 
		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(m3, 60.8d));
		variables.add(new TelemetryValue(m4, 29.096d));
		Telemetry t = new Telemetry(variables,reel,Timestamp.valueOf(now.format(formatter)),SendStatus.NOSEND);
		tele_r.save(t);
		
		log.debug(t.toString());
		tele_r.delete(t);
		
	}

	@Test
	public void test_findByReelAndTimestamp() throws Exception {
		String reelidentifier = "UT-REEL-1";
		Reel reel = reel_r.findByReelidentifier(reelidentifier);
		assertNotNull("Reel is null", reel);

		Metadata  m1 = metadata_r.findByVariablename("UT-Variable1");
		 Metadata  m3 = metadata_r.findByVariablename("UT-Variable3");
		 Metadata  m4 = metadata_r.findByVariablename("UT-Variable4");
		 
		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(m1, 3.123457d));
		variables.add(new TelemetryValue(m3, 10.33345d));
		variables.add(new TelemetryValue(m4, 0.33345));
		Telemetry t = new Telemetry(variables,reel,Timestamp.valueOf(now.format(formatter)),SendStatus.NOSEND);
		tele_r.save(t);

		Telemetry telemetry = tele_r.findByReelAndTimestamp(reel,Timestamp.valueOf(now.format(formatter)));
		assertNotNull("Telemetry is null", telemetry);
		Set<TelemetryValue> vars = telemetry.getVariables();
		assertEquals(3, vars.size());

		tele_r.delete(t);
	}

	@Test
	public void test_findAllByReelAnd_NOSEND_Status() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t2);
		
		double[] ary3 = { 87.002d, 9.456d, 2320.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(15l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t3);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReel(reel);
		assertEquals(3, telemetry.size());
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		reel_r.delete(reel);
	}

	@Test
	public void test_findAllByReelAnd_SENT_Status() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)), SendStatus.SENT);
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.SENT);
		tele_r.save(t2);
		
		double[] ary3 = { 87.002d, 9.456d, 2320.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(15l).format(formatter)), SendStatus.SENT);
		tele_r.save(t3);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReel(reel);
		assertEquals(3, telemetry.size());
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		reel_r.delete(reel);
	}

	@Test
	public void test_findAllByReelAnd_NEW_Status() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)), SendStatus.NEW);
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NEW);
		tele_r.save(t2);
		
		double[] ary3 = { 87.002d, 9.456d, 2320.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(15l).format(formatter)), SendStatus.NEW);
		tele_r.save(t3);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReel(reel);
		assertEquals(3, telemetry.size());
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		reel_r.delete(reel);
	}

	@Test
	public void test_findAllByReelAnd_null_Status() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)));
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)));
		tele_r.save(t2);
		
		double[] ary3 = { 87.002d, 9.456d, 2320.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(15l).format(formatter)));
		tele_r.save(t3);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReel(reel);
		assertEquals(3, telemetry.size());
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		reel_r.delete(reel);
	}

	@Test
	public void test_findAllByReelAnd_FALIED_Status() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)),SendStatus.FAILED);
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)),SendStatus.FAILED);
		tele_r.save(t2);
		
		double[] ary3 = { 87.002d, 9.456d, 2320.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(15l).format(formatter)),SendStatus.FAILED);
		tele_r.save(t3);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReel(reel);
		assertEquals(3, telemetry.size());
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		reel_r.delete(reel);
	}
	
	@Test
	public void test_findAllByReelidentifier() throws Exception {
		String reelidentifier = "UT-REEL-1";
		Collection<Telemetry> telemetry = tele_r.findAllByReelidentifier(reelidentifier);
		assertEquals(2, telemetry.size());
	}
	
	@Test
	public void test_findAllByReelAndSendstatus() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t2);
		
		double[] ary3 = { 94.002d, 9.456d, 2134.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t3);
		
		double[] ary4 = { 91.002d, 7.456d, 2034.6d, 2022.0909d };
		Set<TelemetryValue> variables4 = new HashSet<TelemetryValue>();
		variables4.add(new TelemetryValue(var0, ary4[0]));
		variables4.add(new TelemetryValue(var1, ary4[1]));
		variables4.add(new TelemetryValue(var2, ary4[2]));
		variables4.add(new TelemetryValue(var3, ary4[3]));
		Telemetry t4 = new Telemetry(variables4, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t4);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReelAndSendstatus(reel, SendStatus.NOSEND);
		assertEquals(4, telemetry.size());
		for(Telemetry t : telemetry) {
			assertEquals(reelidentifier, t.getReel().getReelidentifier());
		}
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		tele_r.delete(t4);
		reel_r.delete(reel);
	}
	
	@Test
	public void test_findAllByReelidentifierAndSendstatus() throws Exception {
		// NEW REEL
		String reelidentifier = "UT-REEL-"+now.toString();
		Reel reel = new Reel(reelidentifier,ReelType.EXTRUDER,wo_r.findByWorkorderidentifier("UT-WO-1"));
		reel_r.save(reel);
		assertNotNull("Reel is null", reel);
		
		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);
		Set<TelemetryValue> variables1 = new HashSet<TelemetryValue>();
		variables1.add(new TelemetryValue(var0, ary1[0]));
		variables1.add(new TelemetryValue(var1, ary1[1]));
		variables1.add(new TelemetryValue(var2, ary1[2]));
		variables1.add(new TelemetryValue(var3, ary1[3]));
		Telemetry t1 = new Telemetry(variables1, reel, Timestamp.valueOf(now.plusSeconds(5l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t1);
		
		double[] ary2 = { 97.002d, 11.456d, 2334.6d, 2222.0909d };
		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var0, ary2[0]));
		variables2.add(new TelemetryValue(var1, ary2[1]));
		variables2.add(new TelemetryValue(var2, ary2[2]));
		variables2.add(new TelemetryValue(var3, ary2[3]));
		Telemetry t2 = new Telemetry(variables2, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t2);
		
		double[] ary3 = { 94.002d, 9.456d, 2134.6d, 2122.0909d };
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var0, ary3[0]));
		variables3.add(new TelemetryValue(var1, ary3[1]));
		variables3.add(new TelemetryValue(var2, ary3[2]));
		variables3.add(new TelemetryValue(var3, ary3[3]));
		Telemetry t3 = new Telemetry(variables3, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t3);
		
		double[] ary4 = { 91.002d, 7.456d, 2034.6d, 2022.0909d };
		Set<TelemetryValue> variables4 = new HashSet<TelemetryValue>();
		variables4.add(new TelemetryValue(var0, ary4[0]));
		variables4.add(new TelemetryValue(var1, ary4[1]));
		variables4.add(new TelemetryValue(var2, ary4[2]));
		variables4.add(new TelemetryValue(var3, ary4[3]));
		Telemetry t4 = new Telemetry(variables4, reel, Timestamp.valueOf(now.plusSeconds(10l).format(formatter)), SendStatus.NOSEND);
		tele_r.save(t4);
		
		Collection<Telemetry> telemetry = tele_r.findAllByReelidentifierAndSendstatus(reelidentifier, SendStatus.NOSEND);
		assertEquals(4, telemetry.size());
		for(Telemetry t : telemetry) {
			assertEquals(reelidentifier, t.getReel().getReelidentifier());
		}
		
		tele_r.delete(t1);
		tele_r.delete(t2);
		tele_r.delete(t3);
		tele_r.delete(t4);
		reel_r.delete(reel);
	}
	
	@Test
	public void testDateFormats() throws Exception {

        //Get current date time
        LocalDateTime now = LocalDateTime.now();
        log.info("Before : " + now);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        log.info("After  : " + formatDateTime);

		assertTrue(true);
		
	}
	

}
