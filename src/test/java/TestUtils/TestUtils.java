package TestUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TestUtils implements ITestUtils {
	private static TestUtils instance;

	private TestUtils() {

	}

	public static synchronized TestUtils getInstance() {
		if (instance == null) {
			instance = new TestUtils();
		}
		return instance;

	}

	@Override
	public void mockPrivateField(Object classObj, String fieldName, Object mockObject) {
		Field userDaoField;
		try {
			userDaoField = classObj.getClass().getDeclaredField(fieldName);
			userDaoField.setAccessible(true);
			userDaoField.set(classObj, mockObject);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	

	public List<com.shpach.tutor.persistance.entities.Test> initTestsList() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = new ArrayList<>();
		com.shpach.tutor.persistance.entities.Test test_1 = new com.shpach.tutor.persistance.entities.Test();
		test_1.setTestId(1);
		com.shpach.tutor.persistance.entities.Test test_2 = new com.shpach.tutor.persistance.entities.Test();
		test_2.setTestId(2);
		expectedTests.add(test_1);
		expectedTests.add(test_2);
		return expectedTests;
	}
}
