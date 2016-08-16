package com.brightdairy.personal.brightdairy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
	public static final String PREF_NAME = "AppConfig";
	public static final SharedPreferences sp = GlobalConstants.APPLICATION_CONTEXT
			.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

	public static boolean getBoolean(String key, boolean defaultValue)
	{
		return sp.getBoolean(key, defaultValue);

	}

	public static void setBoolean(String key, boolean value)
	{

		sp.edit().putBoolean(key, value).commit();
	}

	public static String getString(String key, String defaultString)
	{
		return sp.getString(key, defaultString);
	}

	public static void setString(String key, String value)
	{
		sp.edit().putString(key, value).commit();
	}

	public static void setLong(String key, long value)
	{
		sp.edit().putLong(key, value).commit();
	}

	public static long getLong(String key, long defaultValue)
	{
		return sp.getLong(key, defaultValue);
	}
}
