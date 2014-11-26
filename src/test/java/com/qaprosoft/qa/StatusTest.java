package com.qaprosoft.qa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
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
		Resty resty = new Resty();
		JSONResource jsonResource = resty.json("http://localhost:4444/grid/admin/StatusServlet", new Content("application/json", json.getBytes()));
		String actualResponse = new Gson().toJson(jsonResource.object());
		LOGGER.info("Response recieved:");
		LOGGER.info(new Gson().toJson(actualResponse));
		
		// preparing expected rs
		NodesStatus nodesStatus = new NodesStatus();
		nodesStatus.setDescription("available");
		nodesStatus.setMessage("success");
		List<NodeStatus> nodeStatuses = new ArrayList<NodeStatus>();
		NodeStatus_ nodeStatus_ = new NodeStatus_();
		List<BrowserStatus> browserStatuses = new ArrayList<BrowserStatus>();
		for(String browser: node_.getBrowsers())
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
		LOGGER.info(expectedResponse);
		
//		// preparing expected rs
//		String expectedResponse = new Gson().toJson(IOUtils.toString(StatusTest.class.getClassLoader().getResourceAsStream("testdata/rs.json")));
//		LOGGER.info("Expected response:");
//		LOGGER.info(expectedResponse);
//		
//		JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.STRICT);
    }
}
