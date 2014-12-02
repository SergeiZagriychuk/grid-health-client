package com.qaprosoft.qa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.qaprosoft.qa.domain.rq.Node;
import com.qaprosoft.qa.domain.rq.Node_;
import com.qaprosoft.qa.domain.rs.BrowserStatus;
import com.qaprosoft.qa.domain.rs.BrowserStatus_;
import com.qaprosoft.qa.domain.rs.NodeStatus;
import com.qaprosoft.qa.domain.rs.NodeStatus_;
import com.qaprosoft.qa.domain.rs.NodesStatus;

public class StatusTest
{
    private final static Logger LOGGER = Logger.getLogger(StatusTest.class);

    private final static Properties CFG_PROPERTIES = PropertiesUtil.loadProperties("grid.properties");

    @Parameters({ "host", "browsers" })
    @Test(groups = "acceptance")
    public void testStatus(String host, String browsers) throws Exception
    {
	GetStatusRequest getStatusRequest = new GetStatusRequest();
	List<Node> nodes = new ArrayList<Node>();
	Node_ node_ = new Node_();
	node_.setHost(host);
	node_.setBrowsers(Arrays.asList(browsers.split(",")));
	Node node = new Node();
	node.setNode(node_);
	nodes.add(node);
	getStatusRequest.setNodes(nodes);

	String json = new Gson().toJson(getStatusRequest);
	LOGGER.info("Request sent:");
	LOGGER.info(JsonUtils.formatJson(json));
	Resty resty = new Resty();
	JSONResource jsonResource = resty.json(
		String.format("http://%s:%s/grid/admin/StatusServlet", CFG_PROPERTIES.getProperty("grid_host"), CFG_PROPERTIES.getProperty("grid_port")),
		new Content("application/json", json.getBytes()));
	NodesStatus nodesStatusAct = new Gson().fromJson(jsonResource.object().toString(), NodesStatus.class);
	String actualResponse = new Gson().toJson(nodesStatusAct);
	LOGGER.info("Response recieved:");
	LOGGER.info(JsonUtils.formatJson(actualResponse));

	// preparing expected rs
	NodesStatus nodesStatus = new NodesStatus();
	List<NodeStatus> nodeStatuses = new ArrayList<NodeStatus>();
	NodeStatus_ nodeStatus_ = new NodeStatus_();
	nodeStatus_.setHost(host);
	nodeStatus_.setStatus("available");
	List<BrowserStatus> browserStatuses = new ArrayList<BrowserStatus>();
	for (String browser : node_.getBrowsers())
	{
	    BrowserStatus_ browserStatus_ = new BrowserStatus_();
	    browserStatus_.setBrowser(browser);
	    browserStatus_.setStatus("pass");
	    BrowserStatus browserStatus = new BrowserStatus();
	    browserStatus.setBrowserStatus(browserStatus_);
	    browserStatuses.add(browserStatus);
	}
	nodeStatus_.setBrowserStatuses(browserStatuses);
	NodeStatus nodeStatus = new NodeStatus();
	nodeStatus.setNodeStatus(nodeStatus_);
	nodeStatuses.add(nodeStatus);
	nodesStatus.setNodeStatuses(nodeStatuses);
	String expectedResponse = new Gson().toJson(nodesStatus);
	LOGGER.info("Expected response:");
	LOGGER.info(JsonUtils.formatJson(expectedResponse));

	JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.NON_EXTENSIBLE);
    }
}
