package seers.bugrepcompl;

import java.io.File;

import org.junit.Test;

public class MainAgreementTest {

	@Test
	public void testMain() throws Exception {
		String[] args = { "test_data" + File.separator + "aggr", "fiorella", "oscar" };
		MainAgreement.main(args);
	}

}
