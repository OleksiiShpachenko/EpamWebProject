package Service;
import org.junit.*;
import com.shpach.tutor.service.SessionServise;

import static org.junit.Assert.*;
public class SessionServiceTest {
	private SessionServise sessionServise;
	@Before
	public void init(){
		sessionServise=SessionServise.getInstance();
	}
	@Test
	public void checkSessionValid(){
		boolean actuals=sessionServise.checkSession("12345", "userName");
		assertTrue(actuals);
	}
	@Test
	public void checkSessionNotValid(){
		boolean actuals=sessionServise.checkSession("12345", null);
		assertFalse(actuals);
	}

}
