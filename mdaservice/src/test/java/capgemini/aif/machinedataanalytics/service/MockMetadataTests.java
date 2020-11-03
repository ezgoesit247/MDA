package capgemini.aif.machinedataanalytics.service;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

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

import capgemini.aif.machinedataanalytics.service.Metadata.VariableType;

@TestPropertySource(locations="classpath:application-${SERVICE_TEST_ENVIRONMENT}.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockMetadataTests {

	private void logByVariablename(String variablename) throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/metadata/search/findByVariablename?variablename={variablename}", variablename)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().getContentAsString();
		log.debug("\n\tDumping "+this.getClass().getName()+" object:\n"
				+ result.getResponse().getContentAsString());
	}
	
	private final Logger log = LoggerFactory.getLogger(MetadataTests.class);
	private long millis;
	private void setMillis() {
		this.millis = System.currentTimeMillis();
	}
	String loc1, loc2, loc3, loc4;

	@Autowired
	private MockMvc mockMvc;
	
	@Before
	public void before() throws Exception {
		setMillis();
		log.debug("\n\t"+"Test Run: "+millis+this.getClass().getSimpleName());

		loc1 = mockMvc.perform(post("/metadata").content("{\"shortname\": \"TV1"+millis+"\", \"variablename\": \"test-var-1-"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\"}"))
				.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		loc2 = mockMvc.perform(post("/metadata").content("{\"variablename\": \"test-var-2-"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\", \"shortname\": \"TV2"+millis+"\"}"))
				.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		loc3 = mockMvc.perform(post("/metadata").content("{\"variablename\": \"test-var-3-"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\", \"shortname\": \"TV3"+millis+"\"}"))
				.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");

		loc4 = mockMvc.perform(post("/metadata").content("{\"variablename\": \"test-var-4-"+millis+"\", \"shortname\": \"TV4"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\"}"))
				.andExpect(status().isCreated())
			.andReturn().getResponse().getHeader("Location");
	}
	
	@After
	public void after() throws Exception {
		mockMvc.perform(delete(loc1))
		.andExpect(status().isNoContent());
		mockMvc.perform(delete(loc2))
		.andExpect(status().isNoContent());
		mockMvc.perform(delete(loc3))
		.andExpect(status().isNoContent());
		mockMvc.perform(delete(loc4))
		.andExpect(status().isNoContent());
		
	}
	

	//	@Test
	// can't use this if we save to sql and don't call repo.deleteAll() @Before
	public void testfindAllByType() throws Exception {
		mockMvc.perform(get("/metadata/search/findAllByType?type={type}", VariableType.DEFAULT))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.metadata.*", isA(Collection.class)))
				.andExpect(jsonPath("$._embedded.metadata.*", hasSize(4)));
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.metadata").exists());
	}

	@Test
	public void testCreateAndDatabaseDefaults() throws Exception {
		// type has default set @ db
		// short name is not null
		log.debug("\n\t"+"Exception Test Run: "+millis+this.getClass().getSimpleName());
		mockMvc.perform(post("/metadata").content("{\"variablename\": \"VARIABLE_NAME1\"}"))
				.andExpect(status().is4xxClientError());

		String loc = mockMvc.perform(post("/metadata").content("{\"variablename\": \"VARIABLE_NAME4"+millis+"\", \"shortname\": \"SHRT4"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("metadata/")))
			.andReturn().getResponse().getHeader("Location");
		
		mockMvc.perform(delete(loc))
				.andExpect(status().isNoContent());

	}
	
	@Test
	public void shouldNotDoAnything() throws Exception {
		// unique short name
		log.debug("\n\t"+"Exception Test Run: "+millis+this.getClass().getSimpleName());
		mockMvc.perform(post("/metadata").content("{\"variablename\": \"TEST1"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\", \"shortname\": \"TV1"+millis+"\"}"))
				.andExpect(status().is4xxClientError());

		// unique short name
		log.debug("\n\t"+"Exception Test Run: "+millis+this.getClass().getSimpleName());
		mockMvc.perform(post("/metadata").content("{\"variablename\": \"TEST2"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\", \"shortname\": \"TV2"+millis+"\"}"))
				.andExpect(status().is4xxClientError());

		// unique variable name (pk -- DUH)
		log.debug("\n\t"+"Exception Test Run: "+millis+this.getClass().getSimpleName());
		mockMvc.perform(post("/metadata").content("{\"variablename\": \"test-var-1-"+millis+"\", \"type\":\""+VariableType.DEFAULT+"\", \"shortname\": \"TV3"+millis+"\"}"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void testfindByVariablename() throws Exception {
		mockMvc.perform(get("/metadata/search/findByVariablename?variablename={variablename}", "test-var-1-"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.type").value(VariableType.DEFAULT.toString()))
				.andExpect(jsonPath("$.variablename").value("test-var-1-"+millis));
	}

	@Test
	public void testfindByVariableshortname() throws Exception {
		mockMvc.perform(get("/metadata/search/findByVariablename?variablename={variablename}", "test-var-2-"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.type").value(VariableType.DEFAULT.toString()))
				.andExpect(jsonPath("$.variablename").value("test-var-2-"+millis))
				.andExpect(jsonPath("$.shortname").value("TV2"+millis));

		mockMvc.perform(get("/metadata/search/findByShortname?shortname={shortname}", "TV3"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.type").value(VariableType.DEFAULT.toString()))
				.andExpect(jsonPath("$.variablename").value("test-var-3-"+millis))
				.andExpect(jsonPath("$.shortname").value("TV3"+millis));

		mockMvc.perform(get("/metadata/search/findByShortname?shortname={shortname}", "TV4"+millis))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.type").value(VariableType.DEFAULT.toString()))
				.andExpect(jsonPath("$.variablename").value("test-var-4-"+millis))
				.andExpect(jsonPath("$.shortname").value("TV4"+millis));
		
	}
	 
	 @Test
	public void testUri() throws Exception {
			mockMvc.perform(get("/metadata/search/findByVariablename?variablename={variablename}", "test-var-3-"+millis))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.variablename").value("test-var-3-"+millis))
			.andExpect(jsonPath("$.uri").isNotEmpty())
			.andExpect(content().string(org.hamcrest.Matchers.containsString("metadata/")));
			
			logByVariablename("test-var-3-"+millis);
		 
	 }

}
