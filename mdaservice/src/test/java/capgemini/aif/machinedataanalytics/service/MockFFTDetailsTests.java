package capgemini.aif.machinedataanalytics.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collection;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockFFTDetailsTests {

	private void logReelByReelidentifier(String reelidentifier) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/reel/search/findByReelidentifier?reelidentifier={reelidentifier}", reelidentifier)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}
	private void logFFTResultAtNdx(String ndx) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/fftresult/"+ndx)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}

	private void logWorkOrderByWorkorderidentifier(String workorderidentifier) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/workorder/search/findByWorkorderidentifier?workorderidentifier={workorderidentifier}", workorderidentifier)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}

	@SuppressWarnings("unused")
	private void logFFTDetailsByWordOrderIdentifier(String workorderuri) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/fftresult/search/findAllByWorkorder?workorderuri={workorderuri}", workorderuri)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
}

	private final Logger log = LoggerFactory.getLogger(FFTDetailsTests.class);
	private long millis;
	@SuppressWarnings("unused")
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}
	private String formatted_timestamp;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void before() throws Exception {
		this.millis = System.currentTimeMillis();
	}
	@After
	public void after() throws Exception {}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.fftresult").exists());
		
		logFFTResultAtNdx("1000026");
	}
	
//	@Test
	public void testfindAllByReel() throws Exception {
		
	}
//	@Test
	public void testFindAllByWorkorder() throws Exception {

		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";
		
//		 this were just/will be made here i hope
		String workorderuri = "workorder/2";
		String reeluri = "reel/2";
		
//		 make a reel
		mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-1-fftdetailstest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated());

		logReelByReelidentifier("EXT-1-fftdetailstest"+millis);
		logWorkOrderByWorkorderidentifier("WO-"+millis);

//		put one fft result
		String wo  = workorderuri;
		String ts  = formatted_timestamp;
		String st  = formatted_timestamp;
		String et  = formatted_timestamp;
		Long f1    = 0l;
		Integer a1 = 0;
		Long f3    = 0l;
		Integer a3 = 0;
		Long fc    = 0l;
		Integer ac = 0;
		
		mockMvc.perform(post("/fftresult")
			.content("{\"workorder\"     : \""+wo+"\",\"timestamp\"     : \""+ts+"\","
					+ "\"starttime\"     : \""+st+"\",\"endtime\"       : \""+et+"\","
					+ "\"d1frequency\"   :   "+f1+",\"d1amplitude\"       : "+a1+","
					+ "\"d3frequency\"   :   "+f3+",\"d3amplitude\"       : "+a3+","
					+ "\"ccoldfrequency\":   "+fc+",\"ccoldamplitude\"    : "+ac+"}"))
				.andExpect(status().isCreated());

//		adjust time and other variables
//		put another result
		formatted_timestamp = new Timestamp(millis+5000).toString().replaceAll(" ", "T") + "+0000";
		f1 += 100;
		a1 += 100;
		f3 += 100;
		a3 += 100;
		fc += 100;
		ac += 100;
		mockMvc.perform(post("/fftresult")
			.content("{\"workorder\"     : \""+wo+"\",\"timestamp\"     : \""+ts+"\","
					+ "\"starttime\"     : \""+st+"\",\"endtime\"       : \""+et+"\","
					+ "\"d1frequency\"   :   "+f1+",\"d1amplitude\"       : "+a1+","
					+ "\"d3frequency\"   :   "+f3+",\"d3amplitude\"       : "+a3+","
					+ "\"ccoldfrequency\":   "+fc+",\"ccoldamplitude\"    : "+ac+"}"))
				.andExpect(status().isCreated());

//		adjust time and other variables
//		put another result
		formatted_timestamp = new Timestamp(millis+5000).toString().replaceAll(" ", "T") + "+0000";
		f1 += 100;
		a1 += 100;
		f3 += 100;
		a3 += 100;
		fc += 100;
		ac += 100;
		mockMvc.perform(post("/fftresult")
			.content("{\"workorder\"     : \""+wo+"\",\"timestamp\"     : \""+ts+"\","
					+ "\"starttime\"     : \""+st+"\",\"endtime\"       : \""+et+"\","
					+ "\"d1frequency\"   :   "+f1+",\"d1amplitude\"       : "+a1+","
					+ "\"d3frequency\"   :   "+f3+",\"d3amplitude\"       : "+a3+","
					+ "\"ccoldfrequency\":   "+fc+",\"ccoldamplitude\"    : "+ac+"}"))
				.andExpect(status().isCreated());
		

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/reel/search/findByWorkorder?workorder={workorder}",workorderuri)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("LOOKHERE\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
		
		mockMvc.perform(get("/reel/search/findByWorkorder?workorder={workorder}",workorderuri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.uri").value(reeluri))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("workorder")));
//				.andExpect(jsonPath("$._links.workorder").
		
		// need to pass URI here
		mockMvc.perform(get("/fftresult/search/findAllByWorkorder?workorder={workorder}", workorderuri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.fftresult.*", isA(Collection.class)))
				.andExpect(jsonPath("$._embedded.fftresult.*", hasSize(3)));
	}
	
//	@Test
	public void shouldNotCreateEmpty() throws Exception {
		mockMvc.perform(post("/fftresult")
			.content("{\"workorder\"     : \"\",\"timestamp\"     : \"\","
					+ "\"starttime\"     : \"\",\"endtime\"       : \"\","
					+ "\"d1frequency\"   : \"\",\"d1amplitude\"   : \"\","
					+ "\"d3amplitude\"   : \"\",\"d3amplitude\"   : \"\","
					+ "\"ccoldfrequency\": \"\",\"ccoldamplitude\": \"\"}"))
		.andExpect(status().is4xxClientError());
	}
//	@Test
	public void shouldCreateRecord() throws Exception {
		String workorderuri = "workorder/2";
		
//		 make a reel
		MvcResult mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-1-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();
		
		String reeluri = "reel/1";
		
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";

		log.debug(formatted_timestamp);
		logWorkOrderByWorkorderidentifier("WO-"+millis);

		String wo  = workorderuri;
		String rl  = reeluri;
		String ts  = formatted_timestamp;
		String st  = formatted_timestamp;
		String et  = formatted_timestamp;
		Long f1    = 0l;
		Integer a1 = 0;
		Long f3    = 0l;
		Integer a3 = 0;
		Long fc    = 0l;
		Integer ac = 0;
		// get workorder uri
		mvcResult = null;
		mvcResult = mockMvc.perform(post("/fftresult")
			.content("{\"reel\"          : \""+rl+"\","
					+ "\"workorder\"     : \""+wo+"\",\"timestamp\"     : \""+ts+"\","
					+ "\"starttime\"     : \""+st+"\",\"endtime\"       : \""+et+"\","
					+ "\"d1frequency\"   :   "+f1+",\"d1amplitude\"       : "+a1+","
					+ "\"d3frequency\"   :   "+f3+",\"d3amplitude\"       : "+a3+","
					+ "\"ccoldfrequency\":   "+fc+",\"ccoldamplitude\"    : "+ac+"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
		
		// test casca
		mockMvc.perform(MockMvcRequestBuilders.get("/"+workorderuri))
				.andExpect(status().isOk());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/"+reeluri))
				.andExpect(status().isOk());
		
		
	}

	@Test
	public void testRequireValidWorkOrder() throws Exception {
		String WorkOrderUri = "workorder/1";
		String ReelUri = "reel/80846";
		
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";

		log.debug(formatted_timestamp);
		logWorkOrderByWorkorderidentifier("WO-"+millis);

		String wo  = WorkOrderUri;
		String rl  = ReelUri;
		String ts  = formatted_timestamp;
		String st  = formatted_timestamp;
		String et  = formatted_timestamp;
		Long f1    = 0l;
		Integer a1 = 0;
		Long f3    = 0l;
		Integer a3 = 0;
		Long fc    = 0l;
		Integer ac = 0;
		// get workorder uri
		mockMvc.perform(post("/fftresult")
				.content("{\"reel\"          : \""+rl+"\","
						+ "\"workorder\"     : \""+wo+"\",\"timestamp\"     : \""+ts+"\","
						+ "\"starttime\"     : \""+st+"\",\"endtime\"       : \""+et+"\","
						+ "\"d1frequency\"   :   "+f1+",\"d1amplitude\"       : "+a1+","
						+ "\"d3frequency\"   :   "+f3+",\"d3amplitude\"       : "+a3+","
						+ "\"ccoldfrequency\":   "+fc+",\"ccoldamplitude\"    : "+ac+"}"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void testRequireValidReel() throws Exception {
		String WorkOrderUri = "workorder/851482";
		String ReelUri = "reel/851482";
		
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";

		log.debug(formatted_timestamp);
		logWorkOrderByWorkorderidentifier("WO-"+millis);

		String wo  = WorkOrderUri;
		String rl  = ReelUri;
		String ts  = formatted_timestamp;
		String st  = formatted_timestamp;
		String et  = formatted_timestamp;
		Long f1    = 0l;
		Integer a1 = 0;
		Long f3    = 0l;
		Integer a3 = 0;
		Long fc    = 0l;
		Integer ac = 0;
		// get workorder uri
		mockMvc.perform(post("/fftresult")
				.content("{\"reel\"          : \""+rl+"\","
						+ "\"workorder\"     : \""+wo+"\",\"timestamp\"     : \""+ts+"\","
						+ "\"starttime\"     : \""+st+"\",\"endtime\"       : \""+et+"\","
						+ "\"d1frequency\"   :   "+f1+",\"d1amplitude\"       : "+a1+","
						+ "\"d3frequency\"   :   "+f3+",\"d3amplitude\"       : "+a3+","
						+ "\"ccoldfrequency\":   "+fc+",\"ccoldamplitude\"    : "+ac+"}"))
				.andExpect(status().is4xxClientError());
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
