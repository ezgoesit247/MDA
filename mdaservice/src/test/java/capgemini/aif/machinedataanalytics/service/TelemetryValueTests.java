package capgemini.aif.machinedataanalytics.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import capgemini.aif.machinedataanalytics.service.Metadata.VariableType;
import capgemini.aif.machinedataanalytics.service.Reel.ReelType;
import capgemini.aif.machinedataanalytics.service.Telemetry.SendStatus;

import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TelemetryValueTests {

	private long millis;
	private void setMillis() {
		millis = System.currentTimeMillis();
	}

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(TelemetryValueTests.class);
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
		String reelidentifier1 = "ext-reel-1"+millis;
		String workorderidentifier1 = "UT-WO-1"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier1, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier1, Timestamp.valueOf(now.format(formatter)))))));
		
		double[] ary = { 98.002d, 12.456d, 2335.6d, 2333.0909d };
		
		Reel reel = reel_r.findByReelidentifier(reelidentifier1);
		assertNotNull(reel);

		String metaidentifier1 = "METAVAR-10"+millis;
		String metaidentifier2 = "METAVAR-20"+millis;
		String metaidentifier3 = "METAVAR-30"+millis;
		String metaidentifier4 = "METAVAR-40"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp0", "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss0", "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp0", "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension0", "Machine4","EQUIP4")));

		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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
		Telemetry t = new Telemetry(variables, reel, ts, SendStatus.NOSEND);
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
		String reelname = "ext-1"+millis;
		String woname = "wo-1"+millis;
		reel_r.save(new Reel(reelname, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(woname, Timestamp.valueOf(now.format(formatter))))));

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		
		Reel reel = reel_r.findByReelidentifier(reelname);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier2);

		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(var1, 355.2d));
		variables.add(new TelemetryValue(var2, 55.864d));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts, SendStatus.NOSEND);
		assertNotNull(t);
		tele_r.save(t);
		Collection<Telemetry> t_colletion = tele_r.findAllByReel(reel);
		assertNotNull(t_colletion);
		assertTrue(t_colletion.size() > 0);

		/* CANT DELETE THESE YET */
//		Collection<TelemetryValue> c = t.getVariables();
//		for(TelemetryValue tv : c)
//			tv_r.delete(tv);
//		tele_r.delete(t);
	}

	@Test
	public void testFindByTimestamp() throws Exception {
		String reelidentifier2 = "ext-reel-2"+millis;
		String workorderidentifier2 = "UT-WO-2"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier2, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier2, Timestamp.valueOf(now.format(formatter)))))));
		
		Reel reel2 = reel_r.findByReelidentifier(reelidentifier2);

		String reelidentifier3 = "ext-reel-3"+millis;
		String workorderidentifier3 = "UT-WO-3"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier3, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier3, Timestamp.valueOf(now.format(formatter)))))));
		
		Reel reel3 = reel_r.findByReelidentifier(reelidentifier3);

		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.REEL_CAPACITY,"oven_humidity"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var2 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var4 = metadata_r.findByVariablename(metaidentifier4);

		Set<TelemetryValue> variables2 = new HashSet<TelemetryValue>();
		variables2.add(new TelemetryValue(var3, 65.61d));
		variables2.add(new TelemetryValue(var4, 87.809d));
		Set<TelemetryValue> variables3 = new HashSet<TelemetryValue>();
		variables3.add(new TelemetryValue(var2, 8.61d));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts0 = Timestamp.valueOf(now.format(formatter));
		Timestamp ts  = new Timestamp(ts0.getTime() + (600 * 1000l));

		Telemetry t0 = new Telemetry(variables2, reel2, ts, SendStatus.NOSEND);
		assertNotNull(t0);
		tele_r.save(t0);
		
		Telemetry t1 = new Telemetry(variables3, reel3, ts, SendStatus.NOSEND);
		assertNotNull(t1);
		tele_r.save(t1);

		
		Collection<Telemetry> t_colletion = tele_r.findAllByTimestamp(ts);
		assertNotNull(t_colletion);
		assertEquals(2, t_colletion.size());
		
		/* CANT DELETE THESE YET */
//		TODO: Figure out how to remove these
//		Collection<TelemetryValue> c = t.getVariables();
//		for(TelemetryValue tv : c)
//			tv_r.delete(tv);
//		tele_r.delete(t);

	}
}