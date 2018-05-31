package com.grizzly.productmicro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMicroApplicationTests {
    @Autowired
    ProductService testService;

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

    @Test
    public void getPageRequest_SortsByField() {
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
    public void getPageRequest_DefaultstoId() {
        //set up
        String column_name = "invalid";
        String expected = "productId: ASC";
        PageRequest result;
        String resultString;

        //execution
        result = testService.getPageRequest(column_name);
        resultString = result.getSort().toString();

        //verification
        assertEquals(expected, resultString);
    }
}