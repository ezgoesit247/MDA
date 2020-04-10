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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.EventData;

import capgemini.aif.machinedataanalytics.service.Metadata;
import capgemini.aif.machinedataanalytics.service.MetadataRepository;
import capgemini.aif.machinedataanalytics.service.Reel;
import capgemini.aif.machinedataanalytics.service.ReelRepository;
import capgemini.aif.machinedataanalytics.service.Telemetry;
import capgemini.aif.machinedataanalytics.service.TelemetryRepository;
import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockTelemetryTests {

	private void logByReelidentifier(String reelidentifier) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/telemetry/search/findByReelidentifier?reelidentifier={reelidentifier}",reelidentifier)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}

	private final Logger log = LoggerFactory.getLogger(TelemetryTests.class);
	private long millis;
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
