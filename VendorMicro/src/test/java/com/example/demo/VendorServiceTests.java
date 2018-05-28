package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
    public void makeListFromIterableChangesIterableType() {
        // set up
        List<String> expected = new ArrayList<String>();
        expected.add("one");
        expected.add("three");
        expected.add("two");

        List<String> original = new LinkedList<String>();
        original.add("one");
        original.add("three");
        original.add("two");

        ArrayList<String> result;

        // execution
        result = testService.makeListFromIterable(original);

        // verification
        assertEquals(expected, result);
    }

}
