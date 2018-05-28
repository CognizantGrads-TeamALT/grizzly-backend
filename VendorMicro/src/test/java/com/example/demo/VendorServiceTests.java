package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

}
