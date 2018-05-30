package com.grizzly.categorymicro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategorymicroApplicationTests {

	@Autowired
	CategoryService testService;

	@InjectMocks
	CategoryService mockService;

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
	public void getPageRequest_DefaultstoId()
	{
		//set up
		String column_name = "invalid";
		String expected = "categoryId: ASC";
		PageRequest result;
		String resultString;

		//execution
		result = testService.getPageRequest(column_name);
		resultString = result.getSort().toString();

		//verification
		assertEquals(expected, resultString);


	}

	@Test
	public void update_changesNameAndDescription() {
		// set up
		String id = "testId";
		String newName = "new";
		String newDescription = "new desc";
		Category testCat = new Category(id, "old", "old desc", true);

		CategoryRepository mockRepo = mock(CategoryRepository.class);
		when(mockRepo.findById(anyString())).thenReturn(Optional.of(testCat));
		when(mockRepo.save(any(Category.class))).thenReturn(testCat);
		mockService.setCategoryRepository(mockRepo);

		// execution
		Category newCat = mockService.update(id, newName, newDescription);

		// verification
		assertEquals(newName, newCat.getName());
		assertEquals(newDescription, newCat.getDescription());

	}

	@Test
	public void update_unchangedValuesRemain() {
		// set up
		String id = "testId";
		String newName = "new";
		String newDescription = "old desc";
		Category testCat = new Category(id, "old", "old desc", true);

		CategoryRepository mockRepo = mock(CategoryRepository.class);
		when(mockRepo.findById(anyString())).thenReturn(Optional.of(testCat));
		when(mockRepo.save(any(Category.class))).thenReturn(testCat);
		mockService.setCategoryRepository(mockRepo);

		// execution
		Category newCat = mockService.update(id, newName, newDescription);

		// verification
		assertEquals(newName, newCat.getName());
		assertEquals(newDescription, newCat.getDescription());
	}

	@Test
	public void update_returnsNullIfNonexistant() {
		// set up
		String id = "testId";
		String newName = "new";
		String newDescription = "new desc";
		Category testCat = new Category(id, "old", "old desc", true);

		CategoryRepository mockRepo = mock(CategoryRepository.class);
		when(mockRepo.findById(anyString())).thenReturn(Optional.empty());
		when(mockRepo.save(any(Category.class))).thenReturn(testCat);
		mockService.setCategoryRepository(mockRepo);

		// execution
		Category newCat = mockService.update(id, newName, newDescription);

		// verification
		assertNull(newCat);
	}
}

