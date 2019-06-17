package com.tcurtil.kata.bankocr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tcurtil.kata.bankocr.dao.RawIdentifierDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankOcrUiTests {

	@Autowired
    private WebApplicationContext wac;
    
    protected MockMvc mockMvc;
    
    @Resource
    private RawIdentifierDao rawIdentifierDao;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void testRootPage() throws Exception {
		MvcResult result = mockMvc
			.perform(
				get("/")
					
		    )
			.andExpect(status().isOk())
			.andReturn();
		
		String responseAsString = result.getResponse().getContentAsString();
		for(String filename : rawIdentifierDao.listFilenames()) {
			Assert.assertTrue("Filename " + filename + " was not found in produced html page.", responseAsString.contains(filename));
		}
	}
    
    @Test
    public void testParsingResultOk() throws Exception {
		MvcResult result = mockMvc
			.perform(
				get("/?filename=account_list_2.txt")
					
		    )
			.andExpect(status().isOk())
			.andReturn();
		
		String responseAsString = result.getResponse().getContentAsString();
		Assert.assertTrue("Page should have contained the 888888888 account", responseAsString.contains("888888888"));
	}
    
    @Test
    public void testParsingResultError() throws Exception {
		MvcResult result = mockMvc
			.perform(
				get("/?filename=account_list_invalid_1.txt")
					
		    )
			.andExpect(status().isOk())
			.andReturn();
		
		String responseAsString = result.getResponse().getContentAsString();
		Assert.assertTrue("Page should have an error message included", 
				responseAsString.contains("file account_list_invalid_1.txt do not contain a number of lines that is a multiple of 4."));
	}
	
}
