package com.qaprosoft.qa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil
{
    public static Properties loadProperties(String filename)
    {
	Properties properties = new Properties();

	InputStream inStream = null;
	inStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(filename);
	if (inStream == null) { throw new RuntimeException("Sorry, unable to find " + filename); }

	try
	{
	    properties.load(inStream);
	}
	catch (IOException e)
	{
	    throw new RuntimeException(e);
	}
	finally
	{
	    if (inStream != null)
	    {
		try
		{
		    inStream.close();
		}
		catch (IOException e)
		{
		    throw new RuntimeException(e);
		}
	    }
	}

	return properties;
    }
}
