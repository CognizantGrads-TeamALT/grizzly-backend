package com.grizzly.grizlibrary;

import com.grizzly.grizlibrary.helpers.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GrizLibraryApplicationTests {

    Helper helper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void makeListFromIterable_ReplicatesIterables() {
        // set up
        List<String> original = new ArrayList<String>();
        original.add("one");
        original.add("three");
        original.add("two");
        List<String> after;

        // execution
        after = helper.makeListFromIterable(original);

        // verification
        assertEquals(original, after);
    }

    @Test
    public void makeListFromIterable_ChangesIterableType() {
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
        result = helper.makeListFromIterable(original);

        // verification
        assertEquals(expected, result);
    }

    @Test
    public void getPageRequest_SortsByField() {
        //set up
        Integer pageIndex = 0;
        String column_name = "name";
        String expected = "name: ASC";
        PageRequest result;
        String resultString;

        //execution
        result = helper.getPageRequest(pageIndex, column_name);
        resultString = result.getSort().toString();

        //verification
        assertEquals(expected, resultString);
    }

    @Test
    public void getPageRequest_DefaultstoId() {
        //set up
        Integer pageIndex = 0;
        String column_name = "invalid";
        String expected = "vendorId: ASC";
        PageRequest result;
        String resultString;

        //execution
        result = helper.getPageRequest(pageIndex, column_name);
        resultString = result.getSort().toString();

        //verification
        assertEquals(expected, resultString);
    }
}