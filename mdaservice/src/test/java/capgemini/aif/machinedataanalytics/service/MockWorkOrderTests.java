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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockWorkOrderTests {

	private void logByWorkorderidentifier(String workorderidentifier) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/workorder/search/findByWorkorderidentifier?workorderidentifier={workorderidentifier}", workorderidentifier)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}

	private final Logger log = LoggerFactory.getLogger(WorkOrderTests.class);
	private long millis;
	private String formatted_timestamp;
	private String loc;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void before() throws Exception {
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";
		MvcResult mvcResult = mockMvc.perform(post("/workorder").content("{\"timestamp\": \""+formatted_timestamp+"\", \"workorderidentifier\": \"WO-"+millis+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();
		loc = mvcResult.getResponse().getHeader("Location");
		logByWorkorderidentifier("WO-"+millis);
	}
	@After
	public void after() throws Exception {
//		delete it
		mockMvc.perform(delete(loc))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(loc))
				.andExpect(status().isNotFound());
		}

	@Test
	public void testWorkOrderRepo() throws Exception {
		mockMvc.perform(get("/")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.workorder").exists());
	}
	
	@Test
	public void testFindByWorkorderidentifier() throws Exception {
		mockMvc.perform(get("/workorder/search/findByWorkorderidentifier?workorderidentifier={workorderidentifier}", "WO-"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.workorderidentifier").value("WO-"+millis));
	}
	
	@Test
	public void testUniqueIdentifier() throws Exception {
		mockMvc.perform(post("/workorder").content("{\"timestamp\": \""+formatted_timestamp+"\", \"workorderidentifier\": \"WO-"+millis+"\"}"))
				.andExpect(status().is4xxClientError());
	}
	
	
	@Test
	public void testFindReelByWorkorder() throws Exception {
		
		MvcResult mvcResult = mockMvc.perform(get("/workorder/search/findByWorkorderidentifier?workorderidentifier={workorderidentifier}", "WO-"+millis))
				.andExpect(status().isOk()).andReturn();

		assertNotNull("mvcResult is null",mvcResult);
		
		String location = mvcResult.getResponse().getHeader("Location");
		assertNotNull("response is null",mvcResult.getResponse());
		assertNull("Location is not null", location);
		
		mvcResult = null;
		location = null;

		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";
		mvcResult = mockMvc.perform(post("/workorder").content("{\"timestamp\": \""+formatted_timestamp+"\", \"workorderidentifier\": \"WO-"+millis+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();
		location = mvcResult.getResponse().getHeader("Location");
		assertNotNull("response is null",mvcResult.getResponse());
		assertNotNull("Location is null", location);

		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
		
//		mockMvc.perform(get(location))
//				.andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldCreateDefault() throws Exception {
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";
		MvcResult mvcResult = mockMvc.perform(post("/workorder").content("{\"timestamp\": \""+formatted_timestamp+"\", \"workorderidentifier\": \"WO-"+millis+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();
		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}
	
//	@Test can't use this
	public void shouldCreateAndDeleteWO_number2() throws Exception {
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";

//		 make a wo
		MvcResult mvcResult = mockMvc.perform(put("/workorder/2").content("{\"timestamp\": \""+formatted_timestamp+"\", \"workorderidentifier\": \"WO-"+millis+"\"}"))
				.andExpect(status().isCreated())
		.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		
//		test it can be found
		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.uri").value("workorder/2"));

//		delete it
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testUri() throws Exception {
		this.millis = System.currentTimeMillis();
		formatted_timestamp = new Timestamp(millis).toString().replaceAll(" ", "T") + "+0000";
		MvcResult mvcResult = mockMvc.perform(post("/workorder").content("{\"timestamp\": \""+formatted_timestamp+"\", \"workorderidentifier\": \"WO-"+millis+"\"}"))
				.andExpect(status().isCreated())
			.andReturn();

		logByWorkorderidentifier("WO-"+millis);
		
		mockMvc.perform(get("/workorder/search/findByWorkorderidentifier?workorderidentifier={workorderidentifier}", "WO-"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.workorderidentifier").value("WO-"+millis))
				.andExpect(jsonPath("$.uri").isNotEmpty())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("workorder/")));

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}

}
