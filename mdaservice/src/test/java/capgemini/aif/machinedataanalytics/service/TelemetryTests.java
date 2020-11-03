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
public class TelemetryTests {

	private long millis;
	private void setMillis() {
		millis = System.currentTimeMillis();
	}

	private final Logger log = LoggerFactory.getLogger(TelemetryTests.class);
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

	@Test
	public void test_findByReelidentifierAndTimestamp() throws Exception {
		String reelidentifier = "UT-REEL-1";
		
		Reel reel = new Reel(reelidentifier, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder("WO"+now.toString(), Timestamp.valueOf(now.format(formatter)))));
		reel_r.save(reel);

		assertNotNull("Reel is null", reel);

		Metadata  m1 = metadata_r.save(new Metadata("in_oven_heater_tmp","VAR1000",VariableType.DEFAULT));
		Metadata  m2 = metadata_r.save(new Metadata("in_capstan_tension","VAR1001",VariableType.TENSION));
		Metadata  m3 = metadata_r.save(new Metadata("in_taper1_rpm","VAR1002",VariableType.SPEED));
		 
		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(m1, 3.123457d));
		variables.add(new TelemetryValue(m2, 10.33345d));
		variables.add(new TelemetryValue(m3, 12.6645d));
		Telemetry t = new Telemetry(variables,reel,Timestamp.valueOf(now.format(formatter)),SendStatus.NOSEND);

		tele_r.save(t);

		Telemetry telemetry = tele_r.findByReelidentifierAndTimestamp(reelidentifier,Timestamp.valueOf(now.format(formatter)));
		assertNotNull("Telemetry is null", telemetry);
		Set<TelemetryValue> vars = telemetry.getVariables();
		assertEquals(3, vars.size());

		tele_r.delete(t);
	}
	
	@Test
	public void testCreateNew() throws Exception {
		String reelidentifier1 = "ext-reel-2"+millis;
		String workorderidentifier1 = "UT-WO-2"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier1, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier1, Timestamp.valueOf(now.format(formatter)))))));
		
		Reel reel = reel_r.findByReelidentifier(reelidentifier1);

		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var1 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier4);

		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(var1, 65.61d));
		variables.add(new TelemetryValue(var2, 87.809d));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		Timestamp ts = Timestamp.valueOf(now.format(formatter));
		Telemetry t = new Telemetry(variables, reel, ts, SendStatus.NOSEND);
		assertNotNull(t);
		tele_r.save(t);
		
	}

	@Test
	public void test_findByReelAndTimestamp() throws Exception {
		String reelidentifier = "ext-1"+millis;
		String workorderidentifier = "WO-1"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier, Timestamp.valueOf(now.format(formatter)))))));
		
		Reel reel = reel_r.findByReelidentifier(reelidentifier);
		assertNotNull("Reel is null", reel);

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		assertNotNull("var0 is null", var0);
		assertNotNull("var1 is null", var1);
		assertNotNull("var2 is null", var2);
		 
		Set<TelemetryValue> variables = new HashSet<TelemetryValue>();
		variables.add(new TelemetryValue(var0, 3.123457d));
		variables.add(new TelemetryValue(var1, 10.33345d));
		variables.add(new TelemetryValue(var2, 0.33345));
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String reelidentifier = "ext-1"+millis;
		String workorderidentifier = "WO-1"+millis;
		assertNotNull(reel_r.save(new Reel(reelidentifier, ReelType.EXTRUDER, wo_r.save(
				new WorkOrder(workorderidentifier, Timestamp.valueOf(now.format(formatter)))))));
		
		Reel reel = reel_r.findByReelidentifier(reelidentifier);


		// VARIABLES SET
		double[] ary1 = { 98.002d, 12.456d, 2335.6d, 2333.0909d };

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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

		String metaidentifier1 = "METAVAR-1"+millis;
		String metaidentifier2 = "METAVAR-2"+millis;
		String metaidentifier3 = "METAVAR-3"+millis;
		String metaidentifier4 = "METAVAR-4"+millis;
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier1,VariableType.DIGITAL,"in_oven_heater_tmp"+millis, "Machine1","EQUIP1")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier2,VariableType.TENSION,"pred_insertion_loss"+millis, "Machine2","EQUIP2")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier3,VariableType.REEL_CAPACITY,"oven_exit_tmp"+millis, "Machine3","EQUIP3")));
		assertNotNull(metadata_r.save(
				new Metadata(metaidentifier4,VariableType.DEFAULT,"taper1_outlet_tension"+millis, "Machine4","EQUIP4")));
		
		Metadata var0 = metadata_r.findByVariablename(metaidentifier1);
		Metadata var1 = metadata_r.findByVariablename(metaidentifier2);
		Metadata var2 = metadata_r.findByVariablename(metaidentifier3);
		Metadata var3 = metadata_r.findByVariablename(metaidentifier4);
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
