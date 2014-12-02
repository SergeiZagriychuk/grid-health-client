package com.qaprosoft.qa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtils
{
    public static String formatJson(final String json)
    {
	final JsonParser parser = new JsonParser();
	final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	final JsonElement el = parser.parse(json);
	return gson.toJson(el);
    }
}
