package TestUtils;

import java.util.List;

public interface ITestUtils {
	void mockPrivateField(Object classObj, String fieldName, Object mockObject);
	List<com.shpach.tutor.persistance.entities.Test>  initTestsList();

}
