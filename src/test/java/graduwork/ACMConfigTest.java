package graduwork;

import org.junit.Test;

import com.sxy.graduwork.searchconfig.ACMSearchConfig;

public class ACMConfigTest {

	@Test
	public void testQueryString() {
		String json = "{\"anyField\":[{\"match\":\"+\",\"value\":\"123\"},{\"match\":\"+\",\"value\":\"123\"}],\"title\":[{\"match\":\"+\",\"value\":\"123\"},{\"match\":\"-\",\"value\":\"spring\"}]}";
		ACMSearchConfig config = new ACMSearchConfig(json);
		System.out.println(config.toACMQueryString());
	}
}
