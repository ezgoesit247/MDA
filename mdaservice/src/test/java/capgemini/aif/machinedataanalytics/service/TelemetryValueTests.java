package capgemini.aif.machinedataanalytics.service;

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
import capgemini.aif.machinedataanalytics.service.TelemetryRepository;

import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TelemetryValueTests {

	private final Logger log = LoggerFactory.getLogger(TelemetryValueTests.class);

	@Autowired
	private TelemetryRepository tele_r;

	@Autowired
	private TelemetryValueRepository tv_r;

	@Autowired
	private ReelRepository reel_r;

	@Autowired
	private MetadataRepository metadata_r;

	@Autowired
	WorkOrderRepository wo_r;
	
	@Test(expected = RuntimeException.class)
	public void testNullVariables() throws Exception {

		Reel reel = reel_r.findByReelidentifier("UT-REEL-1");
		Set<TelemetryValue> variables = null;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts);
		assertNotNull(t);
		tele_r.save(t);

	}
	
	@Test(expected = RuntimeException.class)
	public void testNoVariables() throws Exception {

		Reel reel = reel_r.findByReelidentifier("UT-REEL-1");
		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts);
		assertNotNull(t);
		tele_r.save(t);

	}

	@Test
	public void testVariables() throws Exception {
		double[] ary = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		
		Reel reel = reel_r.findByReelidentifier("UT-REEL-1");
		Metadata var0 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable2");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable3");
		Metadata var3 = metadata_r.findByVariablename("UT-Variable4");
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		assertNotNull("var3 is null", var3);

		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		
		variables.add(new TelemetryValue(var0, ary[0]));
		variables.add(new TelemetryValue(var1, ary[1]));
		variables.add(new TelemetryValue(var2, ary[2]));
		variables.add(new TelemetryValue(var3, ary[3]));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts);
		assertNotNull(t);
		tele_r.save(t);
		t = null;

		Telemetry new_t = tele_r.findByReelAndTimestamp(reel, ts);
		assertNotNull(new_t);
		Set<TelemetryValue> tele_values = new_t.getVariables();
		assertEquals(4, tele_values.size());

		/* CANT DELETE THESE YET */
//		Collection<TelemetryValue> c = new_t.getVariables();
//		for(TelemetryValue tv : c)
//			tv_r.delete(tv);
		tele_r.delete(new_t);
	}

	@Test
	public void testFindAllByReel() throws Exception {
		Reel reel = reel_r.findByReelidentifier("UT-REEL-1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable2");

		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(var1, 355.2d));
		variables.add(new TelemetryValue(var2, 55.864d));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts);
		assertNotNull(t);
		tele_r.save(t);
		Collection<Telemetry> t_colletion = tele_r.findAllByReel(reel);
		assertNotNull(t_colletion);
		assertTrue(t_colletion.size() > 0);

		/* CANT DELETE THESE YET */
//		Collection<TelemetryValue> c = t.getVariables();
//		for(TelemetryValue tv : c)
//			tv_r.delete(tv);
		tele_r.delete(t);
	}

	@Test
	public void testFindByTimestamp() throws Exception {
		Reel reel = reel_r.findByReelidentifier("UT-REEL-1");
		Metadata var1 = metadata_r.findByVariablename("UT-Variable1");
		Metadata var2 = metadata_r.findByVariablename("UT-Variable2");

		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(var1, 65.61d));
		variables.add(new TelemetryValue(var2, 87.809d));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts);
		assertNotNull(t);
		tele_r.save(t);
		Collection<Telemetry> t_colletion = tele_r.findAllByTimestamp(ts);
		assertNotNull(t_colletion);
		assertEquals(1, t_colletion.size());
		
		/* CANT DELETE THESE YET */
//		TODO: Figure out how to remove these
//		Collection<TelemetryValue> c = t.getVariables();
//		for(TelemetryValue tv : c)
//			tv_r.delete(tv);
		tele_r.delete(t);

	}
}