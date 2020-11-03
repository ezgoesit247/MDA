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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockTelemetryTests {

	@SuppressWarnings("unused")
	private void logByReelidentifier(String reelidentifier) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/telemetry/search/findByReelidentifier?reelidentifier={reelidentifier}",reelidentifier)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}

	private final Logger log = LoggerFactory.getLogger(TelemetryTests.class);
	@SuppressWarnings("unused")
	private long millis;
	@SuppressWarnings("unused")
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void before() throws Exception {}
	@After
	public void after() throws Exception {}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.telemetry").exists());
	}


}
