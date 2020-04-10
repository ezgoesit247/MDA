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

import capgemini.aif.machinedataanalytics.service.Metadata.VariableType;
import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FFTDetailsTests {


	private final Logger log = LoggerFactory.getLogger(FFTDetailsTests.class);
	private long millis;
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}
	private String formatted_timestamp;

	@Before
	public void before() throws Exception {
		setMillis();
		log.debug("\n\t"+"Test Run: "+millis+this.getClass().getSimpleName());
	}
	@After
	public void after() throws Exception {}
	
	@Test
	public void placeholder() throws Exception {
		assertTrue(true);
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
