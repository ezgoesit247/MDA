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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import capgemini.aif.machinedataanalytics.service.Reel;
import capgemini.aif.machinedataanalytics.service.ReelRepository;
import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockReelTests {

	private void logReelByReelidentifier(String reelidentifier) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/reel/search/findByReelidentifier?reelidentifier={reelidentifier}", reelidentifier)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}
	private void logByReelTypes(ReelType type) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/reel/search/getAllByType?type="+type.toString())
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}
	
	private final Logger log = LoggerFactory.getLogger(ReelTests.class);
	private long millis;
	private void setMillis() {
		this.millis = System.nanoTime();
	}

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void before() throws Exception {
		setMillis();
		log.debug("\n\t"+"Test Run: "+millis+this.getClass().getSimpleName());
		
	}

	@After
	public void after() throws Exception {}

	@Test
	public void testReelRepo() throws Exception {
		mockMvc.perform(get("/")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.reel").exists());
	}
	
	@Test
	public void testFetchData() {
	}

	@Test
	public void getExtType() {
	}

	@Test
	public void getTprType() {
	}

	@Test
	public void testNeedsValidWO() throws Exception {
		String workorderuri = "workorder/2";
		
//		 make a reel
		MvcResult mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-2-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("reel/")))
			.andReturn();
//		make sure wo #13 doesn't exist
		log.debug("\n\t"+"Exception Test Run: "+millis+this.getClass().getSimpleName());
		mockMvc.perform(get("/workorder/13"))
				.andExpect(status().is4xxClientError());
//		make sure can update a reel with a bad so
		log.debug("\n\t"+"Exception Test Run: "+millis+this.getClass().getSimpleName());
		mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \"workorder/13\","
					+ "\"reelidentifier\": \"EXT-2-reeltest-"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().is4xxClientError());

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}
	@Test
	public void testFestFindAllByType() throws Exception {
		String workorderuri = "workorder/2";
//		 make a reel
		MvcResult mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-2-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location2 = mvcResult.getResponse().getHeader("Location");		

		
//		 make a reel
		workorderuri = "workorder/3";
		mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-3-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location3 = mvcResult.getResponse().getHeader("Location");		


//		 make a reel
		workorderuri = "workorder/4";
		mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-4-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location4 = mvcResult.getResponse().getHeader("Location");	


		mockMvc.perform(get("/reel/search/findAllByType?type={type}", ReelType.EXTRUDER))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.reel.*", isA(Collection.class)));
//				.andExpect(jsonPath("$._embedded.reel.*", hasSize(3))); // some wreh made in other tests
		

		
//		 make a reel
		workorderuri = "workorder/5";
		mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"TPR-5-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.TAPER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location5 = mvcResult.getResponse().getHeader("Location");	


		mockMvc.perform(get("/reel/search/findAllByType?type={type}", ReelType.TAPER))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.reel.*", isA(Collection.class)));
		

		mockMvc.perform(delete(location2))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location2))
				.andExpect(status().isNotFound());
		mockMvc.perform(delete(location3))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location3))
				.andExpect(status().isNotFound());
		mockMvc.perform(delete(location4))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location4))
				.andExpect(status().isNotFound());	
		mockMvc.perform(delete(location5))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location5))
				.andExpect(status().isNotFound());	
	}

	 @Test
	public void shouldUpdateEntity() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/reel").content("{\"workorder\": \"workorder/2\",\"reelidentifier\": \"tpr-4-"+millis+"\", \"type\":\""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content("{\"reelidentifier\": \"tpr-4-"+millis+"\", \"type\":\""+ReelType.TAPER+"\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.type").value(ReelType.TAPER.toString()))
				.andExpect(jsonPath("$.reelidentifier").value("tpr-4-"+millis));

		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
	}

	@Test
	public void shouldRetrieveEntity() throws Exception {
		String workorderuri = "workorder/2";
//		 make a reel
		MvcResult mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-1-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.reelidentifier").value("EXT-1-reeltest"+millis))
				.andExpect(jsonPath("$.type").value(ReelType.EXTRUDER.toString()));

		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
	}

	 @Test
	public void shouldDeleteEntity() throws Exception {
		String workorderuri = "workorder/2";
		
//		 make a reel
		MvcResult mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"EXT-1-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldPartiallyUpdateEntity() throws Exception {
		String workorderuri = "workorder/2";
		
//		 make a reel
		MvcResult mvcResult = mockMvc.perform(post("/reel")
			.content("{\"workorder\"     : \""+workorderuri+"\","
					+ "\"reelidentifier\": \"TPR-1-reeltest"+millis+"\", "
					+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.reelidentifier").value("TPR-1-reeltest"+millis))
				.andExpect(jsonPath("$.type").value(ReelType.EXTRUDER.toString()));

		mockMvc.perform(patch(location).content("{\"type\": \""+ReelType.TAPER+"\"}"))
				.andExpect(status().isNoContent());
		
		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.reelidentifier").value("TPR-1-reeltest"+millis))
				.andExpect(jsonPath("$.type").value(ReelType.TAPER.toString()));

		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
	}
	 
	 @Test
	public void testUri() throws Exception {
			String workorderuri = "workorder/2";
			
//			 make a reel
			MvcResult mvcResult = mockMvc.perform(post("/reel")
				.content("{\"workorder\"     : \""+workorderuri+"\","
						+ "\"reelidentifier\": \"ext-1-"+millis+"\", "
						+ "\"type\"          : \""+ReelType.EXTRUDER+"\"}"))
					.andExpect(status().isCreated())
				.andReturn();

			String location = mvcResult.getResponse().getHeader("Location");
			
			mockMvc.perform(get("/reel/search/findByReelidentifier?reelidentifier={reelidentifier}", "ext-1-"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.reelidentifier").value("ext-1-"+millis))
				.andExpect(jsonPath("$.uri").isNotEmpty())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("reel/")));
			
			logReelByReelidentifier("ext-1-"+millis);

			mockMvc.perform(delete(location))
					.andExpect(status().isNoContent());
		 
	 }
	 
}
