package com.qaprosoft.qa;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.testng.Reporter;

public class HtmlReportAppender extends AppenderSkeleton {

	@Override
	protected void append(LoggingEvent event) {
		String log = this.layout.format(event);
		Reporter.log(log);
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

}
