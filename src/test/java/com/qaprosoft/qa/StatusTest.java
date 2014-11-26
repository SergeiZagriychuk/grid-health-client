package com.qaprosoft.qa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
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

public class StatusTest
{
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
	JSONAssert.assertEquals(IOUtils.toString(StatusTest.class.getClassLoader().getResourceAsStream("testdata/rs.json")),
		new Gson().toJson(jsonResource.object()), JSONCompareMode.STRICT);
    }
}
