package capgemini.aif.machinedataanalytics.service;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

import org.hamcrest.core.IsNull;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import capgemini.aif.machinedataanalytics.service.Metadata;
import capgemini.aif.machinedataanalytics.service.MetadataRepository;
import capgemini.aif.machinedataanalytics.service.Metadata.VariableType;
import capgemini.aif.machinedataanalytics.service.Reel.ReelType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetadataTests {

	private final Logger log = LoggerFactory.getLogger(MetadataTests.class);
	
	Metadata v1;

	@Before
	public void before() throws Exception {
		log.debug("\n\t"+"Test Run: "+System.currentTimeMillis()+this.getClass().getSimpleName());
	}
	@After
	public void after() throws Exception {}
	
	@Test
	public void placeholder() throws Exception {
		assertTrue(true);
	}

}
