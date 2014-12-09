package com.qaprosoft.qa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qaprosoft.qa.domain.rq.Browser;
import com.qaprosoft.qa.domain.rq.GetStatusRequest;
import com.qaprosoft.qa.domain.rq.Node;
import com.qaprosoft.qa.domain.rq.Node_;
import com.qaprosoft.qa.domain.rs.BrowserStatus;
import com.qaprosoft.qa.domain.rs.BrowserStatus_;
import com.qaprosoft.qa.domain.rs.NodeStatus;
import com.qaprosoft.qa.domain.rs.NodeStatus_;
import com.qaprosoft.qa.domain.rs.NodesStatus;

public class Builder
{
    public static GetStatusRequest buildGetStatusRequest(String nodeHost, String browsers, int timeout)
    {
	GetStatusRequest getStatusRequest = new GetStatusRequest();
	List<Node> nodes = new ArrayList<Node>();
	Node_ node_ = new Node_();
	node_.setHost(nodeHost);
	node_.setTimeout(timeout);
	List<Browser> browsersWithVersionArr = new ArrayList<Browser>();
	for (String browserWithVersion : Arrays.asList(browsers.split(",")))
	{
	    Browser browser = new Browser();
	    if (browserWithVersion.contains(":"))
	    {
		browser.setBrowserName(browserWithVersion.substring(0, browserWithVersion.indexOf(":")));
		browser.setBrowserVersion(browserWithVersion.substring(browserWithVersion.indexOf(":") + 1));
	    }
	    else
	    {
		browser.setBrowserName(browserWithVersion);
	    }
	    browsersWithVersionArr.add(browser);
	}
	node_.setBrowsers(browsersWithVersionArr);
	Node node = new Node();
	node.setNode(node_);
	nodes.add(node);
	getStatusRequest.setNodes(nodes);
	return getStatusRequest;
    }

    public static NodesStatus buildExpectedNodesStatus(String nodeHost, String browsers)
    {
	NodesStatus nodesStatus = new NodesStatus();
	List<NodeStatus> nodeStatuses = new ArrayList<NodeStatus>();
	NodeStatus_ nodeStatus_ = new NodeStatus_();
	nodeStatus_.setHost(nodeHost);
	nodeStatus_.setStatus("available");
	List<BrowserStatus> browserStatuses = new ArrayList<BrowserStatus>();
	for (String browser : Arrays.asList(browsers.split(",")))
	{
	    BrowserStatus_ browserStatus_ = new BrowserStatus_();
	    browserStatus_.setBrowser(browser.split(":")[0]);
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
	return nodesStatus;
    }

}
