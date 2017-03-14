package graduwork;

import org.junit.Test;

import com.google.gson.Gson;
import com.sxy.graduwork.searchconfig.ACMSearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;

public class ACMConfigTest {

	@Test
	public void testQueryString() {
		String json = "{\"anyField\":[{\"match\":\"+\",\"value\":\"123\"},{\"match\":\"+\",\"value\":\"123\"}],\"title\":[{\"match\":\"+\",\"value\":\"123\"},{\"match\":\"-\",\"value\":\"spring\"}]}";
		ACMSearchConfig config = new ACMSearchConfig(new Gson().fromJson(json, BasicSearchConfig.class));
		System.out.println(config.toACMQueryString());
	}
}
