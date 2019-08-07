package de.comparus.opensource.longmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LongMapImplTest {

	public static final int START_SIZE = 5;

	LongMapImpl<String> _map;
	long _existingKey;
	long _nonexistingKey;
	int _startSize;
	String _existingValue;
	String _nonexistingValue;

	@Before
	public void init() {
		_map = new LongMapImpl<>();

		_map.put(12L, "Heinlein");
		_map.put(21L, "Asimov");
		_map.put(524L, "Bradbury");
		_map.put(17L, "Lem");
		_map.put(163L, "Verne");

		_existingKey = 21;
		_nonexistingKey = 10;

		_existingValue = "Asimov";
		_nonexistingValue = "Hoffman";
	}

	@Test
	public void sizeTest() {
		int expected = 5;
		int actual = _map.size();
		Assert.assertEquals("Found incorrect size.", expected, actual);
	}

	@Test
	public void getByExistantKeyTest() {
		String expected = _existingValue;
		String actual = _map.get(_existingKey);
		Assert.assertEquals("Wrong value.", expected, actual);
	}

	@Test
	public void getByNonexistantKeyTest() {
		Assert.assertNull("Result is not a null.", _map.get(_nonexistingKey));
	}

	@Test
	public void containsExistingKeyTest() {
		Assert.assertTrue("Existing key = 21 not found", _map.containsKey(_existingKey));
	}

	@Test
	public void containsNonexistingKeyTest() {
		Assert.assertFalse("Found nonexisting key = 10", _map.containsKey(_nonexistingKey));
	}

	@Test
	public void doesRemoveByExistingKeyReturnsRemovingValueTest() {
		String expected = _existingValue;
		String actual = _map.remove(_existingKey);
		Assert.assertEquals("Get unexpected value.", expected, actual);

	}

	@Test
	public void doesRemoveByNonexistingKeyReturnsNullTest() {
		Assert.assertNull("Returned not a null by nonexistant key", _map.remove(_nonexistingKey));
	}

	@Test
	public void isSizeChangedAfterRemovingByExistingKey() {
		_map.remove(_existingKey);
		int actual = _map.size();
		int expected = START_SIZE;
		Assert.assertNotEquals("Map's size didn't change after operation " + "remove() by existing key.", expected,
				actual);
	}

	@Test
	public void isSizeChangedAfterRemovingByNonexistingKey() {
		_map.remove(_nonexistingKey);
		int actual = _map.size();
		int expected = START_SIZE;
		Assert.assertEquals("Map's size changed after operation remove() " + "by nonexisting key.", expected, actual);
	}

	@Test
	public void isEmptyOnEmptyMapTest() {
		_map = new LongMapImpl<>();
		Assert.assertTrue("Defined map as nonempty while it is empty", _map.isEmpty());
	}

	@Test
	public void isEmptyOnNonemptyMapTest() {
		Assert.assertFalse("Defined map as empty while it is not empty ", _map.isEmpty());
	}

	@Test
	public void isSizeChangesAfterPutByNonexistingKeyTest() {
		_map.put(_nonexistingKey, _nonexistingValue);
		int newSize = _map.size();
		Assert.assertNotEquals("Map's size didn't change after operation put() " + "by nonexisting key", newSize,
				START_SIZE);
	}

	@Test
	public void isSizeChangesAfterPutByExistingKeyTest() {
		_map.put(_existingKey, _nonexistingValue);
		int newSize = _map.size();
		Assert.assertEquals("Map's size changed after operation put() " + "by existing key", newSize, START_SIZE);
	}

	@Test
	public void doesPutReturnPreviousValueByExistingKeyTest() {
		String expacted = _existingValue;
		String actual = _map.put(_existingKey, _nonexistingValue);
		Assert.assertEquals("Operation put() returned incorrect value.", expacted, actual);
	}

	@Test
	public void doesPutReturnNullByNonexistingKeyTest() {
		String actual = _map.put(_nonexistingKey, _nonexistingValue);
		Assert.assertNull("Returned not a null.", actual);
	}

	@Test
	public void isMapIsEmptyAfterClearTest() {
		_map.clear();
		Assert.assertTrue("Map is not empty after clear() operation.", _map.isEmpty());
	}

	@Test
	public void doesMapContainValueByExistingValueTest() {
		Assert.assertTrue("Didn't find existing value in the map.", _map.containsValue(_existingValue));
	}

	@Test
	public void doesMapContainValueByNonexistingValueTest() {
		Assert.assertFalse("Found nonexisting value in the map.", _map.containsValue(_nonexistingValue));
	}

	@Test
	public void doesMapReturnCorrectArrayOfKeysTest() {
		long[] expected = { 12L, 21L, 524L, 17L, 163L };
		long[] actual = _map.keys();
		Arrays.sort(expected);
		Arrays.sort(actual);
		Assert.assertTrue(Arrays.equals(expected, actual));
	}

	@Test
	public void doesMapReturnCorrectArrayOfValuesTest() {
		String[] expectedArray = { "Heinlein", "Asimov", "Bradbury", "Lem", "Verne" };
		List<String> expected = new ArrayList<>(Arrays.asList(expectedArray));
		List<String> actual = _map.values();
		actual.sort(null);
		expected.sort(null);
		Assert.assertTrue(expected.equals(actual));
	}
}
