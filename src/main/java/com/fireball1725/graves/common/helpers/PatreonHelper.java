package com.fireball1725.graves.common.helpers;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.reference.ModInfo;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class PatreonHelper
{
    public static Map<String, Map<String, String>> specialText = Maps.newHashMap();

    public static void getSpecialText() {
		Map<String, Map<String, String>> specialText = Maps.newHashMap();
		Graves.logger.info(new Gson().toJson(specialText, new TypeToken<Map<String, Map<String, String>>>()
		{
		}.getType()));
		specialText.clear();
		try
		{
			specialText = new Gson().fromJson(readUrl(ModInfo.SPECIAL_TEXT), new TypeToken<Map<String, Map<String, String>>>()
			{
			}.getType());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        PatreonHelper.specialText = specialText;
    }

	private static String readUrl(String urlString) throws Exception
	{
		BufferedReader reader = null;
		try
		{
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		}
		finally
		{
			if(reader != null)
			{ reader.close(); }
		}
	}
}
