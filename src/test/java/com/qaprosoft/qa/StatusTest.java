package com.qaprosoft.qa;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import com.google.gson.Gson;
import com.qaprosoft.qa.domain.rq.GetStatusRequest;
import com.qaprosoft.qa.domain.rs.NodesStatus;

public class StatusTest
{
    private final static Logger LOGGER = Logger.getLogger(StatusTest.class);

    private final static Properties CFG_PROPERTIES = PropertiesUtil.loadProperties("grid.properties");

    @Parameters({ "host", "node", "browsers", "timeout" })
    @Test(groups = "acceptance")
    public void testStatus(String host, String node, String browsers, int timeout) throws Exception
    {
	GetStatusRequest getStatusRequest = Builder.buildGetStatusRequest(node, browsers, timeout);
	String json = new Gson().toJson(getStatusRequest);
	LOGGER.info("Request with body sent:");
	LOGGER.info(JsonUtils.formatJson(json));
	Resty resty = new Resty();
	JSONResource jsonResource = resty.json(String.format("http://%s:%s/grid/admin/StatusServlet", host, CFG_PROPERTIES.getProperty("grid_port")),
		new Content("application/json", json.getBytes()));
	NodesStatus nodesStatusAct = new Gson().fromJson(jsonResource.object().toString(), NodesStatus.class);
	String actualResponse = new Gson().toJson(nodesStatusAct);
	LOGGER.info("Response recieved:");
	LOGGER.info(JsonUtils.formatJson(actualResponse));

	// preparing expected rs
	NodesStatus nodesStatus = Builder.buildExpectedNodesStatus(node, browsers);
	String expectedResponse = new Gson().toJson(nodesStatus);
	// LOGGER.info("Expected response:");
	// LOGGER.info(JsonUtils.formatJson(expectedResponse));

	JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.NON_EXTENSIBLE);
    }
}
