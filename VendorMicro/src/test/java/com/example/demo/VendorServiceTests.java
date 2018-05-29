package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VendorServiceTests {

    @Autowired
    VendorService testService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void makeListFromIterableReplicatesIterables() {
	    // set up
        List<String> original = new ArrayList<String>();
        original.add("one");
        original.add("three");
        original.add("two");
        List<String> after;

		// execution
        after = testService.makeListFromIterable(original);

		// verification
        assertEquals(original, after);
	}

    @Test
    public void getPageRequest_SortsByField()
    {
        //set up
        String column_name = "name";
        String expected = "name: ASC";
        PageRequest result;
        String resultString;

        //execution
        result = testService.getPageRequest(column_name);
        resultString = result.getSort().toString();

        //verification
        assertEquals(expected, resultString);

    }

    @Test
    public void getPageRequest_DefaultstoId()
    {
        //set up
        String column_name = "invalid";
        String expected = "vendorId: ASC";
        PageRequest result;
        String resultString;

        //execution
        result = testService.getPageRequest(column_name);
        resultString = result.getSort().toString();

        //verification
        assertEquals(expected, resultString);


    }

}
